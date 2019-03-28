package com.tianba.web.conf;

import com.alibaba.druid.support.http.WebStatFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * 描述:
 * Servlet Filter implementation class DruidFilter
 *
 * @author yangtao
 * @create 2018-09-23 23:33
 */
@WebFilter(
        filterName = "druidWebStatFilter",
        urlPatterns = {"/*"},
        initParams = {
                @WebInitParam(name = "exclusions", value = "*.js,*.jpg,*.png,*.gif,*.ico,*.css,/druid/") // 配置这个过滤器应该放行的请求
        }
)
public class DruidStatFilter extends WebStatFilter {
}
