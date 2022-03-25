package com.dao.blocking.controller;

import com.dao.blocking.dto.*;
import com.dao.blocking.pojo.Block;
import com.dao.blocking.pojo.BlockChain;
import com.dao.blocking.pojo.ProofAndHash;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TransactionApiController {
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private BlockChain blockChain;
    @GetMapping("mine")
    public Block mine(){
        String lastProof = blockChain.getLastProof();
        Long proof = blockChain.proofOfWork(Long.parseLong(lastProof));
        String uuid = (String) servletContext.getAttribute("uuid");
        blockChain.newTransactions("0", uuid, 1L);
        return blockChain.newBlock(proof, null);
    }

    @PostMapping("transactions/new")
    public String newTransaction(@RequestBody TransactionDto transaction){
        long index = blockChain.newTransactions(transaction.getSender(), transaction.getRecipient(), transaction.getAmount());
        return "Transaction will be added to Block" + index;
    }

    @GetMapping("chain")
    public ChainInfoDto chain(){
        return new ChainInfoDto(blockChain.getChain(),blockChain.getChain().size());

    }

    @PostMapping("/nodes/register")
    public Set<String> nodeRegister(@RequestBody Register register){
        List<String> nodes = register.getNodes();
        if (CollectionUtils.isEmpty(nodes)){
            throw new RuntimeException("valid list");
        }
        for (Object node : nodes) {
            blockChain.registerNode((String) node);
        }
        return blockChain.getNodes();
    }

    @GetMapping("/nodes/resolve")
    public ChainInfoDto resolve(){
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
