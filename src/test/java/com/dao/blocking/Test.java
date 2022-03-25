package com.dao.blocking;

import com.dao.blocking.pojo.Block;
import com.dao.blocking.pojo.BlockChain;
import com.dao.blocking.pojo.ProofAndHash;
import com.dao.blocking.utils.EasyPOW;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BlockChain blockChain = new BlockChain();
        blockChain.newTransactions("one","two",3333L);
        EasyPOW easyPOW = new EasyPOW();
        Long proofAndHash = easyPOW.proofOfWork(100);
        Block block = blockChain.newBlock(proofAndHash, null);
        System.out.println(gson.toJson(block));

//        blockChain.newTransactions("james","guo",9999L);
//        BlockDto blockWithOneTransaction = blockChain.newBlock(new BlockDto(String.valueOf(500), String.valueOf(System.currentTimeMillis())), null);
//        System.out.println("two  :"+ gson.toJson(blockWithOneTransaction));
//
//        System.out.println("-----------------------------------------");
//        blockChain.newTransactions("xss1","guo",8888L);
//        blockChain.newTransactions("asdasd2","guo",21233L);
//        blockChain.newTransactions("2sad3","fsxq",2313L);
//        BlockDto blockWithSomeTransaction = blockChain.newBlock(new BlockDto(String.valueOf(600), String.valueOf(System.currentTimeMillis())), null);
//        System.out.println("three  :"+ gson.toJson(blockWithSomeTransaction));

        Map<String, Object> chain = new HashMap<>();
        chain.put("chain",blockChain.getChain());
        chain.put("length",blockChain.getChain().size());
        System.out.println("----------------------------------------------");
        System.out.println("chain : "+ gson.toJson(chain));

//        //普通单例验证
//        System.out.println("-----------_____-------------________------------_________-");
//        BlockChain instance = BlockChain.Inner.getInstance();
//        Map<String, Object> chain2 = new HashMap<>();
//        chain2.put("chain",instance.getChain());
//        chain2.put("length",instance.getChain().size());
//
//        System.out.println("chain : "+ gson.toJson(chain2));

        //反序列化验证,可以不支持序列化防止改动

//        System.out.println("-------------------反序列化验证---------------");
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream("out.obj");
//            @Cleanup
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
//            objectOutputStream.writeObject(blockChain);
//
//            objectOutputStream.flush();
//            @Cleanup
//            FileInputStream fileInputStream = new FileInputStream("out.obj");
//            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//            BlockChain chainFromText = (BlockChain)objectInputStream.readObject();
//            System.out.println(chainFromText == blockChain);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        System.out.println("反射测试:_________________________________________________");
////反射测试
//        Constructor<BlockChain> declaredConstructor = null;
//        try {
//            declaredConstructor = BlockChain.class.getDeclaredConstructor();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//        declaredConstructor.setAccessible(true);
//
//        BlockChain reflect = null;
//        try {
//            reflect = declaredConstructor.newInstance();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(reflect);
//    }
}
}
