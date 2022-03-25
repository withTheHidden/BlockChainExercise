package com.dao.blocking.pojo;

import com.dao.blocking.dto.TransactionDto;
import lombok.Data;

import java.util.List;

@Data
public class Block {
    //信息
    private String message;
    //索引
    private long index;
    //交易信息
    private List<TransactionDto> transactionDto;
    //时间戳
    private String timestamp;

    public Block(long index, List<TransactionDto> transactionDto, String timestamp) {
        this.index = index;
        this.transactionDto = transactionDto;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BlockDto{" +
                "message='" + message + '\'' +
                ", index=" + index +
                ", transactionDto=" + transactionDto +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

}
