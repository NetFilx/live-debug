package com.gintoki.debug.http;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.gintoki.debug.core.DebugRequest;
import com.gintoki.debug.core.DebugScript;
import com.gintoki.debug.core.Response;
import com.gintoki.debug.core.ScriptExecutor;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.util.logging.Slf4j;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.rmi.registry.Registry;

/**
 * @author wyh 吴永涵
 * @date 2020/3/31 3:24 下午
 * @description
 */
@RestController
@RequestMapping("/debug")
@Slf4j
public class DebugController {

    @Resource
    private ScriptExecutor scriptExecutor;

    @Value("${dubbo.application.name}")
    private String appName;

    @Value("${dubbo.registry.address}")
    private String registryAddress;

    @PostMapping("/execute")
    @ResponseBody
    public Response<String> execute(@RequestBody DebugRequest request) {
        // 本地调用
        if (appName.equals(request.getAppName())) {
            return scriptExecutor.execute(request.getScript());
        }

        // dubbo 调用
        return dubboCall(request);
    }

    /**
     * dubbo 泛化调用
     *
     * @param request
     * @return
     */
    private Response<String> dubboCall(DebugRequest request) {
        try {
            ApplicationConfig ac = new ApplicationConfig();
            ac.setName(request.getAppName());
            RegistryConfig rc = new RegistryConfig();
            rc.setAddress(registryAddress);
            ac.setRegistry(rc);
            ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
            reference.setInterface("com.gintoki.debug.rpc.dubbo.DubboService");
            reference.setGeneric(true);
            reference.setApplication(ac);
            GenericService genericService = reference.get();
            String res = (String) genericService.$invoke("execute", new String[]{String.class.getName()}, new Object[]{request.getScript()});
            return Response.success(res);
        } catch (Exception e) {
            return Response.fail("调用dubbo出错" + e.getMessage());
        }
    }


}
