package com.blockchain.controller;

import com.blockchain.Util.StringUtil;
import com.blockchain.entity.Block;
import com.blockchain.entity.Transaction;
import com.blockchain.entity.TransactionOutput;
import com.blockchain.entity.Wallet;

import java.security.Security;
import java.util.*;

public class TestTransaction {

    //维护一个未使用的交易集合
    public static List<Block> blockArraylist = new ArrayList<Block>(;

    public static Map<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();


    public static int diffculty = 3;
    public static Wallet walletA;
    public static Wallet walletB;


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
        Transaction transaction = new Transaction(walletA.publicKey,walletB.publicKey,5,null);

        //用walletA私钥进行签名
        transaction.generateSignature(walletA.privateKey);

        //通过walletA的公钥验证签名是否工作
        System.out.println("walletA的公钥验证签名是否工作："+transaction.verifySignature());



    }
}