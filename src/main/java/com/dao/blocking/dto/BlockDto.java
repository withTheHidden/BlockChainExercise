package com.dao.blocking.dto;

import lombok.Data;

import java.util.List;

@Data
public class BlockDto {
    private String message;
    private long index;
    private List<TransactionDto> transactionDto;
    private String proof;
    private String previousHash;
    private String timestamp;

    public BlockDto(long index, List<TransactionDto> transactionDto, String proof, String previousHash,String timestamp) {
        this.index = index;
        this.transactionDto = transactionDto;
        this.proof = proof;
        this.previousHash = previousHash;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BlockDto{" +
                "message='" + message + '\'' +
                ", index=" + index +
                ", transactionDto=" + transactionDto +
                ", proof='" + proof + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    public BlockDto(String proof, String timestamp) {
        this.proof = proof;
        this.timestamp = timestamp;
    }
}
