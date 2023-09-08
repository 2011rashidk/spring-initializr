package org.happiest.minds.springinitializr.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "spring")
@Data
public class SpringDependencyConfig {

    private Map<String, Map<String, String>> dependencies;
}
