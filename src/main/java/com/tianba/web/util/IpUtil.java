/*
 * @(#)IpUtil.java
 *
 * Copyright @ Hangzhou Xdstock Internet Tech. Co., Ltd.
 */
package com.tianba.web.util;

import java.util.Random;

/**
 * 生成IP的工具类。
 * 
 * @author yangtao 2018/09/28 新建
 */
public class IpUtil {

    /** 
     * 随机生成国内IP地址 
     * 
     * @return ip地址(x.x.x.x)
     */  
    public static String getRandomIp() {

        // ip池  
        int[][] range = { 
                { 607649792, 608174079 },// 36.56.0.0-36.63.255.255  
                { 1038614528, 1039007743 },// 61.232.0.0-61.237.255.255  
                { 1783627776, 1784676351 },// 106.80.0.0-106.95.255.255  
                { 2035023872, 2035154943 },// 121.76.0.0-121.77.255.255  
                { 2078801920, 2079064063 },// 123.232.0.0-123.235.255.255  
                { -1950089216, -1948778497 },// 139.196.0.0-139.215.255.255  
                { -1425539072, -1425014785 },// 171.8.0.0-171.15.255.255  
                { -1236271104, -1235419137 },// 182.80.0.0-182.92.255.255  
                { -770113536, -768606209 },// 210.25.0.0-210.47.255.255  
                { -569376768, -564133889 }, // 222.16.0.0-222.95.255.255  
        };  
 
        Random rdint = new Random();
        int index = rdint.nextInt(10);
        return numToIp(range[index][0] + new Random().nextInt(range[index][1] - range[index][0]));

    }

    /** 
     * 将十进制转换成ip地址。
     * 
     * @param ip 十进制ip
     * 
     * @return IP地址(x.x.x.x)
     */  
    private static String numToIp(int ip) {  

        int[] b = new int[4];  
        String ipAddress;
  
        b[0] = (ip >> 24) & 0xff;
        b[1] = (ip >> 16) & 0xff;
        b[2] = (ip >> 8) & 0xff;
        b[3] = ip & 0xff;
        ipAddress = Integer.toString(b[0]) + "." + Integer.toString(b[1]) + "." 
                + Integer.toString(b[2]) + "." + Integer.toString(b[3]);  
        return ipAddress;

    }

}
