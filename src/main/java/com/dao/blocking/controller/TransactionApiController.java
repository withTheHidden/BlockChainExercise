package com.dao.blocking.controller;

import com.dao.blocking.dto.BlockDto;
import com.dao.blocking.dto.ChainInfoDto;
import com.dao.blocking.dto.TransactionDto;
import com.dao.blocking.pojo.BlockChain;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.util.Map;

/**
 * @author jamesguo
 */
@RestController
@RequestMapping("/")
public class TransactionApiController {
    @GetMapping("mine")
    public BlockDto mine(ServletContext servletContext){
        BlockChain blockChain = BlockChain.Inner.getInstance();
        BlockDto lastBlock = blockChain.lastBlock();

        String uuid = (String) servletContext.getAttribute("uuid");
        blockChain.newTransactions("0", uuid, 1L);

        return blockChain.newBlock(lastBlock, null);

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
}
