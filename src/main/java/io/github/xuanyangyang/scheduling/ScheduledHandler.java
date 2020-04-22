package io.github.xuanyangyang.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;

import java.lang.reflect.Method;

/**
 * {@link Scheduled}处理器
 *
 * @author xuanyangyang
 * @since 2020/4/22 11:09
 */
public class ScheduledHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ScheduledService scheduledService;

    @EventListener
    public void handleApplicationStartedEvent(ApplicationStartedEvent event) {
        String[] names = applicationContext.getBeanDefinitionNames();
        for (String name : names) {
            Object bean = applicationContext.getBean(name);
            handleBean(bean);
        }
    }

    /**
     * 处理bean
     *
     * @param bean bean
     */
    public void handleBean(Object bean) {
        Method[] methods = bean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            Scheduled scheduled = method.getAnnotation(Scheduled.class);
            if (scheduled == null) {
                continue;
            }
            if (method.getParameterCount() != 0) {
                throw new IllegalArgumentException(method + "不支持有方法参数的定时任务");
            }
            String cron = scheduled.cron();
            long delay = scheduled.delay();
            long period = scheduled.period();
            if (cron.isBlank() && delay == 0 && period == 0) {
                throw new IllegalArgumentException(method + "无效定时任务，缺少定时参数");
            }
            if (!cron.isBlank() && delay != 0) {
                throw new IllegalArgumentException(method + "定时任务不能同时配置'cron'和'delay'");
            }
            if (!cron.isBlank() && period != 0) {
                throw new IllegalArgumentException(method + "定时任务不能同时配置'cron'和'period'");
            }
            String name = scheduled.name();
            if (name.isBlank()) {
                name = method.getName();
            }
            method.setAccessible(true);
            Runnable action = () -> taskAction(bean, method);
            if (cron.isBlank()) {
                if (period == 0) {
                    scheduledService.addTask(name, delay, scheduled.timeUnit(), action, scheduled.async());
                } else {
                    scheduledService.addTask(name, delay, period, scheduled.timeUnit(), action, scheduled.async());
                }
            } else {
                scheduledService.addTask(name, cron, action, scheduled.async());
            }
        }
    }

    /**
     * 任务载体
     *
     * @param obj    对象
     * @param method 任务调用的方法
     */
    private void taskAction(Object obj, Method method) {
        try {
            method.invoke(obj);
        } catch (Exception e) {
            logger.error("运行定时任务失败", e.getCause());
        }
    }
}
