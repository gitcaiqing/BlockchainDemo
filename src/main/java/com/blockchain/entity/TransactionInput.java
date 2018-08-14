package com.blockchain.entity;

/**
 * 交易输入类
 * 交易输入类指向交易输出类， 而不是在交易输出类减去对应得金额，在交易输入类加上对应得金额
 */
public class TransactionInput {

    //指向交易输出类的transactionid
    public String transactionOutputId;

    //未使用的交易输出
    public TransactionOutput UTXO;

    public TransactionInput(String transactionOutputId){
        this.transactionOutputId = transactionOutputId;
    }
}