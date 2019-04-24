package com.jhy.app.common.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Order
@Slf4j
@Component
public class StartedUpRunner implements ApplicationRunner {

    @Autowired
    private ConfigurableApplicationContext context;

    @Override
    public void run(ApplicationArguments args) {
        if (context.isActive()) {
            log.info("          _______.  __    __    ______   ______  _______     _______.     _______.");
            log.info("         /       | |  |  |  |  /      | /      ||   ____|   /       |    /       |");
            log.info("        |   (----` |  |  |  | |  ,----'|  ,----'|  |__     |   (----`   |   (----`");
            log.info("        \\   \\     |  |  |  | |  |     |  |     |   __|     \\   \\        \\   \\");
            log.info("    .----)   |     |  `--'  | |  `----.|  `----.|  |____.----)   |   .----)   |");
            log.info("    |_______/       \\______/   \\______| \\______||_______|_______/    |_______/");

            log.info("springboot-cli 启动完毕，时间：" + LocalDateTime.now());
        }
    }
}
