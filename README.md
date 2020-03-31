### live-debug

> 正式项目中请对脚本运行接口做好权限控制

1. 打包：项目根目录运行: mvn clean install ,在target目录下生产可执行jar包：live-debug-0.0.1-SNAPSHOT.jar
2. 运行: 以jar包所在目录运行为例： java -jar live-debug-0.0.1-SNAPSHOT.jar, 默认使用8080端口(可自行修改springboot配置文件定义端口号)
3. 接口执行：
    - url: [http://${ip}:{port}/debug/execute](http://localhost:8080/groovy/script/execute)
    - method: post
    - bodytype: Text
    - body: groovy脚本

4. 示例groovy脚本：

+ 调用TestService方法：

    ```
    def query = testService.add(1,2)
    query
    ```
    结果返回："3".
