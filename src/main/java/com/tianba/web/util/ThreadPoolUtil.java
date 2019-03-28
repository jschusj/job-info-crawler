package com.tianba.web.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 描述:
 * 线程池工具类
 *
 * @author yangtao
 * @create 2018-09-24 12:04
 */
public class ThreadPoolUtil {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * 单线程线程池：一个workder，顺序执行
     */
    public static void singleThreadExecut(Thread thread) {
        executor.execute(thread);
    }

}
