package ew.sr.x1c.quilt.meow.endpoint.util;

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import javax.activation.MimetypesFileTypeMap;
import javax.swing.ImageIcon;

public class ImageUtil {

    public static boolean isImage(String path) {
        File file = new File(path);
        String mimetype = new MimetypesFileTypeMap().getContentType(file);
        String type = mimetype.split("/")[0];
        return type.equals("image");
    }

    public static Dimension getScaleDimension(Dimension imageSize, Dimension bound) {
        int originWidth = imageSize.width;
        int originHeight = imageSize.height;
        int boundWidth = bound.width;
        int boundHeight = bound.height;
        int newWidth = originWidth;
        int newHeight = originHeight;
        if (originWidth > boundWidth) {
            newWidth = boundWidth;
            newHeight = (newWidth * originHeight) / originWidth;
        }
        if (newHeight > boundHeight) {
            newHeight = boundHeight;
            newWidth = (newHeight * originWidth) / originHeight;
        }
        return new Dimension(newWidth, newHeight);
    }

    public static ImageIcon resizeImage(ImageIcon origin, Dimension bound) {
        Dimension imageSize = new Dimension(origin.getIconWidth(), origin.getIconHeight());
        Dimension scale = getScaleDimension(imageSize, bound);
        Image ret = origin.getImage().getScaledInstance((int) scale.getWidth(), (int) scale.getHeight(), Image.SCALE_SMOOTH);
        return new ImageIcon(ret);
    }
}
