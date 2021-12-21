package ew.sr.x1c.quilt.meow.endpoint.util;

import java.nio.file.Paths;

public class PathUtil {

    private static final String EMPTY_STRING = "";

    public static String cleanPath(String path) {
        String ret = path.replaceAll("\\\\", "/").replaceAll("/", EMPTY_STRING);
        while (ret.length() > 0 && ret.charAt(0) == '.') {
            ret = ret.substring(1);
        }

        while (ret.length() > 0 && ret.charAt(ret.length() - 1) == '.') {
            ret = ret.substring(0, ret.length() - 1);
        }
        return ret;
    }

    public static boolean isValidPath(String path) {
        try {
            Paths.get(path);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
