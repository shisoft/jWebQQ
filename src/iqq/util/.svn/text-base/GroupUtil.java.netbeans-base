/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.util;

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chenzhihui
 */
public class GroupUtil {

    private static GroupUtil groupUtil = null;
    public static final String CONFIG_FILE_NAME = "group.json";// 系统配置文件名称
    private static JSONArray listCache = null;

    private GroupUtil() {
    }

    public static GroupUtil getInstance() {
        if (groupUtil == null) {
            groupUtil = new GroupUtil();
        }
        return groupUtil;
    }

    public boolean isDisabled(long uin) {
        try {
            if (listCache == null) {
                String loadResult = load();
                JSONArray array = null;
                if (loadResult == null || loadResult.equals("")) {
                    return false;
                } else {
                    array = new JSONArray(loadResult);
                    listCache = array;
                }
            }

            if (listCache == null || listCache.isEmpty()) {
                return false;
            } else {
                for (int i = 0; i < listCache.size(); i++) {
                    long id = listCache.getLong(i);
                    if (uin == id) {
                        Log.println("群禁了~~");
                        return true;
                    }
                }
            }

        } catch (JSONException ex) {
            Logger.getLogger(GroupUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public String load() {
        String jsonStr = this.readFile(QQEnvironment.getMemberDir() + CONFIG_FILE_NAME);
        Log.println(jsonStr);
        return jsonStr;
    }

    public void delete(long uin) {
        try {
            String loadResult = load();
            JSONArray array = null;
            if (loadResult == null || loadResult.equals("")) {
                return;
            } else {

                array = new JSONArray(loadResult);

            }

            if (array == null || array.isEmpty()) {
                return;
            } else {
                for (int i = 0; i < array.size(); i++) {
                    long id = array.getLong(i);
                    if (uin == id) {
                        array.remove(i);
                    }
                }
            }
            listCache = array;
            String groups = array.toString();
            newFile(QQEnvironment.getMemberDir() + CONFIG_FILE_NAME, groups);
        } catch (JSONException ex) {
            Logger.getLogger(GroupUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void add(long uin) {
        try {
            JSONArray array = null;
            String loadResult = load();
            if (loadResult == null || loadResult.equals("")) {
                array = new JSONArray();
            } else {
                array = new JSONArray(loadResult);
            }

            if (array.isEmpty()) {
                array.add(uin);
            } else {
                boolean isExist = false;
                for (int i = 0; i < array.size(); i++) {
                    long id = array.getLong(i);
                    if (uin == id) {
                        isExist = true;
                    }
                }
                if (!isExist) {
                    array.add(uin);
                }
            }
            listCache = array;
            String groups = array.toString();
            newFile(QQEnvironment.getMemberDir() + CONFIG_FILE_NAME, groups);

        } catch (JSONException ex) {
            Logger.getLogger(GroupUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 新建文件
     *
     * @param filePathAndName String 文件路径及名称 如c:/fqf.txt
     * @param fileContent String 文件内容
     * @return boolean
     */
    public void newFile(String filePathAndName, String fileContent) {
        FileWriter resultFile = null;
        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            if (!myFilePath.exists()) {
                myFilePath.createNewFile();
            }
            resultFile = new FileWriter(myFilePath);
            PrintWriter myFile = new PrintWriter(resultFile);
            String strContent = fileContent;
            myFile.println(strContent);


        } catch (Exception e) {
            System.out.println("新建目录操作出错");
            e.printStackTrace();

        } finally {
            try {
                resultFile.close();
            } catch (IOException ex) {
                Logger.getLogger(GroupUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public String readFile(String filePathAndName) {
        String fileContent = null;
        FileReader resultFile = null;
        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            if (!myFilePath.exists()) {
                myFilePath.createNewFile();
            }
            resultFile = new FileReader(myFilePath);
            char[] c = new char[1024];
            int size = resultFile.read(c);
            if (size > 0) {
                fileContent = new String(c, 0, size);
            }

        } catch (Exception e) {
            System.out.println("新建目录操作出错");
            e.printStackTrace();

        } finally {
            try {
                resultFile.close();
            } catch (IOException ex) {
                Logger.getLogger(GroupUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return fileContent;
    }

    public static void main(String[] args) {
        GroupUtil.getInstance().add(21);
        GroupUtil.getInstance().delete(1);
        Log.println(GroupUtil.getInstance().isDisabled(22));
    }
}
