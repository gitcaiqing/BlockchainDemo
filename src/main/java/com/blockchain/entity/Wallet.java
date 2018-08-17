package com.blockchain.entity;

import com.blockchain.controller.TestTransaction;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 钱包类， 包含公钥和私钥
 */
public class Wallet {

    public PrivateKey privateKey;

    public PublicKey publicKey;

    public HashMap<String, TransactionOutput> UTXOS = new HashMap<>();//钱包自身的UTXO

    public Wallet() {
        generateKeyPair();
    }

    /**
     * 生成公钥和私钥
     */
    public void generateKeyPair(){
        try {
            //椭圆曲线加密算法（ECDSA）
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("prime192v1");
            keyPairGenerator.initialize(ecGenParameterSpec, random);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 返回余额并保存钱包自身的UTXO
     * @return
     */
    public float getBalance(){
        float total = 0;
        for(Map.Entry<String, TransactionOutput> item: TestTransaction.UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            if(UTXO.isMine(publicKey)){
                //保存到钱包自身的UTXO
                UTXOS.put(UTXO.id, UTXO);
                total += UTXO.value;
            }
        }
        return total;
    }

    /**
     * 创建并返回属于这个钱包的一个新交易
     */
    public Transaction sendFounds(PublicKey recipient, float value){
        if(getBalance() < value){
            System.out.println("余额不足");
            return  null;
        }
        //创建输入列表
        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
        float total = 0;

        for(Map.Entry<String, TransactionOutput> item: UTXOS.entrySet()){
            TransactionOutput UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if(total > value){
                break;
            }
        }
        Transaction newTransaction = new Transaction(publicKey, recipient, value, inputs);
        newTransaction.generateSignature(privateKey);
        for (TransactionInput input:inputs){
            UTXOS.remove(input.transactionOutputId);
        }
        return newTransaction;
    }
}