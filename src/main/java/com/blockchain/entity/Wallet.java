package com.blockchain.entity;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

/**
 * 钱包类， 包含公钥和私钥
 */
public class Wallet {

    public PrivateKey privateKey;

    public PublicKey publicKey;

    public Wallet() {
        generateKeyPair();
    }

    /**
     * 生成公钥和私钥
     */
    public void generateKeyPair(){
        try {
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
}