package io.github.xuanyangyang.scheduling;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

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
        ScheduledService scheduledService = new DefaultScheduledService(scheduledAsyncExecutor);
        scheduledService.start();
        return scheduledService;
    }

    @Bean
    public ScheduledHandler scheduledHandler() {
        return new ScheduledHandler();
    }
}
