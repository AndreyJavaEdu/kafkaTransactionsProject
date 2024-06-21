package com.kamenskiy.io.transfers.service;

import com.kamenskiy.io.transfers.model.TransferRestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransferServiceImpl implements TransferService  {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private KafkaTemplate<String, Object> kafkaTemplate;
    private Environment environment;
    private RestTemplate restTemplate;

    public TransferServiceImpl(KafkaTemplate<String, Object> kafkaTemplate, Environment environment, RestTemplate restTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.environment = environment;
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean transfer(TransferRestModel productPaymentRestModel) {




        return false;
    }
}
