package com.gintoki.debug.http;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.gintoki.debug.core.ProbeRequest;
import com.gintoki.debug.core.ProbeResponse;
import com.gintoki.debug.core.ProbeExecutor;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author wyh 吴永涵
 * @date 2020/3/31 3:24 下午
 * @description
 */
@RestController
@RequestMapping("/debug")
@Slf4j
public class ProbeController {

    @Resource
    private ProbeExecutor scriptExecutor;

    @Value("${dubbo.application.name}")
    private String appName;

    @Value("${dubbo.registry.address}")
    private String registryAddress;

    @PostMapping("/execute")
    @ResponseBody
    public ProbeResponse<String> execute(@RequestBody ProbeRequest request) {
        // 本地调用
        if (StringUtils.isEmpty(request.getAppName()) || appName.equals(request.getAppName())) {
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
    private ProbeResponse<String> dubboCall(ProbeRequest request) {
        try {
            ApplicationConfig ac = new ApplicationConfig();
            ac.setName(request.getAppName());
            RegistryConfig rc = new RegistryConfig();
            rc.setAddress(registryAddress);
            ac.setRegistry(rc);
            ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
            reference.setInterface("com.gintoki.debug.rpc.dubbo.DubboService");
            reference.setVersion(request.getAppName());
            reference.setGeneric(true);
            reference.setApplication(ac);
            GenericService genericService = reference.get();
            String res = (String) genericService.$invoke("execute", new String[]{String.class.getName()}, new Object[]{request.getScript()});
            return ProbeResponse.success(res);
        } catch (Exception e) {
            return ProbeResponse.fail("调用dubbo出错" + e.getMessage());
        }
    }


}
