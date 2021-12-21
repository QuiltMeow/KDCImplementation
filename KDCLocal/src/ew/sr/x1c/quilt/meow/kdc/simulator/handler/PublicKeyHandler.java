package ew.sr.x1c.quilt.meow.kdc.simulator.handler;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class PublicKeyHandler {

    public static byte[] RSAEncrypt(RSAPublicKey RSAPublicKey, byte[] plain) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, RSAPublicKey);
            return cipher.doFinal(plain);
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            return null;
        }
    }

    public static byte[] RSADecrypt(RSAPrivateKey RSAPrivateKey, byte[] encrypt) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, RSAPrivateKey);
            return cipher.doFinal(encrypt);
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            return null;
        }
    }

    public static PublicKey loadPublicKey(byte[] publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509EncodeKeySpec = new X509EncodedKeySpec(publicKey);
            return keyFactory.generatePublic(x509EncodeKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            return null;
        }
    }

    public static PrivateKey loadPrivateKey(byte[] privateKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec pkcs8EncodeKeySpec = new PKCS8EncodedKeySpec(privateKey);
            return keyFactory.generatePrivate(pkcs8EncodeKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            return null;
        }
    }

    public static byte[] RSASignature(RSAPrivateKey privateKey, byte[] data) {
        try {
            Signature signature = Signature.getInstance("SHA512withRSA");
            signature.initSign(privateKey);
            signature.update(data);
            return signature.sign();
        } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException ex) {
            return null;
        }
    }

    public static boolean RSASignatureVerify(PublicKey publicKey, byte[] data, byte[] signatureData) {
        try {
            Signature signature = Signature.getInstance("SHA512withRSA");
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(signatureData);
        } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException ex) {
            return false;
        }
    }
}
