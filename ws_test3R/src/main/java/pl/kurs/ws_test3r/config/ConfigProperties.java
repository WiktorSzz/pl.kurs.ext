package pl.kurs.ws_test3r.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.async.executor")
public class ConfigProperties {

    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;
    private String threadNamePrefix;
}
