package com.blockchain.entity;

import com.blockchain.Util.StringUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

/**
 * 交易类
 */
public class Transaction {

    public String transactionId;//交易hash

    public PublicKey sender; //付款人地址 公钥

    public PublicKey reciepient; //收款人地址 公钥

    public float value; //转移金额

    public byte[] signature; //数字签名， 防止第三方更改发送金额

    //输入列表
    public ArrayList<Transaction> inputs = new ArrayList<>();

    //输出列表
    public ArrayList<Transaction> outputs = new ArrayList<>();

    //创建的交易数
    private static int sequence = 0;

    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<Transaction> inputs) {
        this.sender = from;
        this.reciepient = to;
        this.value = value;
        this.inputs = inputs;
    }

    /**
     * 计算校验hash值
     */
    private String calculateTransactionId(){
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender)+ StringUtil.getStringFromKey(reciepient)+ Float.toString(value)+sequence
        );
    }

    /**
     * 生成签名
     * @param privateKey
     */
    public void generateSignature(PrivateKey privateKey){
        String data = StringUtil.getStringFromKey(sender)+StringUtil.getStringFromKey(reciepient)+Float.toString(value);
        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    /**
     * 验证签名
     * @return
     */
    public boolean verifySignature(){
        String data = StringUtil.getStringFromKey(sender)+StringUtil.getStringFromKey(reciepient)+Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }
}