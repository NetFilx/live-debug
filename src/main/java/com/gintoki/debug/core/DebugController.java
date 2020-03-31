package com.gintoki.debug.core;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

/**
 * @author  wyh 吴永涵
 * @date  2020/3/31 3:24 下午
 * @description  
 */
@RestController
@RequestMapping("/debug")
public class DebugController {

    @Autowired
    private Binding binding;

    private GroovyShell shell;

    @PostConstruct
    public void init() {
        GroovyClassLoader gcl = new GroovyClassLoader(this.getClass().getClassLoader());
        CompilerConfiguration conf = new CompilerConfiguration();
        conf.setSourceEncoding(StandardCharsets.UTF_8.name());
        conf.setScriptBaseClass(DebugScript.class.getName());

        shell = new GroovyShell(gcl, binding, conf);
    }

    @PostMapping("/execute")
    public String execute(@RequestBody String script) {
        try {
            // TODO 重复执行的话 可以提供缓存
            Script s = shell.parse(script);
            String res = String.valueOf(s.run());
            // 防止perm区爆炸
            shell.getClassLoader().clearCache();
            return res;
        } catch (Exception e) {
            return "脚本出错";
        }

    }


}
