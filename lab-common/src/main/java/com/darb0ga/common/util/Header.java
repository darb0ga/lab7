package com.darb0ga.common.util;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Header implements Serializable {
    private int blocksCount;
    private int blockNumber;

    public Header(int count, int number){
        this.blocksCount = count;
        this.blockNumber = number;
    }
}