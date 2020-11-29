package com.gintoki.debug.core;

import groovy.lang.Binding;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wyh 吴永涵
 * @date 2020/3/31 3:37 下午
 * @description
 */
@Component
public class ProbeContext implements ApplicationContextAware {

    private static ApplicationContext context;

    private final Map<String, Binding> bindingMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ProbeContext.context = applicationContext;
    }

    public ProbeProxy getProxy(String beanName) {
        Object bean = context.getBean(beanName);
        ProbeProxy proxy = ProbeProxy.createBean(bean);
        bindingMap.putIfAbsent(bean.getClass().getName(), createBinding(proxy));
        return proxy;
    }

    public <T> ProbeProxy<T> getProxy(Class<T> clazz) {
        T bean = context.getBean(clazz);
        ProbeProxy<T> proxy = ProbeProxy.createBean(bean);
        bindingMap.putIfAbsent(clazz.getName(), createBinding(proxy));
        return proxy;
    }

    private static <T> Binding createBinding(ProbeProxy<T> probeProxy) {
        Binding b = new Binding();
        Map<String, Method> methodMap = probeProxy.getMethodMap();
        methodMap.forEach(b::setVariable);
        return b;
    }

    /**
     * 不推荐使用这种方式获取bean，推荐使用proxy的方式获取
     */
    @Deprecated
    public static ApplicationContext getApplicationContext() {
        return context;
    }

}
