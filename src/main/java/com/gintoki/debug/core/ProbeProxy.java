package com.gintoki.debug.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wyh
 * @version 1.0
 * @time 2020/11/29 4:26 下午
 * bean的代理类，可以做一些增强的事
 * thread safe
 */
public class ProbeProxy<T> {

    private final T bean;

    private final Map<String, Method> methodMap = new HashMap<>();

    private ProbeProxy(T bean) {
        this.bean = bean;
        this.methodMapping();
    }

    public static <T> ProbeProxy<T> createBean(T bean) {
        return new ProbeProxy<>(bean);
    }

    private void methodMapping() {
        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            method.setAccessible(Boolean.TRUE);
            methodMap.put(method.getName(), method);
        }
    }

    public Map<String, Method> getMethodMap() {
        return this.methodMap;
    }

    public T getBean() {
        return bean;
    }

    public Object invoke(String methodName, Object... param) throws InvocationTargetException, IllegalAccessException {
        Method method = methodMap.get(methodName);
        if (method == null) {
            return null;
        }
        return method.invoke(bean, param);
    }

}
