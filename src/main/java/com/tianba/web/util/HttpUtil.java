package com.tianba.web.util;

import com.arronlong.httpclientutil.common.HttpConfig;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

/**
 * 描述:
 * Http工具类
 *
 * @author yangtao
 * @create 2018-09-30 17:44
 */
public class HttpUtil {

    public static HttpConfig getRandomIpHttpConfig() {
        String randomIp = IpUtil.getRandomIp();
        Header[] headers = {
                new BasicHeader("X-Forwarded-For", randomIp),
                new BasicHeader("X-Real-IP", randomIp)
        };
        return HttpConfig.custom().headers(headers);
    }

}
