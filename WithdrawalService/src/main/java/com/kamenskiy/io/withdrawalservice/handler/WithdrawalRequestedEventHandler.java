package com.kamenskiy.io.withdrawalservice.handler;

import com.kamenskiy.io.core.events.WithdrawalRequestedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

@KafkaListener
public class WithdrawalRequestedEventHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @KafkaHandler
    public void handleEvent(@Payload WithdrawalRequestedEvent event) {
        LOGGER.info("Received event: {}", event.getAmount());
    }
}
