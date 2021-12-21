package ew.sr.x1c.quilt.meow.kdc.simulator;

import ew.sr.x1c.quilt.meow.kdc.simulator.constant.Constant;
import ew.sr.x1c.quilt.meow.kdc.simulator.handler.PublicKeyHandler;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.logging.Level;
import lombok.Getter;

public class KeyData {

    @Getter
    private final RSAPublicKey AlicePublicKey;

    @Getter
    private final RSAPrivateKey AlicePrivateKey;

    @Getter
    private final RSAPublicKey BobPublicKey;

    @Getter
    private final RSAPrivateKey BobPrivateKey;

    @Getter
    private final RSAPublicKey KDCPublicKey;

    @Getter
    private final RSAPrivateKey KDCPrivateKey;

    @Getter
    private final byte[] AlicePublicKeyByte;

    @Getter
    private final byte[] AlicePrivateKeyByte;

    @Getter
    private final byte[] BobPublicKeyByte;

    @Getter
    private final byte[] BobPrivateKeyByte;

    @Getter
    private final byte[] KDCPublicKeyByte;

    @Getter
    private final byte[] KDCPrivateKeyByte;

    @Getter
    private static KeyData instance;

    public static KeyData getInstance() {
        if (instance == null) {
            instance = new KeyData();
        }
        return instance;
    }

    public final byte[] loadResource(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[Constant.BUFFER_SIZE];
                int length;
                while ((length = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }
                return baos.toByteArray();
            }
        } catch (Exception ex) {
            MainGUI.getInstance().getLogger().log(Level.SEVERE, "資源無法載入", ex);
            return null;
        }
    }

    private KeyData() {
        AlicePublicKeyByte = loadResource("/ew/sr/x1c/quilt/meow/kdc/simulator/key/Alice_public_key.key");
        AlicePrivateKeyByte = loadResource("/ew/sr/x1c/quilt/meow/kdc/simulator/key/Alice_private_key.key");
        AlicePublicKey = (RSAPublicKey) PublicKeyHandler.loadPublicKey(AlicePublicKeyByte);
        AlicePrivateKey = (RSAPrivateKey) PublicKeyHandler.loadPrivateKey(AlicePrivateKeyByte);

        BobPublicKeyByte = loadResource("/ew/sr/x1c/quilt/meow/kdc/simulator/key/Bob_public_key.key");
        BobPrivateKeyByte = loadResource("/ew/sr/x1c/quilt/meow/kdc/simulator/key/Bob_private_key.key");
        BobPublicKey = (RSAPublicKey) PublicKeyHandler.loadPublicKey(BobPublicKeyByte);
        BobPrivateKey = (RSAPrivateKey) PublicKeyHandler.loadPrivateKey(BobPrivateKeyByte);

        KDCPublicKeyByte = loadResource("/ew/sr/x1c/quilt/meow/kdc/simulator/key/KDC_public_key.key");
        KDCPrivateKeyByte = loadResource("/ew/sr/x1c/quilt/meow/kdc/simulator/key/KDC_private_key.key");
        KDCPublicKey = (RSAPublicKey) PublicKeyHandler.loadPublicKey(KDCPublicKeyByte);
        KDCPrivateKey = (RSAPrivateKey) PublicKeyHandler.loadPrivateKey(KDCPrivateKeyByte);
    }
}
