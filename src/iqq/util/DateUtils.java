/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.util;

import atg.taglib.json.util.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author chenzhihui
 */
public class DateUtils {
    public static Date parse(JSONObject jsonobj) throws Exception {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d = format.parse(jsonobj.getInt("year") + "-" + jsonobj.getInt("month")+ "-" + jsonobj.getInt("day"));
        return d;
    }
}
