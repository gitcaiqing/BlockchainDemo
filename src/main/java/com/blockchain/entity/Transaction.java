package com.blockchain.entity;

import com.blockchain.Util.StringUtil;
import com.blockchain.controller.TestTransaction;

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
    public ArrayList<TransactionInput> inputs = new ArrayList<>();

    //输出列表
    public ArrayList<TransactionOutput> outputs = new ArrayList<>();

    //创建的交易数
    private static int sequence = 0;

    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
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
     * 生成交易签名
     * @param privateKey
     */
    public void generateSignature(PrivateKey privateKey){
        //要签名的数据
        String data = StringUtil.getStringFromKey(sender)+StringUtil.getStringFromKey(reciepient)+Float.toString(value);
        //私钥+数据 产生签名
        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    /**
     * 验证交易签名
     * @return
     */
    public boolean verifySignature(){
        //要签名的数据
        String data = StringUtil.getStringFromKey(sender)+StringUtil.getStringFromKey(reciepient)+Float.toString(value);
        //签名 + 公钥 + 数据 验证签名是否合法 ； sender = publicKey
        //返回true 签名合法， false 签名不合法
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

    /**
     * 产生交易
     * @return
     */
    public boolean processTransaction(){

        //验证签名
        if(verifySignature() == false){
            System.out.println("验证签名失败");
            return false;
        }

        //交易输入集合
        for(TransactionInput input:inputs){
            input.UTXO = TestTransaction.UTXOs.get(input.transactionOutputId);
        }

        //交易是否有效
        if(getInputsValue() < TestTransaction.minimumTransaction){
            System.out.println("金额不足：输入剩余金额："+getInputsValue());
            return false;
        }

        //剩余金额
        float leftOver = getInputsValue() - value;
        transactionId = calculateTransactionId();

        outputs.add(new TransactionOutput(reciepient, value, transactionId));
        outputs.add(new TransactionOutput(sender, leftOver, transactionId));

        //把输出增加到未使用的列表中
        for(TransactionOutput output:outputs){
            TestTransaction.UTXOs.put(output.id, output);
        }
        //把已经使用的交易输入从UTXO中移除
        for(TransactionInput input:inputs){
            if(input.UTXO == null){
                continue;
            }
            TestTransaction.UTXOs.remove(input.UTXO.id);
        }
        return true;

    }

    /**
     * 返回余额
     * @return
     */
    public float getInputsValue(){
        float total = 0;
        for(TransactionInput i:inputs){
            total += i.UTXO.value;
        }
        return total;
    }

    /**
     * 返回输出总和
     */
    public float getOutputsValue(){
        float total = 0;
        for(TransactionOutput output:outputs){
            total += output.value;
        }
        return total;
    }
}