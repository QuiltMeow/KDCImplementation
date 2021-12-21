package ew.sr.x1c.quilt.meow.kdc.plugin.handler;

import ew.sr.x1c.quilt.meow.kdc.constant.PluginConstant;
import ew.sr.x1c.quilt.meow.server.GeneralManager;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.logging.Level;
import lombok.Getter;

public class KDCKey {

    @Getter
    private final RSAPublicKey publicKey;

    @Getter
    private final RSAPrivateKey privateKey;

    @Getter
    private final byte[] publicKeyByte;

    @Getter
    private static KDCKey instance;

    public static KDCKey getInstance() {
        if (instance == null) {
            instance = new KDCKey();
        }
        return instance;
    }

    public final byte[] loadResource(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[PluginConstant.BUFFER_SIZE];
                int length;
                while ((length = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }
                return baos.toByteArray();
            }
        } catch (Exception ex) {
            GeneralManager.getInstance().getLogger().log(Level.SEVERE, "資源無法載入", ex);
            return null;
        }
    }

    private KDCKey() {
        publicKeyByte = loadResource("/ew/sr/x1c/quilt/meow/kdc/key/public_key.key");
        byte[] privateKeyByte = loadResource("/ew/sr/x1c/quilt/meow/kdc/key/private_key.key");
        publicKey = (RSAPublicKey) PublicKeyHandler.loadPublicKey(publicKeyByte);
        privateKey = (RSAPrivateKey) PublicKeyHandler.loadPrivateKey(privateKeyByte);
    }
}
