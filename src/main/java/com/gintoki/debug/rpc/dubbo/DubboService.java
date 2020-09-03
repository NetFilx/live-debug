package com.gintoki.debug.rpc.dubbo;

import com.gintoki.debug.core.Response;

/**
 * @author wyh
 * @version 1.0
 * @time 2020/9/3 7:47 下午
 */
public interface DubboService {

    public Response<String> execute(String script);

}
