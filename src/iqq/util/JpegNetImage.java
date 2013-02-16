package iqq.util;

import java.awt.image.BufferedImage;

import java.io.FileOutputStream;

import java.io.IOException;

import javax.imageio.ImageIO;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unchecked")
public class JpegNetImage extends AbstractNetImage {

    public JpegNetImage(String _url) {

        super(_url);

    }

    public JpegNetImage() {
    }

    //JPEG编码
    protected void encode(FileOutputStream out, BufferedImage bufferedImage) {
        try {
            javax.imageio.ImageIO.write(bufferedImage, "jpeg", out);
        } catch (IOException ex) {
            Logger.getLogger(JpegNetImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
