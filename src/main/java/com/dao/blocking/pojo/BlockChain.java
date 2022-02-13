package com.dao.blocking.pojo;

import com.dao.blocking.dto.BlockDto;
import com.dao.blocking.dto.ChainInfoDto;
import com.dao.blocking.dto.TransactionDto;
import com.dao.blocking.utils.EasyPOW;
import com.dao.blocking.utils.Encrypt;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Cleanup;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author jamesguo
 */
@Data
public class BlockChain {
    private List<BlockDto> chain;
    private List<TransactionDto> currentTransactions;
    /**
     * 分布式节点
     */
    private Set<String> nodes;

    private Gson gson;

    private BlockChain() {
        //防止反射创建实例
        if (null != Inner.getInstance()) {
            throw new RuntimeException("nullllllllllllllll");
        }
        this.chain = new ArrayList<>();
        this.currentTransactions = new ArrayList<TransactionDto>();
        gson = new GsonBuilder().create();
        nodes = new HashSet<>();

        //初始
        newBlock(new BlockDto(String.valueOf(100), String.valueOf(System.currentTimeMillis())), "0");
    }

    public BlockDto getLastBlock() {
        return getChain().get(getChain().size() - 1);
    }

    /**
     * 防止反序列化破坏单例,但因为没有实现 序列化接口因此可以反序列化
     */
    private Object readResolve() {
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

        public static BlockChain getInstance() {
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
        TransactionDto transaction = new TransactionDto(sender, recipient, amount);
        getCurrentTransactions().add(transaction);
        return getLastBlock().getIndex() + 1;
    }

    /**
     * 工作量认证
     *
     * @param lastProof
     * @return
     */
    public long proofOfWork(long lastProof) {
        return EasyPOW.proofOfWork(lastProof);
    }

    @SneakyThrows
    public void registerNode(String address) {
        URL url = new URL(address);
        StringBuilder append = new StringBuilder(url.getHost()).append(":").append(url.getPort() == -1 ? url.getDefaultPort() : url.getPort());
        nodes.add(append.toString());
    }

    /**
     * 校验全链是否有问题
     *
     * @param chain 全链
     * @return
     */
    public boolean validChain(List<BlockDto> chain) {
        BlockDto preBlock = chain.get(0);
        int currentIndex = 1;
        while (currentIndex < chain.size()) {
            BlockDto block = chain.get(currentIndex);
            //log
            if (!block.getPreviousHash().equals(hash(preBlock))) {
                return false;
            }

            preBlock = block;
            currentIndex++;
        }
        return true;
    }

    @SneakyThrows
    public boolean resolveConflicts() {
        Set<String> localNodes = this.nodes;
        List<BlockDto> newChain = null;

        long maxLength = this.chain.size();

        for (String node : localNodes) {
            URL url = new URL("http://" + node + "/java/chain");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() == 200) {
                @Cleanup
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "utf-8"));
                StringBuffer responseData = new StringBuffer();
                String response = null;
                while ((response = bufferedReader.readLine()) != null) {
                    responseData.append(response);
                }
                //有问题?
                ChainInfoDto chainInfoDto = gson.fromJson(responseData.toString(), ChainInfoDto.class);
                Long length = chainInfoDto.getLength();
                List<BlockDto> chain = chainInfoDto.getChain();

                if (length > maxLength && validChain(chain)) {
                    maxLength = length;
                    newChain = chain;
                }
            }

        }
        if (null!=newChain){
            this.chain = newChain;
            return true;
        }
        return false;
    }
}
