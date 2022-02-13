package com.dao.blocking.controller;

import com.dao.blocking.dto.BlockDto;
import com.dao.blocking.dto.ChainInfoDto;
import com.dao.blocking.dto.TransactionDto;
import com.dao.blocking.pojo.BlockChain;
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
    @GetMapping("mine")
    public BlockDto mine(ServletContext servletContext){
        BlockChain blockChain = BlockChain.Inner.getInstance();
        BlockDto lastBlock = blockChain.getLastBlock();

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

    @PostMapping("/nodes/register")
    public Set<String> nodeRegister(@RequestBody List<String> nodes){
        if (CollectionUtils.isEmpty(nodes)){
            throw new RuntimeException("valid list");
        }
        BlockChain blockChain = BlockChain.Inner.getInstance();
        for (String node : nodes) {
            blockChain.registerNode(node);
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
