spring-boot
===
* `IDEAIntelliJ IDEA 2018.3.5 (Ultimate Edition)`
* `mysql 8.0.16`
* `spring-boot 2.1.5.RELEASE`
* `jdk1.8`

spring-boot项目初始化使用IDEA，File -> New -> Project -> Spring Initializr -> 选择Type(Gradle Project)/Language/Packaging/Java Version等信息 -> 添加 Spring Boot DevTools / Spring Web -> Web创建项目基本文件。
## 开启热部署
***
* 添加`DevTools`
* File -> Settings -> Build,Execution,Deplyment -> Compiler，勾选 Build project automatically
* Shift+Ctrl+Alt+/ -> Registry -> 勾选 compiler.automake.allow.when.app.running

## 编写controller
***
* 新建类给类名加上`@RestController`注解
* 给方法加上`@RequestMapping("/hello")`注解，参数为url路径
    ```java
    @RestController
    public class HelloWorldController {
        @RequestMapping("/hello")
        public String index() {
            return "Hello World";
        }
    }
    ```
    * Controller默认输出为json格式使用jackson，默认时间格式为`yyyy-MM-dd'T'HH:mm:ss.SSSZ`，可以在`application.yaml`中修改默认时间格式及时区
    ```yaml
    spring:
    jackson:
        date-format: yyyy-MM-dd'T'HH:mm:ss.SSSZ
        time-zone: GMT+8
    ```  
给类添加`@RequestMapping("/hello")`可以设置请求默认前缀
## 单元测试
***
添加`testImplementation 'org.springframework.boot:spring-boot-starter-test'`已支持单元测试
```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringGradleProjectApplicationTests {
    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new HomeControl()).build();
    }

    @Test
    public void contextLoads1() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.equalTo("Hello World")));
    }
    @Test
    public void contextLoads2() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}

```
## 自定义过滤器
***
```java
@Configuration
public class WebConfiguration {
    @Bean
    public RemoteIpFilter remoteIpFilter() {
        return new RemoteIpFilter();
    }

    @Bean
    public FilterRegistrationBean testFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new MyFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("MyFilter");
        registration.setOrder(1);
        return registration;
    }

    public class MyFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            HttpServletRequest request1 = (HttpServletRequest) request;
            System.out.println("this is MyFilter,url:" + request1.getRequestURL());
            chain.doFilter(request, response);
        }

        @Override
        public void destroy() {

        }
    }
}
```
## 自定义属性
***
自定义属性有两种方法，属性要么在`application.yaml`里面设置值或者设置默认值
```java
@Component
public class NeoProperties {
    //使用:value方法添加默认值
    @Value("${com.sam.name:fsddsdf}")
    private String t1;
    @Value("${com.a.xxxxx:4s6sdf}")
    private String t2;

    public String getT1() {
        return t1;
    }

    public void setT1(String t1) {
        this.t1 = t1;
    }

    public String getT2() {
        return t2;
    }

    public void setT2(String t2) {
        this.t2 = t2;
    }
}
```
或
```java
@Component
//设置指定前缀
@ConfigurationProperties(prefix = "com.xxx")
public class NeoProperties2 {
    //设置默认值
    private String n1 = "fsdf";
    private String n2 = "fssdfdf";

    public String getN1() {
        return n1;
    }

    public void setN1(String n1) {
        this.n1 = n1;
    }

    public String getN2() {
        return n2;
    }

    public void setN2(String n2) {
        this.n2 = n2;
    }
}
```
## 注入方法
***
* 参数注入
    ```java
    @Autowired
    private NeoProperties neoProperties;
    ```
    优点：代码简洁，方法简单  
    缺点：非IOC容器只能使用反射提供依赖，调用时才会提示错误；可能会导致循环依赖，spring team不建议使用
* 构造函数注入
    ```java
    @RestController
    public class HomeControl {
        private NeoProperties neoProperties;
        private NeoProperties2 neoProperties2;

        @Autowired
        public HomeControl(NeoProperties neoProperties, NeoProperties2 neoProperties2) {
            this.neoProperties = neoProperties;
            this.neoProperties2 = neoProperties2;
        }

        @RequestMapping("/")
        public String index() {
            return neoProperties2.getN1();
        }
    }
    ```
    优点：依赖不可空；非IOC容器可使用构造方法传入依赖，使用构造器注入有循环依赖启动时提示  
    缺点：注入参数多是代码量大
    * setter注入
    ```java
    @Autowired
    public void setNeoProperties(NeoProperties neoProperties) {
        this.neoProperties = neoProperties;
    }
    ```
    优点：相比构造方法注入参数可以，允许在构造完成后注入
## log配置
***
设置日志输出地址和输出级别
```yaml
logging:
  level:
    root: info
  path: user/local/log
```
## 数据库操作
***
添加JPA`implementation 'org.springframework.boot:spring-boot-starter-data-jpa'`、`implementation 'mysql:mysql-connector-java'`  
连接设置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/spring-data
    username: root
    password: root
    driver-class-name: com.mysql.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true # 只有语句没有参数
```
ddl-auto：用于创建更新验证数据库表结构，一共有4个值
* create： 每次加载 hibernate 时都会删除上一次的生成的表，然后根据你的 model 类再重新来生成新表，哪怕两次没有任何改变也要这样执行，这就是导致数据库表数据丢失的一个重要原因。
* create-drop ：每次加载 hibernate 时根据 model 类生成表，但是 sessionFactory 一关闭,表就自动删除。
* update：最常用的属性，第一次加载 hibernate 时根据 model 类会自动建立起表的结构（前提是先建立好数据库），以后加载 hibernate 时根据 model 类自动更新表结构，即使表结构改变了但表中的行仍然存在不会删除以前的行。要注意的是当部署到服务器后，表结构是不会被马上建立起来的，是要等 应用第一次运行起来后才会。
* validate ：每次加载 hibernate 时，验证创建数据库表结构，只会和数据库中的表进行比较，不会创建新表，但是会插入新值。

如果提示`The server time zone value '�й���׼ʱ��' is unrecognized or represents more than one time zone.`是由于mysql新版驱动处理方法有几种
* 在数据库连接最后添加`serverTimezone=UTC`，`jdbc:mysql://localhost:3306/spring-data?serverTimezone=UTC`
* 命令行设置，mysql服务重启是会失效
    ```sql
    SET GLOBAL time_zone = '+8:00';# 设置全局时区
    SET time_zone = '+8:00';# 设置时区为东八区
    FLUSH PRIVILEGES;# 刷新权限使设置立即生效
    SHOW VARIABLES LIKE '%time_zone%';# 查看是否设置成功
    ```
* 或者在默认配置文件(默认位置为：C:\ProgramData\MySQL\MySQL Server 8.0\my.ini)里的`[mysqld]`下添加`default-time-zone='+8:00'`，然后重启mysql
## 添加实体类和Dao
***
实体类
```java
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {
    @Id//主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//增长方式，id自增长
    private Long id;
    @Column(nullable = false)//注解可为空，nullable=false表示不可为空
    private String name;
    @Column(nullable = false)
    private Date createDate;
    private String tttt;
    @Transient//忽略字段，将不会自动创建数据库字段
    private String tttt2;
}
```
Dao
```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {
    User findByName(String name);
}
```
## 开启定时任务
***
在启动类上加上`@EnableScheduling`开启定时任务  
添加定时任务类
```java
@Component
public class SchedulerTask {
    private int count = 0;

    @Scheduled(cron="*/6 * * * * ?")
    private void proecee() {
        System.out.println("this is scheduler task runing  " + (count++));
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 6000)
    public void reportCurrentTime() {
        System.out.println("现在时间：" + dateFormat.format(new Date()));
    }

}
```
定时任务分为两种，一种是使用cron表达式，一种是设置延迟时间
* @Scheduled(fixedRate = 6000) ：上一次开始执行时间点之后6秒再执行
* @Scheduled(fixedDelay = 6000) ：上一次执行完毕时间点之后6秒再执行
* @Scheduled(initialDelay=1000, fixedRate=6000) ：第一次延迟1秒后执行，之后按 fixedRate 的规则每6秒执行一次