package com.blockchain.controller;

import com.blockchain.entity.Block;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 区块链测试
 * 学习地址：https://blog.csdn.net/u010093971/article/details/79358730
 */
public class TestBlockchain {

    //创建区块难度系数
    public static int difficulty = 4;

    public static ArrayList<Block> blockchain = new ArrayList<Block>();

    public static void main(String[] args) {

        //创建区块链测试
        //createBlock();

        long beginTime1 = new Date().getTime();
        //创建第一个区块
        //第二个参数为上一区块hash值，如果未创世纪区块 则为初始为0；
        blockchain.add(new Block("第一个区块", "0"));
        blockchain.get(0).mineBlock(difficulty);
        long endTime1 = new Date().getTime();
        System.out.println("创建第一个区块耗时： " + (endTime1 - beginTime1));

        //创建第二个区块, prehash为第一个区块的hash值
        blockchain.add(new Block("第二个区块", blockchain.get(0).hash));
        blockchain.get(1).mineBlock(difficulty);
        long endTime2 = new Date().getTime();
        System.out.println("创建第二个区块耗时： " + (endTime2 - endTime1));

        //创建第三个区块, prehash为第一个区块的hash值
        blockchain.add(new Block("第三个区块", blockchain.get(1).hash));
        blockchain.get(2).mineBlock(difficulty);
        long endTime3 = new Date().getTime();
        System.out.println("创建第三个区块耗时： " + (endTime3 - endTime2));

        //验证区块链是否合法
        System.out.println("区块链是否合法："+isChainValid());

        String blockchainJson = blcokchainToJson(blockchain);
        System.out.println(blockchainJson);
    }

    /**
     * 创建区块链
     */
    public static void createBlock(){
        Block first = new Block("Hi i am the first block", "0");
        System.out.println("Hash for block 1 : " + first.hash);

        blockchain.add(first);

        Block second = new Block("Hi i am the second block", first.hash);
        System.out.println("Hash for block 2 : " + second.hash);

        blockchain.add(second);


        Block third = new Block("Hi i am the third block", second.hash);
        System.out.println("Hash for block 3 : " + third.hash);

        blockchain.add(third);

        String blockchainJson = blcokchainToJson(blockchain);
        System.out.println(blockchainJson);
    }

    public static String blcokchainToJson(List<Block> blockList){
        return new GsonBuilder().setPrettyPrinting().create().toJson(blockList);
    }


    /**
     * 区块链完整性验证
     * @return
     */
    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Current Hashes not equal!");
                return false;
            }

            if (!previousBlock.hash.equals(currentBlock.prehash)) {
                System.out.println("Previous Hashes not equal!");
                return false;
            }

            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }
}