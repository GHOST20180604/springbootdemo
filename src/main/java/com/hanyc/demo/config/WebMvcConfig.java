package com.hanyc.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * MVC自定义配置
 *
 * @author hanyc
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public UserRateLimiterInterceptor userRateLimiterInterceptor() {
        return new UserRateLimiterInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userRateLimiterInterceptor()).addPathPatterns("/**");
    }
}
