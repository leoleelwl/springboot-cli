package com.jhy.app.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private ShiroProperties shiro = new ShiroProperties();

    private boolean openAopLog = true;

}
