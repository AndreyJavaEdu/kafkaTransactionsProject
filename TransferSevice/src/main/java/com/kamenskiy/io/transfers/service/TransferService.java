package com.kamenskiy.io.transfers.service;

import com.kamenskiy.io.transfers.model.TransferRestModel;
import org.springframework.stereotype.Service;


public interface TransferService {
    public boolean transfer(TransferRestModel productPaymentRestModel);
}
