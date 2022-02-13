package com.dao.blocking.dto;

import java.util.List;


public class ChainInfoDto {

    private List<BlockDto> chain;
    private Long length;
    private String message;

    public ChainInfoDto(List<BlockDto> chain, long length) {
        this.chain = chain;
        this.length = length;
    }

    public List<BlockDto> getChain() {
        return chain;
    }

    public void setChain(List<BlockDto> chain) {
        this.chain = chain;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
