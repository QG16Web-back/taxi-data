package com.qg.taxi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Wilder Gao
 * time:2018/7/30
 * description：
 * motto: All efforts are not in vain
 */
@Configuration
public class HBaseConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/conf/**").addResourceLocations("classpath:/conf/");
        super.addResourceHandlers(registry);
    }
}
