package com.example.demochatapp.Util;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    private static final String TAG ="AESUtil" ;
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static PublicKey getPublicKey(String base64PublicKey){
        PublicKey publicKey = null;
        try{
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("DiffieHellman");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e(TAG, "getPublicKey: "+e.getMessage());
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            Log.e(TAG, "getPublicKey: "+e.getMessage());
        }
        return publicKey;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static PrivateKey getPrivateKey(String base64PrivateKey){
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("DiffieHellman");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e(TAG, "getPublicKey: "+e.getMessage());
        }
        try {
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            Log.e(TAG, "getPublicKey: "+e.getMessage());
        }
        return privateKey;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static byte[] getSharedSecretKey(String base64PrivateKey, String base64PublicKey) throws NoSuchAlgorithmException, InvalidKeyException {
        PrivateKey privateKey=getPrivateKey(base64PrivateKey);
        PublicKey publicKey=getPublicKey(base64PublicKey);

        KeyAgreement keyAgreement=KeyAgreement.getInstance("DH");
        keyAgreement.init(privateKey);
        keyAgreement.doPhase(publicKey,true);

        return keyAgreement.generateSecret();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static byte[] encrypt(String value, byte[] sharedSecretKey) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(sharedSecretKey);

            SecretKeySpec skeySpec = new SecretKeySpec(digest, "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

            return cipher.doFinal(value.getBytes());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decrypt(String encrypted, byte[] sharedSecretKey) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(sharedSecretKey);

            SecretKeySpec skeySpec = new SecretKeySpec(digest, "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted.getBytes()));
            return new String(original);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
