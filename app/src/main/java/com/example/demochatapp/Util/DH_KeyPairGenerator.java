package com.example.demochatapp.Util;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.spec.DHParameterSpec;

public class DH_KeyPairGenerator {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public DH_KeyPairGenerator() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DiffieHellman");
        DHParameterSpec dhparams = ike2048();
        keyGen.initialize(dhparams);
        KeyPair pair = keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
        Log.e("Encoding", "Private Key: "+ privateKey.getFormat());
        Log.e("Encoding", "Public Key: "+ publicKey.getFormat());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getPrivateKey_AES() {
        return (Base64.getEncoder().encodeToString(privateKey.getEncoded()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getPublicKey_AES() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public DHParameterSpec ike2048() {
        final BigInteger p =
                new BigInteger(
                        "ffffffffffffffffc90fdaa22168c234c4c6628b80dc1cd129024e088a67cc74"
                                + "020bbea63b139b22514a08798e3404ddef9519b3cd3a431b302b0a6df25f1437"
                                + "4fe1356d6d51c245e485b576625e7ec6f44c42e9a637ed6b0bff5cb6f406b7ed"
                                + "ee386bfb5a899fa5ae9f24117c4b1fe649286651ece45b3dc2007cb8a163bf05"
                                + "98da48361c55d39a69163fa8fd24cf5f83655d23dca3ad961c62f356208552bb"
                                + "9ed529077096966d670c354e4abc9804f1746c08ca18217c32905e462e36ce3b"
                                + "e39e772c180e86039b2783a2ec07a28fb5c55df06f4c52c9de2bcbf695581718"
                                + "3995497cea956ae515d2261898fa051015728e5a8aacaa68ffffffffffffffff",
                        16);
        final BigInteger g = new BigInteger("2");
        return new DHParameterSpec(p, g);
    }
}
