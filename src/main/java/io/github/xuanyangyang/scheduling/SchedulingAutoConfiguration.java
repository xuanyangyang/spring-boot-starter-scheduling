package io.github.xuanyangyang.scheduling;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import scheduling.DefaultScheduledService;
import scheduling.ScheduledService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 调度自动配置
 *
 * @author xuanyangyang
 * @since 2020/3/27 17:37
 */
public class SchedulingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "scheduledAsyncExecutor")
    public Executor scheduledAsyncExecutor() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new CustomizableThreadFactory("异步调度线程"));
    }

    @Bean
    @ConditionalOnMissingBean(ScheduledService.class)
    public ScheduledService scheduledService(Executor scheduledAsyncExecutor) {
        DefaultScheduledService scheduledService = new DefaultScheduledService(scheduledAsyncExecutor);
        scheduledService.init();
        return scheduledService;
    }

    @Bean
    @ConditionalOnMissingBean(ScheduledBeanPostProcessor.class)
    public ScheduledBeanPostProcessor scheduledBeanPostProcessor() {
        return new ScheduledBeanPostProcessor();
    }

    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(2);
        long toEpochMilli = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        System.out.println(toEpochMilli);
    }
}
