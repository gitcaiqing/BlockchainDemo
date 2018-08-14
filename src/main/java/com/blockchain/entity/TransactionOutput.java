package com.blockchain.entity;

import com.blockchain.Util.StringUtil;

import java.security.PublicKey;

/**
 * 交易输出类
 */
public class TransactionOutput {

    public String id;

    public PublicKey reciepient;//持有者公钥

    public float value; //交易金额

    public String parentTransactionId;//交易编号

    public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId) {
        this.reciepient = reciepient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(
                StringUtil.getStringFromKey(reciepient)+Float.toString(value)+parentTransactionId
        );
    }

    public boolean isMine(PublicKey publicKey){
        return publicKey == reciepient;
    }
}