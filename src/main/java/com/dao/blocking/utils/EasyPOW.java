package com.dao.blocking.utils;

public class EasyPOW {
    private static Encrypt encrypt =new Encrypt();
    public static long proofOfWork(long lastProof){
        long proof = 0;
        while (!validProof(lastProof,proof)){
            proof ++;
        }
        return proof;
    }

    private static boolean validProof(long lastProof, long proof) {
        String guess = new StringBuilder().append(lastProof).append(proof).toString();
        return encrypt.getSHA256(guess).startsWith("0000");
    }

}
