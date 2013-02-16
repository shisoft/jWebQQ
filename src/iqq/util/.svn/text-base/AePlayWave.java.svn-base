package iqq.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

//播放声音的类
public class AePlayWave extends Thread { // 

    public final static String AUDIO_ONLINE = "online.wav";
    public final static String AUDIO_MSG = "msg.wav";
    private String filename;
    InputStream inputStream = null;

    public AePlayWave(String wavfile) {
        filename = wavfile;
    }

    public synchronized void paly() {
        //File soundFile = new File(filename);
        inputStream = AePlayWave.class.getClass().getResourceAsStream("/iqq/res/sound/" + filename);
        InputStream bufferedIn = new BufferedInputStream(inputStream);
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
        } catch (Exception e1) {
            e1.printStackTrace();
            try {
                if (audioInputStream != null) {
                    audioInputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (bufferedIn != null) {
                    bufferedIn.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(AePlayWave.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }

        AudioFormat format = audioInputStream.getFormat();
        SourceDataLine auline = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        try {
            auline = (SourceDataLine) AudioSystem.getLine(info);
            if (auline.getBufferSize() == 0) {
                if (audioInputStream != null) {
                    audioInputStream.close();
                }
                if (bufferedIn != null) {
                    bufferedIn.close();
                }
                if (auline != null) {
                    auline.drain();
                    auline.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                return;
            }
            auline.open(format);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (audioInputStream != null) {
                    audioInputStream.close();
                }
                if (bufferedIn != null) {
                    bufferedIn.close();
                }
                if (auline != null) {
                    auline.drain();
                    auline.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(AePlayWave.class.getName()).log(Level.SEVERE, null, ex);
            }
            auline.drain();
            auline.close();
            return;
        }

        auline.start();
        int nBytesRead = 0;
        //这是缓冲
        byte[] abData = new byte[128];

        try {
            while (nBytesRead != -1) {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0) {
                    auline.write(abData, 0, nBytesRead);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            try {
                if (audioInputStream != null) {
                    audioInputStream.close();
                }
                if (bufferedIn != null) {
                    bufferedIn.close();
                }
                if (auline != null) {
                    auline.drain();
                    auline.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(AePlayWave.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void run() {
        paly();
    }

    public synchronized static void play(String wavfile) {
        new AePlayWave(wavfile).start();
    }

    public static void main(String[] args) {
        //声音提示
        AePlayWave.play(AePlayWave.AUDIO_MSG);
    }
}
