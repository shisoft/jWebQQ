package iqq.util;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 类说明
 * ============================================================================
 * 版权所有 2012 WootBest.COM，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.wootbest.com
 * ----------------------------------------------------------------------------
 *
 * @Author: ChenZhiHui
 * ============================================================================
 */
public class PersistenceUtils {

    public static final String CONFIG_FILE_NAME = "login.json";// 系统配置文件名称
    public static PersistenceUtils persistenceUtils = null;

    private PersistenceUtils() {
    }

    public static PersistenceUtils getInstance() {
        if (persistenceUtils == null) {
            persistenceUtils = new PersistenceUtils();
        }
        return persistenceUtils;
    }

    public String load() {
        String jsonStr = this.readFile(QQEnvironment.getConfigPath() + CONFIG_FILE_NAME);
        Log.println(jsonStr);
        
        return jsonStr;
    }
    
    public JSONObject loadMemberList() {
        JSONObject obj = null;
        try {
            String str = load();
            if(str == null) {
                return null;
            }
            obj = new JSONObject(str);
        } catch (JSONException ex) {
            Logger.getLogger(PersistenceUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj;
    }

    public void update(Map map) {
        try {
            //String jsonStr = "{\"users\":[{\"account\":\"917362009\",\"pwd\":\"chen01234\",\"rem_pwd\":true,\"login_status\":10}]}";
            JSONObject obj = null;
            String loadResult = load();
            if(loadResult == null || loadResult.equals("")) {
                obj = new JSONObject();
            } else {
                obj = new JSONObject(loadResult);
            }
            
            String key = map.get("account").toString();
            if(!obj.isNull(key)) {
                obj.remove(key);
                obj.put(key, map.get("password")); 
            } else {
               obj.put(key, map.get("password")); 
            }
            
            String users = obj.toString();
            
            newFile(QQEnvironment.getConfigPath() + CONFIG_FILE_NAME, users);
            
        } catch (JSONException ex) {
            Logger.getLogger(PersistenceUtils.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(PersistenceUtils.class.getName()).log(Level.SEVERE, null, ex);
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
            if(size > 0) {
                fileContent = new String(c, 0, size);
            }

        } catch (Exception e) {
            System.out.println("新建目录操作出错");
            e.printStackTrace();

        } finally {
            try {
                resultFile.close();
            } catch (IOException ex) {
                Logger.getLogger(PersistenceUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return fileContent;
    }

    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("account", "22");
        map.put("password", "22");
        PersistenceUtils.getInstance().update(map);
        PersistenceUtils.getInstance().load();
        System.out.println(PersistenceUtils.getInstance().loadMemberList());
        
//        List<Map> memberList = null;
//        memberList = PersistenceUtils.getInstance().loadMemberList();
//        if (memberList != null) {
//            for (Map m : memberList) {
//               System.out.println(m.get("password"));
//            }
//        }
    }
}
