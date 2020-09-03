package com.gintoki.debug.rpc.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.gintoki.debug.core.Response;
import com.gintoki.debug.core.ScriptExecutor;

import javax.annotation.Resource;

/**
 * 提供dubbo接口
 *
 * @author wyh
 * @version 1.0
 * @time 2020/9/2 5:20 下午
 */
@Service(version = "${dubbo.application.name}")
public class DubboService {

    @Resource
    private ScriptExecutor scriptExecutor;

    public Response<String> execute(String script) {
        return scriptExecutor.execute(script);
    }

}
