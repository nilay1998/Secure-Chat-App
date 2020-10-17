package com.example.demochatapp.Util;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAUtil {

    private static final String TAG ="RSAUtil" ;
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static PublicKey getPublicKey(String base64PublicKey){
        PublicKey publicKey = null;
        try{
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
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
            keyFactory = KeyFactory.getInstance("RSA");
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
    public static byte[] encrypt(String data, String base64PublicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(base64PublicKey));
        return cipher.doFinal(data.getBytes());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decrypt(String data, String base64PrivateKey) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return decrypt(Base64.getDecoder().decode(data.getBytes()), base64PrivateKey);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String decrypt(byte[] data, String base64PrivateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(base64PrivateKey));
        return new String(cipher.doFinal(data));
    }
}
