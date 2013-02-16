/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.util;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author chenzhihui
 */
public class AudioPlayWave {

    private static AudioPlayWave audioUtil = null;
    private InputStream inputStream = null;
    private String fileName = "";
    private File file = null;
    public final static String AUDIO_ONLINE = "online.wav";
    public final static String AUDIO_MSG = "msg.wav";

    private AudioPlayWave() {
    }

    public static AudioPlayWave getInstance() {
        if (audioUtil == null) {
            audioUtil = new AudioPlayWave();
        }

        return audioUtil;
    }

    public static AudioPlayWave newInstance() {
        audioUtil = new AudioPlayWave();

        return audioUtil;
    }

    public void play(String internal) {

        String path = "/iqq/res/sound/" + internal;
        Log.println(path);
        InputStream in = this.getClass().getResourceAsStream(path);
        play(in);
//        URI uri = null;
//
//        try {
//            uri = this.getClass().getClassLoader().getResource(path).toURI();
//        } catch (URISyntaxException ex) {
//            Logger.getLogger(AudioPlayWave.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        file = new File(uri);
//        if (file.exists()) {
//            play(file);
//        }
    }

    public void playCoustom(String fileName) {
        if (fileName != null) {
            file = new File(fileName);
        }
        if (file.exists()) {
            play(file);
        }
    }

    public synchronized void play(File file) {
        try {
            inputStream = new FileInputStream(file);
            AudioPlayer.player.start(inputStream);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AudioPlayWave.class.getName()).log(Level.SEVERE, null, ex);
        }
        Log.println("play finish!");
    }

    public synchronized void play(InputStream inputStream) {
        AudioStream as = null;
        try {
            as = new AudioStream(inputStream);
        } catch (IOException ex) {
            Logger.getLogger(AudioPlayWave.class.getName()).log(Level.SEVERE, null, ex);
        }
        AudioPlayer.player.start(as);
        Log.println("play finish!");
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public static void main(String[] args) {
        AudioPlayWave.getInstance().play(AudioPlayWave.AUDIO_MSG);
    }
}
