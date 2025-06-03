package com.sem.configs;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver; // Измените этот импорт
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

        logger.info("WebMVC conf");
        resolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
