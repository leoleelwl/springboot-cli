package com.jhy.app.common.config;


import com.jhy.app.common.utils.MailUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringMailConfig {

    @Bean
    public MailUtil mailUtil(){
        return  new MailUtil();
    }
}
