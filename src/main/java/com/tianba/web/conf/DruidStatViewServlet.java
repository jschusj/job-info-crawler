package com.tianba.web.conf;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * 描述:
 * Servlet implementation class DruidStateViewServlet
 *
 * @author yangtao
 * @create 2018-09-23 23:38
 */
@WebServlet(
        urlPatterns = {"/druid/*"},
        initParams = {
                @WebInitParam(name = "loginUsername", value = "root"),
                @WebInitParam(name = "loginPassword", value = "Yang163110"),
                @WebInitParam(name = "resetEnable", value = "true")
        }
)
public class DruidStatViewServlet extends StatViewServlet implements Servlet {
    private static final long serialVersionUID = 1L;
}
