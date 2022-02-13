package com.dao.blocking.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChainInfoDto {
    private Map<String, Object> chain;

    public ChainInfoDto(List chain, int length) {
        this.chain = new HashMap<String, Object>() {{
            put("chain", chain);
            put("length", length);
        }};
    }

    public Map<String, Object> getChain() {
        return chain;
    }

    public void setChain(Map<String, Object> chain) {
        this.chain = chain;
    }
}
