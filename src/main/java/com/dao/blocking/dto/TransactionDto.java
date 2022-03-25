package com.dao.blocking.dto;

import lombok.Data;
import lombok.NonNull;
@Data
public class TransactionDto {
    @NonNull
    private String sender;
    @NonNull
    private String recipient;
    @NonNull
    private Long amount;

    public TransactionDto(String sender, String recipient, Long amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }

}
