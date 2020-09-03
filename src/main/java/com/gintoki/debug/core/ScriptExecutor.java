package com.gintoki.debug.core;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * 脚本执行器
 *
 * @author wyh
 * @version 1.0
 * @time 2020/9/3 11:55 上午
 */
@Component
public class ScriptExecutor {

    @Resource
    private Binding groovyBinding;

    private GroovyShell shell;

    private boolean isInit;

    @PostConstruct
    public void init() {
        try {
            GroovyClassLoader gcl = new GroovyClassLoader(this.getClass().getClassLoader());
            CompilerConfiguration conf = new CompilerConfiguration();
            conf.setSourceEncoding(StandardCharsets.UTF_8.name());
            conf.setScriptBaseClass(DebugScript.class.getName());
            shell = new GroovyShell(gcl, groovyBinding, conf);
            isInit = true;
        } catch (Exception e) {
            isInit = false;
        }
    }

    public Response<String> execute(String script) {
        if (isInit) {
            return Response.fail("scriptExecutor init fail");
        }
        try {
            // TODO 重复执行的话 可以提供缓存
            Script s = shell.parse(script);
            String res = String.valueOf(s.run());
            // 防止perm区爆炸
            shell.getClassLoader().clearCache();
            return Response.success(res);
        } catch (Exception e) {
            return Response.fail("脚本存在问题，请排查" + e);
        }
    }

}
