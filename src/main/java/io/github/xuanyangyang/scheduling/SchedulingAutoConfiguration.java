package io.github.xuanyangyang.scheduling;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(SchedulingProperties.class)
public class SchedulingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "scheduledAsyncExecutor")
    public Executor scheduledAsyncExecutor(SchedulingProperties schedulingProperties) {
        SchedulingProperties.Pool pool = schedulingProperties.getPool();
        return Executors.newFixedThreadPool(pool.getAsyncPoolSize(), new CustomizableThreadFactory(pool.getAsyncThreadNamePrefix()));
    }

    @Bean
    @ConditionalOnMissingBean(ScheduledService.class)
    public ScheduledService scheduledService(Executor scheduledAsyncExecutor, SchedulingProperties schedulingProperties) {
        SchedulingProperties.Refresh refresh = schedulingProperties.getRefresh();
        ScheduledService scheduledService = new DefaultScheduledService(
                schedulingProperties.getPool().getThreadNamePrefix(),
                scheduledAsyncExecutor,
                refresh.isEnable(),
                refresh.getDelay(),
                refresh.getPeriod(),
                refresh.getThreshold());
        scheduledService.start();
        return scheduledService;
    }

    @Bean
    public ScheduledHandler scheduledHandler() {
        return new ScheduledHandler();
    }
}
