package ew.sr.x1c.quilt.meow.kdc.plugin.handler;

import ew.sr.x1c.quilt.meow.kdc.constant.PluginConstant;
import java.math.BigInteger;
import java.security.MessageDigest;

public class SHAHandler {

    public static String SHA512(String input) {
        String ret = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update(input.getBytes(PluginConstant.DEFAULT_CHARSET));
            ret = String.format("%0128x", new BigInteger(1, digest.digest()));
        } catch (Exception ex) {
        }
        return ret;
    }
}
