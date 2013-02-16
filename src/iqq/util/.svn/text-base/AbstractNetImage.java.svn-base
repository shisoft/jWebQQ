package iqq.util;

import java.awt.Image;

import java.awt.image.BufferedImage;

import java.io.*;

import java.net.MalformedURLException;

import java.net.URL;

import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import iqq.service.HttpService;

@SuppressWarnings("unchecked")
public abstract class AbstractNetImage {

    private String url = null;
    private static int errorCount = 0;

    public AbstractNetImage(String url) {

        this.url = url;

    }

    public AbstractNetImage() {
    }

    public String getUrl() {

        return url;

    }

    public void setUrl(String url) {

        this.url = url;

    }

    /**
     *
     * 设立该抽象方法的意图是要使具体子类实现不同格式图片的编码方法(jpeg等)
     *
     *
     */
    protected abstract void encode(FileOutputStream out, BufferedImage bufferedImage);

    private void getImageImpl2(String newFilePath, int sizeReduceRank) {

        if (url == null || errorCount > 5) {
            return;
        }
        FileOutputStream out = null;
        try {

            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            conn.addRequestProperty("Cookie", HttpService.getCookie());
            conn.addRequestProperty("Connection", "Keep-Alive");
            conn.addRequestProperty("Accept-Language", "zh-cn");
            conn.addRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.addRequestProperty("Cache-Control", "no-cache");
            conn.addRequestProperty("Accept-Charset", "UTF-8;");
            conn.addRequestProperty("Referer", "http://web.qq.com");
            conn.addRequestProperty("Host", "d.web2.qq.com");
            conn.addRequestProperty(
                    "User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727)");
            try {
                if (conn.getInputStream() == null) {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            Image image = javax.imageio.ImageIO.read(conn.getInputStream());

            int width = image.getWidth(null) / sizeReduceRank;

            int height = image.getHeight(null) / sizeReduceRank;

            // 更改图片大小 sizeRank是原图的缩小的比例 若为2意思为将下载的文件保存为原理图片长宽的1/2

            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            bufferedImage.getGraphics().drawImage(image, 0, 0, width, height, null);

            out = new FileOutputStream(newFilePath);

            encode(out, bufferedImage);


        } catch (Exception e) {
            //e.printStackTrace();
            Log.println(e.getMessage());
            Log.println("Image download errorCount:" + errorCount);
            errorCount++;
            getImageImpl2(newFilePath, sizeReduceRank);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(AbstractNetImage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void getImageImpl(String newFilePath, int sizeReduceRank) {

        if (url == null || errorCount > 5) {
            return;
        }
        FileOutputStream out = null;
        InputStream is = null;
        try {

            HttpService hs = new HttpService(url, Method.GET);
            is = hs.getInputStream();

            if (is == null) {
                return;
            }

            Image image = javax.imageio.ImageIO.read(is);

            int width = image.getWidth(null) / sizeReduceRank;

            int height = image.getHeight(null) / sizeReduceRank;

            // 更改图片大小 sizeRank是原图的缩小的比例 若为2意思为将下载的文件保存为原理图片长宽的1/2

            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            bufferedImage.getGraphics().drawImage(image, 0, 0, width, height, null);

            out = new FileOutputStream(newFilePath);

            encode(out, bufferedImage);


        } catch (Exception e) {
            //e.printStackTrace();
            Log.println(e.getMessage());
            Log.println("Image download errorCount:" + errorCount);
            errorCount++;
            getImageImpl(newFilePath, sizeReduceRank);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(AbstractNetImage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * 取得原图片，缩小尺寸有参数sizeReduceRank决定
     *
     *
     */
    public final void getImage(String newFilePath, int sizeReduceRank) throws MalformedURLException, IOException, Exception {

        getImageImpl(newFilePath, sizeReduceRank);

    }

    /**
     *
     * 取得原图片，尺寸不变
     *
     *
     */
    public final void getImage(String newFilePath) throws MalformedURLException, IOException, Exception {
        getImageImpl(newFilePath, 1);

    }

    /**
     *
     * 直接指定网络图片的位置，和下载到本地文件系统的位置
     *
     *
     */
    public void getImageFromUrl(String url, String newFilePath) throws MalformedURLException, IOException, Exception {
        File f = new File(newFilePath);
        if (f.exists()) {
            return;
        }
        this.setUrl(url);

        this.getImage(newFilePath);

    }
}