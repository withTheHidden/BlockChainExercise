package com.dao.blocking.controller;

import com.dao.blocking.dto.BlockDto;
import com.dao.blocking.dto.ChainInfoDto;
import com.dao.blocking.dto.Register;
import com.dao.blocking.dto.TransactionDto;
import com.dao.blocking.pojo.BlockChain;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.Set;

/**
 * @author jamesguo
 */
@RestController
@RequestMapping("java")
public class TransactionApiController {
    @Autowired
    private ServletContext servletContext;

    @GetMapping("mine")
    public BlockDto mine(){
        BlockChain blockChain = BlockChain.Inner.getInstance();
        BlockDto lastBlock = blockChain.getLastBlock();
        String lastProof = lastBlock.getProof();
        Long proof = blockChain.proofOfWork(Long.parseLong(lastProof));

        String uuid = (String) servletContext.getAttribute("uuid");
        blockChain.newTransactions("0", uuid, 1L);
        return blockChain.newBlock(proof, null);

    }

    @PostMapping("transactions/new")
    public String newTransaction(@RequestBody TransactionDto transaction){
        BlockChain blockChain = BlockChain.Inner.getInstance();
        long index = blockChain.newTransactions(transaction.getSender(), transaction.getRecipient(), transaction.getAmount());
        return "Transaction will be added to Block" + index;
    }

    @GetMapping("chain")
    public ChainInfoDto chain(){
        BlockChain blockChain = BlockChain.Inner.getInstance();
        return new ChainInfoDto(blockChain.getChain(),blockChain.getChain().size());

    }

    @PostMapping("/nodes/register")
    public Set<String> nodeRegister(@RequestBody Register register){
        List<String> nodes = register.getNodes();
        if (CollectionUtils.isEmpty(nodes)){
            throw new RuntimeException("valid list");
        }
        BlockChain blockChain = BlockChain.Inner.getInstance();
        for (Object node : nodes) {
            blockChain.registerNode((String) node);
        }
        return blockChain.getNodes();
    }

    @GetMapping("/nodes/resolve")
    public ChainInfoDto resolve(){
        BlockChain blockChain = BlockChain.Inner.getInstance();

        boolean replacedChain = blockChain.resolveConflicts();
        List<BlockDto> chain = blockChain.getChain();
        String msg = "replaced";
        if (!replacedChain){
            msg = "authoritative";
        }
        ChainInfoDto chainInfoDto = new ChainInfoDto(chain,chain.size());
        chainInfoDto.setMessage(msg);

        return chainInfoDto;
    }


}
