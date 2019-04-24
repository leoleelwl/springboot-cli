package com.jhy.app.common.runner;

import com.jhy.app.common.exceptions.RedisConnectException;
import com.jhy.app.common.service.CacheService;
import com.jhy.app.system.domain.User;
import com.jhy.app.system.manager.UserManager;
import com.jhy.app.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 缓存初始化
 */
@Slf4j
@Component
public class CacheInitRunner implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private CacheService cacheService;
    @Autowired
    private UserManager userManager;

    @Autowired
    private ConfigurableApplicationContext context;

    @Override
    public void run(ApplicationArguments args) {
        try {
            log.info("Redis连接中 ······");
            cacheService.testConnect();
            log.info("缓存初始化 ······");
            log.info("缓存用户数据 ······");
            List<User> list = this.userService.list();
            for (User user : list) {
                userManager.loadUserRedisCache(user);
            }
        } catch (Exception e) {
            log.error("缓存初始化失败，{}", e.getMessage());

            log.error(" _______    ___       __   __       _______  _______");
            log.error("|   ____|  /   \\     |  | |  |     |   ____||       \\");
            log.error("|  |__    /  ^  \\    |  | |  |     |  |__   |  .--.  |");
            log.error("|   __|  /  /_\\  \\   |  | |  |     |   __|  |  |  |  |");
            log.error("|  |    /  _____  \\  |  | |  `----.|  |____ |  '--'  |");
            log.error("|__|   /__/     \\__\\ |__| |_______||_______||_______/");
            log.error("springboot-cli 启动失败              ");
            if (e instanceof RedisConnectException)
                log.error("Redis连接异常，请检查Redis连接配置并确保Redis服务已启动");
            // 关闭 contxt
            context.close();
        }
    }
}
