package com.dao.blocking.pojo;

import com.dao.blocking.dto.BlockDto;
import com.dao.blocking.dto.TransactionDto;
import com.dao.blocking.utils.EasyPOW;
import com.dao.blocking.utils.Encrypt;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jamesguo
 */
@Data
public class BlockChain {
    private List<BlockDto> chain;
    private List<TransactionDto> currentTransactions;
    private Gson gson ;
    private BlockChain() {
        //反反射创建实例
        if (null!=Inner.getInstance()){
            throw new RuntimeException("nullllllllllllllll");
        }
        this.chain = new ArrayList<>();
        this.currentTransactions = new ArrayList<TransactionDto>();
        gson = new GsonBuilder().create();
        //初始
        newBlock(new BlockDto(String.valueOf(100),String.valueOf(System.currentTimeMillis())), "0");
    }

    public BlockDto lastBlock() {
        return getChain().get(getChain().size() - 1);
    }

    /**
    *防止反序列化破坏单例,但因为没有实现 序列化接口因此可以反序列化
    */
    private Object readResolve(){
        return Inner.getInstance();
    }

    //    public static BlockChain getInstance(){
//        if (null == blockChain){
//            synchronized (BlockChain.class){
//                if (null == blockChain){
//                    blockChain = new BlockChain();
//                }
//            }
//        }
//        return blockChain;
//    }
//
   public static class Inner {
        private static final BlockChain BLOCK_CHAIN = new BlockChain();
        public  static BlockChain  getInstance(){
            return BLOCK_CHAIN;
        }
    }


    public BlockDto newBlock(BlockDto blockDto, String previousHash) {
        // 如果没有传递上一个区块的hash就计算出区块链中最后一个区块的hash
        previousHash = previousHash != null ? previousHash : hash(getChain().get(getChain().size() - 1));
        blockDto.setPreviousHash(previousHash);
        blockDto.setTransactionDto(getCurrentTransactions());
        // 重置当前的交易信息列表
        setCurrentTransactions(new ArrayList<TransactionDto>());
        getChain().add(blockDto);
        return blockDto;
    }

    private String hash(BlockDto blockDto) {
        return new Encrypt().getSHA256(gson.toJson(blockDto));
    }

    /**
     * 返回索引
     */
    public long newTransactions(String sender, String recipient, Long amount) {
        TransactionDto transaction = new TransactionDto(sender,recipient,amount);
        getCurrentTransactions().add(transaction);
        return lastBlock().getIndex() + 1;
    }

    /**
     * 工作量认证
     * @param lastProof
     * @return
     */
    public long proofOfWork(long lastProof) {
        return EasyPOW.proofOfWork(lastProof);
    }


}
