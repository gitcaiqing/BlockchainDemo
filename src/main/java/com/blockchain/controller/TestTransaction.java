package com.blockchain.controller;

import com.blockchain.Util.StringUtil;
import com.blockchain.entity.Block;
import com.blockchain.entity.Transaction;
import com.blockchain.entity.TransactionOutput;
import com.blockchain.entity.Wallet;
import com.google.gson.GsonBuilder;

import java.security.Security;
import java.util.*;

public class TestTransaction {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();

    //维护一个未使用的交易集合
    public static Map<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();

    public static int difficulty = 3;

    public static float minimumTransaction = 0.1f;

    public static Wallet walletA;
    public static Wallet walletB;

    public static Transaction genesisTransaction;


    public static void main(String[] args) {

        //调用Bouncey castle作为安全性的提供类
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        //--创建钱包
        walletA = new Wallet();
        walletB = new Wallet();

        //测试公钥私钥
        System.out.println("walletA 的公钥："+ StringUtil.getStringFromKey(walletA.privateKey));
        System.out.println("walletA 的私钥："+ StringUtil.getStringFromKey(walletA.publicKey));

        //--创建一个交易从A->B
        //Transaction transaction = new Transaction(walletA.publicKey,walletB.publicKey,5,null);

        //用walletA私钥进行签名
        //transaction.generateSignature(walletA.privateKey);

        //通过walletA的公钥验证签名是否工作
        //System.out.println("walletA的公钥验证签名是否工作："+transaction.verifySignature());


        Wallet coinbase = new Wallet();
        //创建创世纪交易，将100个货币发送给walletA
        genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
        //对创世纪交易签名
        genesisTransaction.generateSignature(coinbase.privateKey);
        //交易id
        genesisTransaction.transactionId = "0";
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId));
        //交易输出
        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));
        System.out.println("创建创世纪交易区块链");
        Block genesis = new Block("0");

        genesis.addTransatcion(genesisTransaction);
        addBlock(genesis);
        System.out.println("创世纪钱包将100个货币发送给walletA");
        System.out.println("创世纪钱包的金额："+coinbase.getBalance());
        System.out.println("walletA的金额："+walletA.getBalance());

        //Block block1 = new Block(genesis.hash);


        System.out.println("----------------------------------------");

        System.out.println("创建第二个区块链");
        Block  block1 = new Block(genesis.hash);
        //创建新的交易walletA将40个货币转至walletB并加入区块链中
        System.out.println("walletA将40个货币转至walletB");
        block1.addTransatcion(walletA.sendFounds(walletB.publicKey, 40f));
        addBlock(block1);
        System.out.println();
        System.out.println("walletA的金额："+walletA.getBalance());
        System.out.println("walletB的金额："+walletB.getBalance());
        System.out.println("----------------------------------------");


//        String blockchainJson = blcokchainToJson(blockchain);
//        System.out.println("交易区块链详情："+blockchainJson);

    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }

    public static String blcokchainToJson(List<Block> blockList){
        return new GsonBuilder().setPrettyPrinting().create().toJson(blockList);
    }
}