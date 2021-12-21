package ew.sr.x1c.quilt.meow.kdc.plugin.handler;

import ew.sr.x1c.quilt.meow.kdc.constant.PluginConstant;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

public class AESCrypto {

    public static byte[] CBCEncrypt(IvParameterSpec iv, byte[] message) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, PluginConstant.AES_KEY, iv);
            return cipher.doFinal(message);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            return null;
        }
    }

    public static byte[] CBCDecrypt(IvParameterSpec iv, byte[] encrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, PluginConstant.AES_KEY, iv);
            return cipher.doFinal(encrypt);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            return null;
        }
    }
}
