//package com.dao.blocking.pojo;
//
//import com.dao.blocking.dto.BlockDto;
//import com.dao.blocking.dto.ChainInfoDto;
//import com.dao.blocking.dto.TransactionDto;
//import com.dao.blocking.utils.EasyPOW;
//import com.dao.blocking.utils.Encrypt;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import lombok.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Repository;
//
//import javax.annotation.PostConstruct;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//@Data
//@Component
//public class BlockChainInstance {
//    private List<BlockDto> chain;
//    private List<TransactionDto> currentTransactions;
//    private Set<String> nodes;
//    private Gson gson;
//    @Autowired
//    EasyPOW easyPOW;
//    @PostConstruct
//    private void init() {
//        chain = new ArrayList<>();
//        currentTransactions = new ArrayList<TransactionDto>();
//        nodes = new HashSet<>();
//        gson = new GsonBuilder().create();
//        //初始块定义
//        BlockDto blockDto = new BlockDto(
//                0,
//                currentTransactions,
//                String.valueOf(100),
//                null,
//                String.valueOf(System.currentTimeMillis()),
//                "0"
//                );
//        chain.add(blockDto);
//    }
//
//    /**
//     * 获取最后一个节点
//     * @return BlockDto
//     */
//    public BlockDto getLastBlock() {
//        return getChain().get(getChain().size() - 1);
//    }
//
////    /**
////     * 防止反序列化破坏单例,但因为没有实现 序列化接口因此可以反序列化
////     */
////    private Object readResolve() {
////        return BlockChain.Inner.getInstance();
////    }
//
//    //    public static BlockChain getInstance(){
////        if (null == blockChain){
////            synchronized (BlockChain.class){
////                if (null == blockChain){
////                    blockChain = new BlockChain();
////                }
////            }
////        }
////        return blockChain;
////    }
////
//
//    /**
//     * 计算新的区块
//     * @param proof 工作量及当前hash
//     * @param previousHash 前一个区块的hash,创世区块为 0
//     * @return
//     */
//    public BlockDto newBlock(ProofAndHash proof, String previousHash) {
//        // 如果没有传递上一个区块的hash就计算出区块链中最后一个区块的hash
//        previousHash = previousHash != null ? previousHash : hash(getChain().get(getChain().size() - 1));
//        //新生成区块
//        BlockDto blockDto = new BlockDto(
//                getChain().size()+1,
//                getCurrentTransactions(),
//                String.valueOf(proof.getProof()),
//                previousHash,
//                String.valueOf(System.currentTimeMillis()),proof.getNodeHash()
//        );
//
//        // 重置当前的交易信息列表
//        setCurrentTransactions(new ArrayList<TransactionDto>());
//        getChain().add(blockDto);
//        return blockDto;
//    }
//
//    private String hash(BlockDto blockDto) {
//        return new Encrypt().getSHA256(gson.toJson(blockDto));
//    }
//
//    /**
//     * 添加交易信息
//     * @param sender 发送者
//     * @param recipient 接收者
//     * @param amount 量
//     * @return 新增的交易信息的索引
//     */
//    public long newTransactions(String sender, String recipient, Long amount) {
//        TransactionDto transaction = new TransactionDto(sender, recipient, amount);
//        //分布式锁?
//        getCurrentTransactions().add(transaction);
//        return getLastBlock().getIndex() + 1;
//    }
//
//    /**
//     * 计算下一份区块所耗费的工作量
//     * @param lastProof
//     * @return
//     */
//    public ProofAndHash proofOfWork(long lastProof) {
//        return easyPOW.proofOfWork(lastProof);
//    }
//
//    @SneakyThrows
//    public void registerNode(String address) {
//        URL url = new URL(address);
//        StringBuilder append = new StringBuilder(url.getHost()).append(":").append(url.getPort() == -1 ? url.getDefaultPort() : url.getPort());
//        nodes.add(append.toString());
//    }
//
//    /**
//     * 校验全链是否有问题
//     *
//     * @param chain 全链
//     * @return
//     */
//    public boolean validChain(List<BlockDto> chain) {
//        BlockDto preBlock = chain.get(0);
//        int currentIndex = 1;
//        while (currentIndex < chain.size()) {
//            BlockDto block = chain.get(currentIndex);
//            //log
//            if (!block.getPreviousHash().equals(hash(preBlock))) {
//                return false;
//            }
//
//            preBlock = block;
//            currentIndex++;
//        }
//        return true;
//    }
//
//    @SneakyThrows
//    public boolean resolveConflicts() {
//        Set<String> localNodes = this.nodes;
//        List<BlockDto> newChain = null;
//
//        long maxLength = this.chain.size();
//
//        for (String node : localNodes) {
//            URL url = new URL("http://" + node + "/java/chain");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.connect();
//
//            if (connection.getResponseCode() == 200) {
//                @Cleanup
//                BufferedReader bufferedReader = new BufferedReader(
//                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
//                StringBuilder responseData = new StringBuilder();
//                String response = null;
//                while ((response = bufferedReader.readLine()) != null) {
//                    responseData.append(response);
//                }
//                //有问题?
//                ChainInfoDto chainInfoDto = gson.fromJson(responseData.toString(), ChainInfoDto.class);
//                Long length = chainInfoDto.getLength();
//                List<BlockDto> chain = chainInfoDto.getChain();
//
//                if (length > maxLength && validChain(chain)) {
//                    maxLength = length;
//                    newChain = chain;
//                }
//            }
//
//        }
//        if (null!=newChain){
//            this.chain = newChain;
//            return true;
//        }
//        return false;
//    }
//}
