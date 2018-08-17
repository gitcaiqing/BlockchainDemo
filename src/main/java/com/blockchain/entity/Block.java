package com.blockchain.entity;

import com.blockchain.Util.StringUtil;

import java.util.ArrayList;
import java.util.Date;

public class Block {

    public String hash;//存放数字签名

    public String prehash; //存放前面块的签名

    public String merkleRoot;

    //交易数据
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    private String data;

    private long timeStamp;

    private int nonce;

    public Block(String prehash){
        this.prehash = prehash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public Block(String data, String prehash) {
        this.prehash = prehash;
        this.data = data;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    /**
     * 添加数字签名方法
     * @return
     */
    public String calculateHash(){
        String calculatehash =
                StringUtil.applySha256(prehash + Long.toString(timeStamp) + nonce + merkleRoot);
        return calculatehash;
    }

    public void mineBlock(int difficulty) {
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block Mined--->" + hash);
    }

    public boolean addTransatcion(Transaction transaction){
        if(transaction == null){
            return false;
        }
        if (prehash != "0"){
            if(transaction.processTransaction() != true){
                System.out.println("交易失败");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("交易成功加入交易区块链");
        return true;
    }
}