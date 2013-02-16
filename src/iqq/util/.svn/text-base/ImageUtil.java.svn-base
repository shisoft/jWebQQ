package iqq.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;


import org.apache.commons.io.FileUtils;
import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.imageio.plugins.bmp.BMPImageReader;
import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.jpeg.JPEGImageReader;
import com.sun.imageio.plugins.png.PNGImageReader;
import java.io.InputStream;

/**
 * Util - 图片 http://www.wootbest.com
 *
 * Copyright (c) 2012 WootBest.COM All Rights Reserved. License:
 * http://www.wootbest.com/license
 *
 * Revision: 3.0 Date: 2012-01
 */
public class ImageUtil {

    // 水印位置(无、左上、右上、居中、左下、右下)
    public enum WatermarkPosition {

        no, topLeft, topRight, center, bottomLeft, bottomRight
    }

    /**
     * 处理类型(JDK、GraphicsMagick、ImageMagick)
     */
    private enum Type {

        jdk, graphicsMagick, imageMagick
    }
    private static Type type;// 处理类型
    private static String graphicsMagickPath;// GraphicsMagick程序路径
    private static String imageMagickPath;// ImageMagick程序路径
    private static final Color BACKGROUND_COLOR = Color.white;// 背景颜色
    private static final int DEST_QUALITY = 88;// 目标图片品质(取值范围: 0 - 100)
    private static final String JPEG_FORMAT_NAME = "jpg";// JPEG文件格式名称
    private static final String GIF_FORMAT_NAME = "gif";// GIF文件格式名称
    private static final String BMP_FORMAT_NAME = "bmp";// BMP文件格式名称
    private static final String PNG_FORMAT_NAME = "png";// PNG文件格式名称

    static {
        if (type == null) {
            try {
                if (graphicsMagickPath == null) {
                    String osName = System.getProperty("os.name").toLowerCase();
                    if (osName.indexOf("windows") >= 0) {
                        String pathVariable = System.getenv("Path");
                        if (pathVariable != null) {
                            String[] paths = pathVariable.split(";");
                            for (String path : paths) {
                                File gmFile = new File(path.trim() + "/gm.exe");
                                File gmdisplayFile = new File(path.trim() + "/gmdisplay.exe");
                                if (gmFile.exists() && gmdisplayFile.exists()) {
                                    graphicsMagickPath = path.trim();
                                    break;
                                }
                            }
                        }
                    }
                }
                IMOperation operation = new IMOperation();
                operation.version();
                IdentifyCmd identifyCmd = new IdentifyCmd(true);
                if (graphicsMagickPath != null) {
                    identifyCmd.setSearchPath(graphicsMagickPath);
                }
                identifyCmd.run(operation);
                type = Type.graphicsMagick;
            } catch (Throwable e1) {
                try {
                    if (imageMagickPath == null) {
                        String osName = System.getProperty("os.name").toLowerCase();
                        if (osName.indexOf("windows") >= 0) {
                            String pathVariable = System.getenv("Path");
                            if (pathVariable != null) {
                                String[] paths = pathVariable.split(";");
                                for (String path : paths) {
                                    File convertFile = new File(path.trim() + "/convert.exe");
                                    File compositeFile = new File(path.trim() + "/composite.exe");
                                    if (convertFile.exists() && compositeFile.exists()) {
                                        imageMagickPath = path.trim();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    IMOperation operation = new IMOperation();
                    operation.version();
                    IdentifyCmd identifyCmd = new IdentifyCmd(false);
                    identifyCmd.run(operation);
                    if (imageMagickPath != null) {
                        identifyCmd.setSearchPath(imageMagickPath);
                    }
                    type = Type.imageMagick;
                } catch (Throwable e2) {
                    type = Type.jdk;
                }
            }
        }
    }

    /**
     * 等比例图片缩放
     *
     * @param srcFile 源文件
     * @param destFile 目标文件
     * @param destWidth 目标宽度
     * @param destHeight 目标高度
     */
    public static void zoom(File srcFile, File destFile, int destWidth, int destHeight) {
        if (type == Type.jdk) {
            try {
                BufferedImage srcBufferedImage = ImageIO.read(srcFile);
                int srcWidth = srcBufferedImage.getWidth();
                int srcHeight = srcBufferedImage.getHeight();
                int width = destWidth;
                int height = destHeight;
                if (srcHeight >= srcWidth) {
                    width = (int) Math.round(((destHeight * 1.0 / srcHeight) * srcWidth));
                } else {
                    height = (int) Math.round(((destWidth * 1.0 / srcWidth) * srcHeight));
                }
                BufferedImage destBufferedImage = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics2D = destBufferedImage.createGraphics();
                graphics2D.setBackground(BACKGROUND_COLOR);
                graphics2D.clearRect(0, 0, destWidth, destHeight);
                graphics2D.drawImage(srcBufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), (destWidth / 2) - (width / 2), (destHeight / 2) - (height / 2), null);
                graphics2D.dispose();

                FileOutputStream out = new FileOutputStream(destFile);
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(destBufferedImage);
                param.setQuality((float) DEST_QUALITY / 100, false);
                encoder.setJPEGEncodeParam(param);
                encoder.encode(destBufferedImage);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            IMOperation operation = new IMOperation();
            operation.thumbnail(destWidth, destHeight);
            operation.gravity("center");
            operation.background(toHexEncoding(BACKGROUND_COLOR));
            operation.extent(destWidth, destHeight);
            operation.quality((double) DEST_QUALITY);
            operation.addImage(srcFile.getPath());
            operation.addImage(destFile.getPath());
            if (type == Type.graphicsMagick) {
                ConvertCmd convertCmd = new ConvertCmd(true);
                if (graphicsMagickPath != null) {
                    convertCmd.setSearchPath(graphicsMagickPath);
                }
                try {
                    convertCmd.run(operation);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IM4JavaException e) {
                    e.printStackTrace();
                }
            } else {
                ConvertCmd convertCmd = new ConvertCmd(false);
                if (imageMagickPath != null) {
                    convertCmd.setSearchPath(imageMagickPath);
                }
                try {
                    convertCmd.run(operation);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IM4JavaException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 添加水印
     *
     * @param srcFile 源文件
     * @param destFile 目标文件
     * @param watermarkFile 水印文件
     * @param watermarkPosition 水印位置
     * @param alpha 水印透明度
     */
    public synchronized static void addWatermark(File srcFile, File destFile, InputStream watermarkFile, WatermarkPosition watermarkPosition, int alpha) {
        //watermarkFile == null || !watermarkFile.exists() || 
        if (!srcFile.exists() || watermarkFile == null || watermarkPosition == null || watermarkPosition == WatermarkPosition.no) {
            Log.println("addWatermark null");
            return;
        }
        if (type == Type.jdk) {
            try {
                BufferedImage srcBufferedImage = ImageIO.read(srcFile);
                if(srcBufferedImage == null) {
                    return ;
                }
                int srcWidth = srcBufferedImage.getWidth();
                int srcHeight = srcBufferedImage.getHeight();
                BufferedImage destBufferedImage = new BufferedImage(srcWidth, srcHeight, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics2D = destBufferedImage.createGraphics();
                graphics2D.setBackground(BACKGROUND_COLOR);
                graphics2D.clearRect(0, 0, srcWidth, srcHeight);
                graphics2D.drawImage(srcBufferedImage, 0, 0, null);
                graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha / 100.0F));

                BufferedImage watermarkBufferedImage = ImageIO.read(watermarkFile);
                int watermarkImageWidth = watermarkBufferedImage.getWidth();
                int watermarkImageHeight = watermarkBufferedImage.getHeight();
                int x = srcWidth - watermarkImageWidth;
                int y = srcHeight - watermarkImageHeight;
                if (watermarkPosition == WatermarkPosition.topLeft) {
                    x = 0;
                    y = 0;
                } else if (watermarkPosition == WatermarkPosition.topRight) {
                    x = srcWidth - watermarkImageWidth;
                    y = 0;
                } else if (watermarkPosition == WatermarkPosition.center) {
                    x = (srcWidth - watermarkImageWidth) / 2;
                    y = (srcHeight - watermarkImageHeight) / 2;
                } else if (watermarkPosition == WatermarkPosition.bottomLeft) {
                    x = 0;
                    y = srcHeight - watermarkImageHeight;
                } else if (watermarkPosition == WatermarkPosition.bottomRight) {
                    x = srcWidth - watermarkImageWidth;
                    y = srcHeight - watermarkImageHeight;
                }
                graphics2D.drawImage(watermarkBufferedImage, x, y, watermarkImageWidth, watermarkImageHeight, null);
                graphics2D.dispose();

                FileOutputStream out = new FileOutputStream(destFile);
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(destBufferedImage);
                param.setQuality((float) DEST_QUALITY / 100, false);
                encoder.setJPEGEncodeParam(param);
                encoder.encode(destBufferedImage);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String gravity = "SouthEast";
            if (watermarkPosition == WatermarkPosition.topLeft) {
                gravity = "NorthWest";
            } else if (watermarkPosition == WatermarkPosition.topRight) {
                gravity = "NorthEast";
            } else if (watermarkPosition == WatermarkPosition.center) {
                gravity = "Center";
            } else if (watermarkPosition == WatermarkPosition.bottomLeft) {
                gravity = "SouthWest";
            } else if (watermarkPosition == WatermarkPosition.bottomRight) {
                gravity = "SouthEast";
            }
            IMOperation operation = new IMOperation();
            operation.gravity(gravity);
            operation.dissolve(alpha);
            operation.quality((double) DEST_QUALITY);
            //operation.addImage(watermarkFile);
            operation.addImage(srcFile.getPath());
            operation.addImage(destFile.getPath());
            if (type == Type.graphicsMagick) {
                CompositeCmd compositeCmd = new CompositeCmd(true);
                if (graphicsMagickPath != null) {
                    compositeCmd.setSearchPath(graphicsMagickPath);
                }
                try {
                    compositeCmd.run(operation);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IM4JavaException e) {
                    e.printStackTrace();
                }
            } else {
                CompositeCmd compositeCmd = new CompositeCmd(false);
                if (imageMagickPath != null) {
                    compositeCmd.setSearchPath(imageMagickPath);
                }
                try {
                    compositeCmd.run(operation);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IM4JavaException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 转换颜色为十六进制代码
     *
     * @param color 颜色
     */
    private static String toHexEncoding(Color color) {
        String R, G, B;
        StringBuffer stringBuffer = new StringBuffer();
        R = Integer.toHexString(color.getRed());
        G = Integer.toHexString(color.getGreen());
        B = Integer.toHexString(color.getBlue());
        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;
        stringBuffer.append("#");
        stringBuffer.append(R);
        stringBuffer.append(G);
        stringBuffer.append(B);
        return stringBuffer.toString();
    }

    /**
     * 获取图片文件格式
     *
     * @param imageFile 图片文件
     * @return 图片文件格式
     */
    public static String getFormatName(File imageFile) {
        if (imageFile == null || imageFile.length() == 0) {
            return null;
        }
        try {
            String formatName = null;
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(imageFile);
            Iterator<ImageReader> iterator = ImageIO.getImageReaders(imageInputStream);
            if (!iterator.hasNext()) {
                return null;
            }
            ImageReader imageReader = iterator.next();
            if (imageReader instanceof JPEGImageReader) {
                formatName = JPEG_FORMAT_NAME;
            } else if (imageReader instanceof GIFImageReader) {
                formatName = GIF_FORMAT_NAME;
            } else if (imageReader instanceof BMPImageReader) {
                formatName = BMP_FORMAT_NAME;
            } else if (imageReader instanceof PNGImageReader) {
                formatName = PNG_FORMAT_NAME;
            }
            imageInputStream.close();
            return formatName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 复制图片文件至图片目录
     *
     * @param imageFile 图片文件
     * @return 图片文件路径
     */
    public static String copyImageFile(String path, File imageFile) {
        if (imageFile == null) {
            return null;
        }
        String formatName = getFormatName(imageFile);
        if (formatName == null) {
            throw new IllegalArgumentException("imageFile format error!");
        }
        String destImagePath = path + "." + formatName;
        File destImageFile = new File(path + destImagePath);
        File destImageParentFile = destImageFile.getParentFile();
        if (!destImageParentFile.isDirectory()) {
            destImageParentFile.mkdirs();
        }
        try {
            FileUtils.copyFile(imageFile, destImageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destImagePath;
    }
}