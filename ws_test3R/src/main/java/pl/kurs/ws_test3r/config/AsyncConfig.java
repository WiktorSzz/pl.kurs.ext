package pl.kurs.ws_test3r.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    private final ConfigProperties configProperties;

    public AsyncConfig(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    @Bean(name = "importTaskExecutor")
    public Executor importTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(configProperties.getCorePoolSize());
        executor.setMaxPoolSize(configProperties.getMaxPoolSize());
        executor.setQueueCapacity(configProperties.getQueueCapacity());
        executor.setThreadNamePrefix(configProperties.getThreadNamePrefix());
        executor.initialize();
        return executor;
    }
}
