package com.kamenskiy.io.core.events;

import java.math.BigDecimal;

public class DepositRequestedEvent {
    private String senderId;
    private String recepientId;
    private BigDecimal amount;

    public DepositRequestedEvent() {
    }

    public DepositRequestedEvent(String senderId, String recepientId, BigDecimal amount) {
        this.senderId = senderId;
        this.recepientId = recepientId;
        this.amount = amount;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecepientId() {
        return recepientId;
    }

    public void setRecepientId(String recepientId) {
        this.recepientId = recepientId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
