package iqq.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class FileLockUtil {

    private static String fileName = "file.lock";
    private static String currentDirectory = QQEnvironment.getQQLoginDir();
    private static String fileSeparate = System.getProperty("file.separator");
    private static File f = null;
    private static RandomAccessFile raf = null;
    private static FileChannel fc = null;
    private static FileLock fl = null;
    private static FileWriter fr = null;

    public static boolean getFileLock(String account) {
        FileLockUtil.fileName = account + ".lock";
        boolean rtn = false;

        try {
            if (!currentDirectory.endsWith(fileSeparate)) {
                currentDirectory = currentDirectory + fileSeparate;
            }
            System.out.println(currentDirectory);
            f = new File(currentDirectory + fileName);
            raf = new RandomAccessFile(f, "rw");

            fr = new FileWriter(f);
            fr.write("singleton");
            if (fr != null) {
                fr.close();
                fr = null;
            }

            fc = raf.getChannel();
            fl = fc.tryLock();
            if (fl == null) {
                return false;
            }
            if (fl.isValid()) {
                f.deleteOnExit();
                return true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        return rtn;

    }

    public static void releaseFileLock() {

        try {
            if (fr != null) {
                fr.close();
            }
            if (fl != null) {
                fl.release();
            }
            if (fc != null) {
                fc.close();
            }
            if (raf != null) {
                raf.close();
            }
            if (f != null) {
                f.delete();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static int getFileLockCount() { 
        File f = new File(currentDirectory);
        if(f.isDirectory()) {
            return f.list().length;
        }
        return 0;
    }

    public static void main(String[] args) {
        FileLockUtil lockUtil = new FileLockUtil();
        if (lockUtil.getFileLock("1")) {
            System.out.println("get file lock.");
        }
        
        FileLockUtil lockUtil2 = new FileLockUtil();
        if (lockUtil2.getFileLock("2")) {
            System.out.println("get file lock2.");
        } else {
            System.out.println("can not get file lock2.");
        }
System.out.println(FileLockUtil.getFileLockCount());
        lockUtil.releaseFileLock();
        System.out.println("rel");
        // TODO Auto-generated catch block

        FileLockUtil lockUtil3 = new FileLockUtil();
        if (lockUtil3.getFileLock("1")) {
            System.out.println("get file lock2.");
        } else {
            System.out.println("can not get file lock2.");
        }

        lockUtil.releaseFileLock();
        System.out.println("rel");

        if (lockUtil3.getFileLock("1")) {
            System.out.println("get file lock2.");
        } else {
            System.out.println("can not get file lock2.");
        }

    }
}
