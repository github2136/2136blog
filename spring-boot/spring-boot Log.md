Log设置
===
https://blog.csdn.net/weixin_41846320/article/details/86078522  
spring-boot默认使用Logback打印日志，日志等级分为
* TRACE
* DEBUG
* INFO
* WARN
* ERROR
  
默认打印等级为`INFO`  
在Spring boot中一般使用slf4j全称为`Simple Loging Facade For Java`，他是日志输出的一种接口，不包括具体实现。Logback默认实现了slf4j接口，而如果要使用log4j则需要一个适配代码。
```java
private static final Logger logger = LoggerFactory.getLogger(SpringGradleProjectApplication.class);
public static void main(String[] args) {
    logger.trace("trace");
    logger.debug("debug");
    logger.info("info");
    logger.warn("warn");
    logger.error("error");
}
```
默认只会打印`info`/`warn`/`error`，`trace`和`debug`不会打印出来  
修改`application.yaml`来设置Log
```yaml
logging:
  level:
    com:
      xx:
        xxx: trace #com.xx.xxx 指定包名下日志级别
    root: trace #默认日志打印级别
  path: user/local/log #日志打印目录，或者使用绝对路径f:/user
  file: log.log #日志打印文件，当目录和文件同时设置是文件优先级更高
  file:
    max-size: 1Kb #设置单个日志文件大小，与设置日志文件冲突
    max-history: 100 #logback专用，设置保存日志文件数量
  pattern:
    console: '%d{yyyy-MMM-dd} %-5level [%thread] %logger{15} - %msg%n' #logback专用，控制台输出，默认为%clr(%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd HH:mm:ss.SSS}}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}
    file: '%d{yyyy-MMM-dd} %-5level [%thread] %logger{15} - %msg%n' #logback专用，文件输出，默认为%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd HH:mm:ss.SSS}} ${LOG_LEVEL_PATTERN:-%5p} ${PID:- } --- [%t] %-40.40logger{39} : %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}
    dateformat: 'yyyy-MM-dd' #logback专用，时间输出格式，默认为yyyy-MM-dd HH:mm:ss.SSS
    level: '%5p' #logback专用，输出日志级别，默认为%5p
  exception-conversion-word: '%wEx' #记录异常时使用的转换字，默认%wEx
  config: #日志配置文件路径，例如`classpath:logback.xml`，优先级最高
```
## 日志文件XML配置
***
根据不同的日志系统，你可以按如下规则组织配置文件名，就能被正确加载：
* Logback：logback-spring.xml, logback-spring.groovy, logback.xml, logback.groovy
* Log4j：log4j-spring.properties, log4j-spring.xml, log4j.properties, log4j.xml
* Log4j2：log4j2-spring.xml, log4j2.xml
* JDK (Java Util Logging)：logging.properties

Spring Boot官方推荐优先使用带有-spring的文件名作为你的日志配置（如使用logback-spring.xml，而不是logback.xml），命名为logback-spring.xml的日志配置文件，spring boot可以为它添加一些spring boot特有的配置项。上面是默认的命名规则，并且放在src/main/resources下面即可。如果你即想完全掌控日志配置，但又不想用logback.xml作为Logback配置的名字，可以通过logging.config属性指定自定义的名字：
* 根节点&lt;configuration&gt;包含的属性
  * scan: 当此属性设置为true时，配置文件如果发生改变，将会被重新加载，默认值为true。
  * scanPeriod: 设置监测配置文件是否有修改的时间间隔，如果没有给出时间单位，默认单位是毫秒。当scan为true时，此属性生效。默认的时间间隔为1分钟。
  * debug: 当此属性设置为true时，将打印出logback内部日志信息，实时查看logback运行状态。默认值为false。

&lt;configuration&gt;子节点
* &lt;contextName&gt; 上下文名称：每个logger都关联到logger上下文，默认上下文名称为“default”。但可以使用设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改,可以通过%contextName来打印日志上下文名称。
* &lt;property&gt; 变量：用来定义变量值的标签， 有两个属性，name和value；其中name的值是变量的名称，value的值时变量定义的值。通过定义的值会被插入到logger上下文中。定义变量后，可以使`${}`来使用变量。  
  声明：`<property name="server_name" value="operation" />`  
  使用：`${server_name}`
* &lt;appender&gt; appender用来格式化日志输出节点，有俩个属性name和class，class用来指定哪种输出策略，常用就是控制台输出策略和文件输出策略。
  ```xml
  <!--输出到控制台-->
  <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
      <!--
      <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
          <level>ERROR</level>
      </filter>
      -->
      <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
          <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符-->
          <pattern>[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%thread] %highlight([%-5level] %logger{50} - %msg%n)</pattern>
          <charset>GBK</charset>
      </encoder>
  </appender>
  ```
  &lt;encoder&gt;表示对日志进行编码：

   * %d{HH: mm:ss.SSS}——日志输出时间
   * %thread——输出日志的进程名字，这在Web应用以及异步任务处理中很有用
   * %-5level——日志级别，并且使用5个字符靠左对齐
   * %logger{36}——日志输出者的名字
   * %msg——日志消息
   * %n——平台的换行符
  ThresholdFilter为系统定义的拦截器，例如我们用ThresholdFilter来过滤掉ERROR级别以下的日志不输出到文件中。如果不用记得注释掉，不然你控制台会发现没日志

  输出到文件RollingFileAppender

  另一种常见的日志输出到文件，随着应用的运行时间越来越长，日志也会增长的越来越多，将他们输出到同一个文件并非一个好办法。RollingFileAppender用于切分文件日志：
  ```xml
  <!--输出到文件-->
  <appender name="SYSTEM_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
      <!-- 过滤器，只打印ERROR级别的日志 -->
      <filter class="ch.qos.logback.classic.filter.LevelFilter">
          <level>ERROR</level>
          <onMatch>ACCEPT</onMatch>
          <onMismatch>DENY</onMismatch>
      </filter>
      <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
          <!--日志文件输出的文件名-->
          <FileNamePattern>${LOG_HOME}/${PROJECT_NAME}.error.%d{yyyy-MM-dd}.%i.log</FileNamePattern>
          <!--日志文件保留天数-->
          <MaxHistory>15</MaxHistory>
          <!--日志文件最大的大小-->
          <MaxFileSize>10MB</MaxFileSize>
      </rollingPolicy>

      <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
          <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符-->
          <pattern>[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%thread] [%-5level] %logger{50} - %msg%n</pattern>
          <charset>UTF-8</charset>
      </encoder>
  </appender>
  ```

  **[pattern说明](http://logback.qos.ch/manual/layouts.html#ClassicPatternLayout)**
  | 转换字(区分大小写)                                                                                                                                                                                      | 说明                                                                            |
  | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------- |
  | c{length}<br>lo{length}<br>logger{length}<br>                                                                                                                                                           | 调用`LoggerFactory.getLogger(class)`所传入的类                                  |
  | C{length}<br>class{length}                                                                                                                                                                              | `logger.error("error")`打印日志的类名，除非执行速度不是问题，否则应避免使用它。 |
  | contextName<br>cn                                                                                                                                                                                       |                                                                                 |
  | d{pattern}<br>date{pattern}<br>d{pattern, timezone}<br>date{pattern, timezone}                                                                                                                          | 时间                                                                            |
  | F / file                                                                                                                                                                                                | 打印日志的类文件名，除非执行速度不是问题，否则应避免使用它。                    |
  | caller{depth}<br>caller{depthStart..depthEnd}<br>caller{depth, evaluator-1, ... evaluator-n}<br>caller{depthStart..depthEnd, evaluator-1, ... evaluator-n}                                              | 输出生成日志事件的呼叫者的位置信息。                                            |
  | L / line                                                                                                                                                                                                | 打印日志的行号，除非执行速度不是问题，否则应避免使用它。                        |
  | m / msg / message                                                                                                                                                                                       | 打印日志信息                                                                    |
  | M / method                                                                                                                                                                                              | 打印日志的方法 除非执行速度不是问题，否则应避免使用它。                         |
  | n                                                                                                                                                                                                       | 换行符                                                                          |
  | p / le / level                                                                                                                                                                                          | 日志级别                                                                        |
  | r / relative                                                                                                                                                                                            | 输出从应用程序启动到创建日志记录事件为止经过的毫秒数。                          |
  | t / thread                                                                                                                                                                                              | 打印日志的线程名                                                                |
  | ex{depth}<br>exception{depth}<br>throwable{depth}<br>ex{depth, evaluator-1, ..., evaluator-n}<br>exception{depth, evaluator-1, ..., evaluator-n}<br>throwable{depth, evaluator-1, ..., evaluator-n}<br> | 打印错误信息                                                                    |

  还有几个不常用的自行查阅文档，使用转换字必须在前面使用`%`

  格式化内容

  | 示例          | 左对齐 | 最小宽度 | 最大宽度 | 说明                                                                   |
  | ------------- | ------ | -------- | -------- | ---------------------------------------------------------------------- |
  | %20logger     | false  | 20       | none     | 如果内容少于20个字符则在左边添加空格                                   |
  | %-20logger    | true   | 20       | none     | 如果内容少于20个字符则在右边添加空格                                   |
  | %.30logger    | NA     | none     | 30       | 如果内容超过30个字符则保留尾部内容                                     |
  | %20.30logger  | false  | 20       | 30       | 如果内容不足20个字符则左边添加空格，如果内容超过30个字符则保留尾部内容 |
  | %-20.30logger | true   | 20       | 30       | 如果内容不足20个字符则右边添加空格，如果内容超过30个字符则保留尾部内容 |
  | %.-30logger   | NA     | none     | 30       | 如果内容超过30个字符则保留头部内容                                     |

  如果只想打印T，D，W，I和E而不是TRACE，DEBUG，WARN，INFO或ERROR，可以使用`%.-1level`指定字符长度和对齐方式

  logback支持使用 `%black`, `%red`, `%green`,`%yellow`,`%blue`, `%magenta`,`%cyan`, `%white`, `%gray`, `%boldRed`,`%boldGreen`, `%boldYellow`, `%boldBlue`, `%boldMagenta`, `%boldCyan`, `%boldWhite`, `%highlight`来对内容进行颜色改变  
  `％highlight`表示将`error`转换为加粗红色，`warn`为红色，`info`为蓝色，其他为默认色
  ```xml
   <pattern>[%thread] %highlight(%-5level) %cyan(%logger{15}) - %msg %n</pattern>
  ```
  使用`withJansi`设置为`true`可以在输出的log日志中给log着色，但控制台和编辑器必须支持`Jansi`库否则不会变色

* 其中重要的是rollingPolicy的定义，上例中<fileNamePattern>logback.%d{yyyy-MM-dd}.log</fileNamePattern>定义了日志的切分方式——把每一天的日志归档到一个文件中，<maxHistory>30</maxHistory>表示只保留最近30天的日志，以防止日志填满整个磁盘空间。同理，可以使用%d{yyyy-MM-dd_HH-mm}来定义精确到分的日志切分方式。<totalSizeCap>1GB</totalSizeCap>用来指定日志文件的上限大小，例如设置为1GB的话，那么到了这个值，就会删除旧的日志。
* &lt;root&gt; root节点是必选节点，用来指定最基础的日志输出级别，只有一个level属性。默认是DEBUG。可以包含零个或多个元素，标识这个appender将会添加到这个loger。
  ```xml
  <root level="INFO"><!-- 设置默认打印级别 -->
     <appender-ref ref="CONSOLE" />
     <appender-ref ref="SYSTEM_FILE" />
  </root>
  ```
* &lt;logger&gt; <logger>用来设置某一个包或者具体的某一个类的日志打印级别、以及指定<appender>。<logger>仅有一个name属性，一个可选的level和一个可选的additivity属性。
  * name 用来指定受此loger约束的某一个包或者具体的某一个类。
  * level 用来设置打印级别，大小写无关：TRACE, DEBUG, INFO, WARN, ERROR, ALL 和 OFF，还有一个特殊值INHERITED或者同义词NULL，代表强制执行上级的级别。如果未设置此属性，那么当前logger将会继承上级的级别。
  * additivity 是否向上级logger传递打印信息。默认是true。false：表示只用当前logger的appender-ref。true：表示当前logger的appender-ref和rootLogger的appender-ref都有效。
  
  `<logger name="com.demo.springgradleproject.control" level="ERROR"/>`指定包名或文件的打印等级，或者指定打印方式
  ```xml
  <logger name="com.demo.springgradleproject.control" level="WARN" additivity="false">
    <appender-ref ref="STDOUT"/>
  </logger>
  ```
* 多环境日志输出

  根据不同环境（prod:生产环境，test:测试环境，dev:开发环境）来定义不同的日志输出
  ```xml
  <!-- 测试环境+开发环境. 多个使用逗号隔开. -->
  <springProfile name="test,dev">
      <logger name="com.dudu.controller" level="info" />
  </springProfile>
  <!-- 生产环境. -->
  <springProfile name="prod">
      <logger name="com.dudu.controller" level="ERROR" />
  </springProfile>
  ```
  
  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <configuration>
      <include resource="org/springframework/boot/logging/logback/base.xml"/><!--引入默认设置-->
      <logger name="com.demo.springgradleproject.control" level="ERROR"/>
      <logger name="com.demo.springgradleproject.control2" level="TRACE"/>
  </configuration>
  ```
* 示例
  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <configuration>
      <!--日志位置-->
      <property name="LOG_PATH" value="log/log"/>
      <property name="ERROR_PATH" value="log/error"/>
      <!--项目名-->
      <property name="PROJECT_NAME" value="test"/>
      <!--日志详情-->
      <property name="PATTERN"
                value="%yellow(%date{yyyy-MM-dd HH:mm:ss.SSS})|%highlight(%-5level)|%blue(%thread)|%blue(%file:%line)|%green(%logger)|%msg%n"/>
      <!--简版详情-->
      <property name="PATTERN_SHORT"
                value="%yellow(%date{yy-MM-dd HH:mm:ss.SSS})|%highlight(%-5level)|%green(%logger)|%msg%n"/>

      <!-- 控制台输出 -->
      <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
          <withJansi>false</withJansi>
          <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
              <pattern>${PATTERN}</pattern>
          </encoder>
      </appender>

      <!--输出错误到文件-->
      <appender name="ERROR_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
          <!-- 过滤器，只打印ERROR级别的日志 -->
          <filter class="ch.qos.logback.classic.filter.LevelFilter">
              <level>ERROR</level>
              <onMatch>ACCEPT</onMatch>
              <onMismatch>DENY</onMismatch>
          </filter>
          <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
              <!--日志文件输出的文件名-->
              <FileNamePattern>${ERROR_PATH}/${PROJECT_NAME}.error.%d{yyyy-MM-dd}.%i.log</FileNamePattern>
              <!--日志文件最大的大小-->
              <MaxFileSize>1MB</MaxFileSize>
          </rollingPolicy>

          <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
              <pattern>${PATTERN}</pattern>
              <charset>UTF-8</charset>
          </encoder>
      </appender>

      <!--输出所有日志-->
      <appender name="LOG_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
          <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
              <!--日志文件输出的文件名-->
              <FileNamePattern>${LOG_PATH}/${PROJECT_NAME}.log.%d{yyyy-MM-dd}.%i.log</FileNamePattern>
              <!--日志文件保留天数-->
              <MaxHistory>15</MaxHistory>
              <!--日志文件最大的大小-->
              <MaxFileSize>50KB</MaxFileSize>
          </rollingPolicy>

          <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
              <pattern>${PATTERN_SHORT}</pattern>
              <charset>UTF-8</charset>
          </encoder>
      </appender>

      <root level="INFO">
          <appender-ref ref="CONSOLE"/>
          <appender-ref ref="ERROR_FILE"/>
          <appender-ref ref="LOG_FILE"/>
      </root>
  </configuration>
  ```