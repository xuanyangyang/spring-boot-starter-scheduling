package io.github.xuanyangyang.scheduling;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import scheduling.ScheduledService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 调度bean处理器
 *
 * @author xuanyangyang
 * @since 2020/3/31 15:37
 */
public class ScheduledBeanPostProcessor implements BeanPostProcessor {
    @Autowired
    private ScheduledService scheduledService;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
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
        return bean;
    }

    private void taskAction(Object obj, Method method) {
        try {
            method.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
