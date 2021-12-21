package ew.sr.x1c.quilt.meow.endpoint.key;

import ew.sr.x1c.quilt.meow.endpoint.constant.EndpointConstant;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RSAKeyGenerator {

    private static final Logger LOGGER = Logger.getLogger(RSAKeyGenerator.class.getName());

    public static KeyPair generateRSAKeyPair(int keyLength) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keyLength);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.log(Level.SEVERE, "產生金鑰時發生例外狀況", ex);
            return null;
        }
    }

    public static void saveKey(Key key, String path) {
        try {
            Files.write(Paths.get(path), key.getEncoded());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "儲存金鑰時發生例外狀況", ex);
        }
    }

    public static void generate() {
        LOGGER.log(Level.INFO, "RSA 金鑰產生器");
        LOGGER.log(Level.INFO, "長度 : {0} 位元", EndpointConstant.KEY_LENGTH);

        LOGGER.log(Level.INFO, "正在產生金鑰");
        KeyPair keyPair = generateRSAKeyPair(EndpointConstant.KEY_LENGTH);

        LOGGER.log(Level.INFO, "正在儲存金鑰");
        saveKey(keyPair.getPublic(), "publicKey.key");
        saveKey(keyPair.getPrivate(), "privateKey.key");
        LOGGER.log(Level.INFO, "處理完成");
    }

    public static void main(String[] args) {
        generate();
    }
}
