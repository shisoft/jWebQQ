package iqq.util;

import java.awt.image.BufferedImage;

import java.io.FileOutputStream;

import java.io.IOException;

import com.sun.image.codec.jpeg.ImageFormatException;

import com.sun.image.codec.jpeg.JPEGCodec;

import com.sun.image.codec.jpeg.JPEGImageEncoder;
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

        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        try {
            encoder.encode(bufferedImage);
        } catch (IOException ex) {
            Logger.getLogger(JpegNetImage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ImageFormatException ex) {
            Logger.getLogger(JpegNetImage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
