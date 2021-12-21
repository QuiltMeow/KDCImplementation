package ew.sr.x1c.quilt.meow.endpoint.key;

import ew.sr.x1c.quilt.meow.endpoint.MainGUI;
import ew.sr.x1c.quilt.meow.endpoint.constant.EndpointConstant;
import ew.sr.x1c.quilt.meow.endpoint.handler.PublicKeyHandler;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.logging.Level;
import lombok.Getter;
import lombok.Setter;

public class KeyData {

    @Getter
    private final RSAPublicKey KDCPublicKey;

    @Getter
    private RSAPublicKey localPublicKey;

    @Getter
    private byte[] localPublicKeyByte;

    @Getter
    @Setter
    private RSAPrivateKey localPrivateKey;

    @Getter
    private static KeyData instance;

    public static KeyData getInstance() {
        if (instance == null) {
            instance = new KeyData();
        }
        return instance;
    }

    public void setLocalPublicKey(byte[] keyByte, RSAPublicKey key) {
        localPublicKeyByte = keyByte;
        localPublicKey = key;
    }

    public final byte[] loadResource(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[EndpointConstant.BUFFER_SIZE];
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
        KDCPublicKey = (RSAPublicKey) PublicKeyHandler.loadPublicKey(loadResource("/ew/sr/x1c/quilt/meow/endpoint/key/KDC_trust_public_key.key"));
    }
}
