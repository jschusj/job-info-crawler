package com.tianba.web.job;

import com.tianba.web.service.IJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 描述:
 * 商品相关定时任务
 *
 * @author yangtao
 * @create 2018-09-27 22:21
 */
@Component
@Slf4j
public class CrawlJob {

    @Autowired
    private IJobService jobService;

//    @Scheduled(cron = "0 0/1 * * * ?")
    @Scheduled(cron = "0/1 * * * * ?")
    public void crawlJobInfo() {
        log.info("开始执行：采集招聘信息");
        jobService.crawlJobInfo();
        log.info("执行完毕：采集招聘信息");
    }
}
