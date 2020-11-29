package com.gintoki.debug.core;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
public class ProbeExecutor {
    private static final Logger log = LoggerFactory.getLogger(ProbeExecutor.class);

    @Resource
    private ProbeContext probeContext;

    private GroovyShell shell;

    private volatile boolean isInit = false;

    private final byte[] lock = new byte[0];

    public void init() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        CompilerConfiguration config = new CompilerConfiguration();
        config.setSourceEncoding(StandardCharsets.UTF_8.name());
        GroovyClassLoader gcl = new GroovyClassLoader(contextClassLoader, config);
        CompilerConfiguration conf = new CompilerConfiguration();
        conf.setSourceEncoding(StandardCharsets.UTF_8.name());
        conf.setScriptBaseClass(ProbeScript.class.getName());
        shell = new GroovyShell(gcl, conf);
        isInit = true;
    }

    public ProbeResponse<String> execute(String script) {
        try {
            if (!isInit) {
                synchronized (lock) {
                    if (!isInit) {
                        init();
                    }
                }
            }
            // TODO 重复执行的话 可以提供缓存
            Script s = shell.parse(script);
            String res = String.valueOf(s.run());
            // 防止perm区爆炸
            shell.getClassLoader().clearCache();
            return ProbeResponse.success(res);
        } catch (Exception e) {
            log.error("ScriptExecutor.exp, script:{}", script, e);
            return ProbeResponse.fail("脚本存在问题，请排查" + e);
        }
    }

}
