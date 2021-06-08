package com.baiyi.opscloud;


import com.baiyi.opscloud.kafka.config.KafkaConfig;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author baiyi
 * @Date 2019/12/25 4:00 下午
 * @Version 1.0
 */

@EnableConfigurationProperties({KafkaConfig.class})
@EnableTransactionManagement
@MapperScan(basePackages = "com.baiyi.opscloud.mapper")
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "10m")
@EnableCaching
@EnableAsync
@EnableRetry
@ComponentScan(
        basePackages = "com.baiyi"
)
public class ManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageApplication.class, args);
    }
}
