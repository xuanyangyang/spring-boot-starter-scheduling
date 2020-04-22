# spring boot 可调时间的定时任务
定时任务可以通过修改系统时间来触发
## 一、引入依赖
```xml
<dependency>
    <groupId>io.github.xuanyangyang</groupId>
    <artifactId>spring-boot-starter-scheduling</artifactId>
    <version>0.1.0</version>
</dependency>
```

## 二、使用指南
注入依赖
```java
@Autowired
private ScheduledService scheduledService;
```

### 1.添加定时任务
#### 通过scheduledService添加定时任务
添加延迟1秒，每3秒执行一次的任务
```java
scheduledService.addTask("testDelayAndPeriod", 1, 3, TimeUnit.SECONDS, () -> log.info("testDelayAndPeriod"));
```

添加cron任务
```java
scheduledService.addTask("testCron", "* * * * * ?", () -> log.info("testCron"));
```

添加异步cron任务
```java
scheduledService.addTask("testAsync", "*/5 * * * * ?", () -> log.info("testAsync"), true);
```

#### 通过注解的形式添加定时任务
添加延迟1秒，每3秒执行一次的任务
```java
@Scheduled(delay = 1, period = 3, timeUnit = TimeUnit.SECONDS)
private void testDelayAndPeriodByAnnotation() {
    log.info("testDelayAndPeriodByAnnotation");
}
```

添加cron任务
```java
@Scheduled(cron = "* * * * * ?")
public void testCronByAnnotation() {
    log.info("testCronByAnnotation");
}
```

添加异步cron任务
```java
@Scheduled(cron = "*/5 * * * * ?", async = true)
private void testAsyncByAnnotation() {
    log.info("testAsyncByAnnotation");
}
```
### 2.删除定时任务
通过任务名删除
```java
scheduledService.removeTask("taskName");
```
通过任务future删除
```java
ScheduledFuture scheduledFuture = scheduledService.addTask("testCron", "* * * * * ?", () -> log.info("testCron"));
scheduledFuture.cancel();
```