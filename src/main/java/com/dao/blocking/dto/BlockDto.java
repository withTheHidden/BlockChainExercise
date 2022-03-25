package com.dao.blocking.dto;

import com.dao.blocking.pojo.Block;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlockDto {
    private Block block;
    private String proof;
    //hash 值
    private String nodeHash;
    //前一个结点的hash
    private String previousHash;
}
