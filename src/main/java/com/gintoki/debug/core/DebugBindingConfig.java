package com.gintoki.debug.core;

import groovy.lang.Binding;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author wyh 吴永涵
 * @date 2020/3/31 3:37 下午
 * @description
 */
@Configuration
public class DebugBindingConfig implements ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public Map<String, Object> getBeans() {
        return context.getBeansOfType(Object.class);
    }
}
