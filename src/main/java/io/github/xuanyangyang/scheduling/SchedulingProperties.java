package io.github.xuanyangyang.scheduling;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 调度属性
 *
 * @author xuanyangyang
 * @since 2020/4/22 20:37
 */
@ConfigurationProperties(prefix = "xyy.scheduling")
public class SchedulingProperties {
    private Refresh refresh = new Refresh();
    private Pool pool = new Pool();

    public Refresh getRefresh() {
        return refresh;
    }

    public Pool getPool() {
        return pool;
    }

    /**
     * 刷新属性
     */
    public static class Refresh {
        /**
         * 是否启用刷新机制
         */
        private boolean enable = true;
        /**
         * 刷新初始延迟（毫秒）
         */
        private long delay = 0;
        /**
         * 刷新周期（毫秒）
         */
        private long period = 3000;
        /**
         * 刷新阈值（毫秒）
         */
        private long threshold = 10000;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public long getDelay() {
            return delay;
        }

        public void setDelay(long delay) {
            this.delay = delay;
        }

        public long getPeriod() {
            return period;
        }

        public void setPeriod(long period) {
            this.period = period;
        }

        public long getThreshold() {
            return threshold;
        }

        public void setThreshold(long threshold) {
            this.threshold = threshold;
        }
    }

    /**
     * 线程池配置
     */
    public static class Pool {
        /**
         * 线程名前缀
         */
        private String threadNamePrefix = "调度线程-";
        /**
         * 异步线程名前缀
         */
        private String asyncThreadNamePrefix = "异步" + threadNamePrefix;
        /**
         * 异步线程数量
         */
        private int asyncPoolSize = 0;

        public String getThreadNamePrefix() {
            return threadNamePrefix;
        }

        public void setThreadNamePrefix(String threadNamePrefix) {
            this.threadNamePrefix = threadNamePrefix;
        }

        public String getAsyncThreadNamePrefix() {
            return asyncThreadNamePrefix;
        }

        public void setAsyncThreadNamePrefix(String asyncThreadNamePrefix) {
            this.asyncThreadNamePrefix = asyncThreadNamePrefix;
        }

        public int getAsyncPoolSize() {
            if (asyncPoolSize == 0) {
                asyncPoolSize = Runtime.getRuntime().availableProcessors();
            }
            return asyncPoolSize;
        }

        public void setAsyncPoolSize(int asyncPoolSize) {
            this.asyncPoolSize = asyncPoolSize;
        }
    }
}
