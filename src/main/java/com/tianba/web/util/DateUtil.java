package com.tianba.web.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 描述:
 * 时间工具类
 *
 * @author yangtao
 * @create 2018-09-22 19:35
 */
@Slf4j
public class DateUtil {

    public final static String yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public static final String yyyy_MM_dd_HH_mm_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public final static String yyyy__MM__dd = "yyyy/MM/dd";
    public final static String yyyyMMdd = "yyyyMMdd";
    public final static String HH_mm_ss = "HH:mm:ss";

    public static void main(String[] args) {
        String dateStr = getDateStrByTimeStamp(1493848434, 10, yyyy_MM_dd);
        System.out.println(dateStr);
    }

    public static Date getDateAddDays(Date date, int days){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static String getDateStrByFormat(Date date, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String getStringByDate(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    /**
     * 取得指定格式时间戳的时间字符串
     * @param timeStamp 时间戳
     * @param length 时间戳长度 10位或13位
     * @param format 时间格式
     */
    public static String getDateStrByTimeStamp(long timeStamp, long length, String format) {
        long time;
        if (length == 10) {
            time = timeStamp * 1000;
        } else if (length == 13) {
            time = timeStamp;
        } else {
            time = System.currentTimeMillis();
        }
        return new SimpleDateFormat(format).format(new Date(time));
    }

    /**
     * String时间转为时间戳
     * @param dateStr
     * @param formats
     * @return
     */
    public static Date getDateByString(String dateStr, String formats){
        SimpleDateFormat format = new SimpleDateFormat(formats);
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
