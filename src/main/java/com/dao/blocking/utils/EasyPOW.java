package com.dao.blocking.utils;

import com.dao.blocking.pojo.ProofAndHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class EasyPOW {
    @Autowired
    Encrypt encrypt;
    public Long proofOfWork(long lastProof){
        long proof = 0;
        String hash = guessProof(lastProof,proof);
        while (!hash.startsWith("0000")){
            proof++;
            hash = guessProof(lastProof,proof);
        }
        return proof;
    }

    /**
     * 计算下一区块,此处要求下一区块hash以0000开头
     * @param lastProof 上一份区块的工作量
     * @param proof 本此计算的工作量
     * @return
     */
    private  String guessProof(long lastProof, long proof) {
        String guess = new StringBuilder().append(lastProof).append(proof).toString();
        return encrypt.getSHA256(guess);
    }

    /**
     * 验证工作证明
     * @param lastProof
     * @param proof
     * @return
     */
    public boolean validProof(String lastProof, String proof){
        String guess = new StringBuilder().append(lastProof).append(proof).toString();
        return encrypt.getSHA256(guess).startsWith("0000");
    }
}
