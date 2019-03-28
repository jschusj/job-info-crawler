package com.tianba.web.conf;

import com.tianba.web.service.impl.JobServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述:
 * Spring Bean
 *
 * @author yangtao
 * @create 2018-10-06 23:10
 */
@Configuration
public class AppConfig {
    @Bean(initMethod = "init")
    public JobServiceImpl jobService() {
        return new JobServiceImpl();
    }
}
