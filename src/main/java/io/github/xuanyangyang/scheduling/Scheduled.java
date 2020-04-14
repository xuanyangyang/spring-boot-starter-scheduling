package io.github.xuanyangyang.scheduling;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 调度注解
 *
 * @author xuanyangyang
 * @since 2020/3/26 17:59
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scheduled {
    /**
     * @return cron 表达式
     */
    String cron() default "";

    /**
     * @return 任务名
     */
    String name() default "";

    /**
     * @return 延迟多久执行
     */
    long delay() default 0;

    /**
     * @return 周期
     */
    long period() default 0;

    /**
     * @return 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * @return 异步执行
     */
    boolean async() default false;
}
