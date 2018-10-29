package telvoterminal.telvo.com.terminal.utility;

import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by Invariant on 11/20/17.
 */

public class RSACrypt {

    private Cipher cipher;
    private String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzhRIdAtPkgqYZJ0mRy1d\n" +
            "hrj4hZFqX5PYmXNaGMICz4I8BZs417OZygwiowkIIuY49lcyfReNLvX73WuSdJA2\n" +
            "llywgYWk6oj2YFaEwOKwMPLnYIpgi73wnRc8oGI694jsKVY4FZFvrDN2VrLDI4nX\n" +
            "xSxwINyv3L1sT+jXVORL90KTa9fu1ct7fguPFlbQEz7I3lGb139lGP1UFELTmhIZ\n" +
            "26pfArrJ+2Z1Yba88mW5BxFAloO9RBmuxUv+1unEWFq/NbdzgLXfwF6m/q2PNOdQ\n" +
            "WCwWEtdIOFO9WoyIT+H6g6fioyK/cvBS2n+THnvPVKrk8ae7YvXJ+x67NWH7H9wH\n" +
            "vQIDAQAB";

    private String Modulus = "CE1448740B4F920A98649D26472D5D86B8F885916A5F93D899735A18C202CF823C059B38D7B399CA0C22A3090822E638F657327D178D2EF5FBDD6B92749036965CB08185A4EA88F6605684C0E2B030F2E7608A608BBDF09D173CA0623AF788EC29563815916FAC337656B2C32389D7C52C7020DCAFDCBD6C4FE8D754E44BF742936BD7EED5CB7B7E0B8F1656D0133EC8DE519BD77F6518FD541442D39A1219DBAA5F02BAC9FB667561B6BCF265B90711409683BD4419AEC54BFED6E9C4585ABF35B77380B5DFC05EA6FEAD8F34E750582C1612D7483853BD5A8C884FE1FA83A7E2A322BF72F052DA7F931E7BCF54AAE4F1A7BB62F5C9FB1EBB3561FB1FDC07BD";


    public String encryptRSAToString(String clearText) {
        String encryptedBase64 = "";
        try {
//            KeyFactory keyFac = KeyFactory.getInstance("RSA");
//            KeySpec keySpec = new X509EncodedKeySpec(Base64.decode(publicKey.trim().getBytes(), Base64.DEFAULT));
//            Key key = keyFac.generatePublic(keySpec);

//            byte[] keyBytes = Base64.decode(publicKey.getBytes("utf-8"),Base64.DEFAULT);
//            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//            PublicKey key = keyFactory.generatePublic(spec);

            BigInteger modulus = new BigInteger(Modulus, 16);
            BigInteger pubExp = new BigInteger("010001", 16);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            byte[] keyBytes = Base64.decode(publicKey.getBytes("utf-8"),Base64.DEFAULT);
            RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(modulus, pubExp);
//            RSAPublicKey key = (RSAPublicKey)keyFactory.generatePublic(KeySpec);
            RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);
            //RSAPublicKey privatekey = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);


            String pp = bytesToString(key.getEncoded());
            String al = key.getAlgorithm();
            String ad = key.getFormat();

            Log.i("Key info",pp+" -- "+al+" -- "+ad);

            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encryptedBytes = cipher.doFinal(clearText.getBytes("UTF-8"));
            encryptedBase64 = new String(Base64.encode(encryptedBytes, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptedBase64;
    }

//    KeyPairGenerator kpg;
//    KeyPair kp;
//    PublicKey publicKey;
//    PrivateKey privateKey;
//    byte[] encryptedBytes, decryptedBytes;
//    Cipher cipher, cipher1;
//    String encrypted, decrypted;

//    public String Encrypt (String plain) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
//    {
//        kpg = KeyPairGenerator.getInstance("RSA");
//        kpg.initialize(1024);
//        kp = kpg.genKeyPair();
//        publicKey = kp.getPublic();
//        privateKey = kp.getPrivate();
//       String key = bytesToString(publicKey.getEncoded());
//        cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//        encryptedBytes = cipher.doFinal(plain.getBytes());
//
//        encrypted = bytesToString(encryptedBytes);
//        return encrypted;
//
//    }

    public  String bytesToString(byte[] b) {
        byte[] b2 = new byte[b.length + 1];
        b2[0] = 1;
        System.arraycopy(b, 0, b2, 1, b.length);
        return new BigInteger(b2).toString(36);
    }

}
