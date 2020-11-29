package com.gintoki.debug.core;

/**
 * @author wyh 吴永涵
 * @date 2020/4/3 1:11 下午
 * @description
 */
public class ProbeRequest {

    private String script;

    private String appName;

    public ProbeRequest() {
    }

    public ProbeRequest(String script) {
        this.script = script;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
