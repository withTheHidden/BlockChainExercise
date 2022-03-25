package com.dao.blocking.pojo;

import lombok.Data;

@Data
public class ProofAndHash {
    private long proof;
    private String nodeHash;

    public ProofAndHash(long proof, String nodeHash) {
        this.proof = proof;
        this.nodeHash = nodeHash;
    }
}

