package com.kamenskiy.io.transfers.service;


import com.kamenskiy.io.core.events.DepositRequestedEvent;
import com.kamenskiy.io.core.events.WithdrawalRequestedEvent;

import com.kamenskiy.io.transfers.error.TransferServiceException;
import com.kamenskiy.io.transfers.model.TransferRestModel;
import com.kamenskiy.io.transfers.persistence.TransferEntity;
import com.kamenskiy.io.transfers.persistence.TransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class TransferServiceImpl implements TransferService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private KafkaTemplate<String, Object> kafkaTemplate;
    private Environment environment;
    private RestTemplate restTemplate;
    private TransferRepository transferRepository;

    public TransferServiceImpl(KafkaTemplate<String, Object> kafkaTemplate, Environment environment, RestTemplate restTemplate,
                               TransferRepository transferRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.environment = environment;
        this.restTemplate = restTemplate;
        this.transferRepository = transferRepository;
    }

    @Override
    @Transactional(transactionManager = "transactionManager")
    public boolean transfer(TransferRestModel transferRestModel) {
        WithdrawalRequestedEvent withdrawalEvent = new WithdrawalRequestedEvent(transferRestModel.getSenderId(),
                transferRestModel.getRecepientId(), transferRestModel.getAmount());
        DepositRequestedEvent depositEvent = new DepositRequestedEvent(transferRestModel.getSenderId(),
                transferRestModel.getRecepientId(), transferRestModel.getAmount());

        try {
            TransferEntity transferEntity = new TransferEntity();
            BeanUtils.copyProperties(transferRestModel, transferEntity);
            transferEntity.setTransferId(UUID.randomUUID().toString());
            transferRepository.save(transferEntity);
            kafkaTemplate.send(environment.getProperty("withdraw-money-topic", "withdraw-money-topic"),
                    withdrawalEvent);
            LOGGER.info("Sending withdrawal event to withdrawal topic");

            //Business logic that causes and error
            callRemoteService();

            kafkaTemplate.send(environment.getProperty("deposit-money-topic", "deposit-money-topic"),
                    depositEvent);
            LOGGER.info("Sending deposit event to deposit topic");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new TransferServiceException(e);
        }
        return true;
    }


    /*
    public boolean transfer(TransferRestModel transferRestModel) {
        WithdrawalRequestedEvent withdrawalEvent = new WithdrawalRequestedEvent(transferRestModel.getSenderId(),
                transferRestModel.getRecepientId(), transferRestModel.getAmount());
        DepositRequestedEvent depositEvent = new DepositRequestedEvent(transferRestModel.getSenderId(),
                transferRestModel.getRecepientId(), transferRestModel.getAmount());

        try {
            Boolean result = kafkaTemplate.executeInTransaction(t -> {
                t.send(environment.getProperty("withdraw-money-topic", "withdraw-money-topic"),
                        withdrawalEvent);
                LOGGER.info("Sending withdrawal event to withdrawal topic");
                t.send(environment.getProperty("deposit-money-topic", "deposit-money-topic"),
                        depositEvent);
                LOGGER.info("Sending deposit event to deposit topic");
                return true;
            });
            callRemoteService();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new TransferServiceException(e);
        }
        return true;
    }
*/
    private ResponseEntity<String> callRemoteService() throws Exception {
        String requestUrl = "http://localhost:8082/response/200";
        ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);

        if (response.getStatusCode().value() == HttpStatus.SERVICE_UNAVAILABLE.value()) {
            throw new Exception("Destination Microservice is not available");
        }
        if (response.getStatusCode().value() == HttpStatus.OK.value()) {
            LOGGER.info("Destination Microservice is available and received response from mock service");
        }
        return response;
    }
}
