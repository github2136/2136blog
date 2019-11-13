# spring-boot
* `IDEAIntelliJ IDEA 2018.3.5 (Ultimate Edition)`
* `mysql 8.0.16`
* `spring-boot 2.1.5.RELEASE`
* `jdk1.8`

spring-boot项目初始化使用IDEA，File -> New -> Project -> Spring Initializr -> 选择Type(Gradle Project)/Language/Packaging/Java Version等信息 -> 添加 Spring Boot DevTools / Spring Web -> Web创建项目基本文件。
#### 开启热部署
* 添加`DevTools`
* File -> Settings -> Build,Execution,Deplyment -> Compiler，勾选 Build project automatically
* Shift+Ctrl+Alt+/ -> Registry -> 勾选 compiler.automake.allow.when.app.running

#### 编写controller
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
#### 单元测试
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
#### 自定义过滤器
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
#### 自定义属性
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
#### 注入方法
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
#### log配置
设置日志输出地址和输出级别
```yaml
logging:
  level:
    root: info
  path: user/local/log
```
#### 数据库操作
添加JPA`implementation 'org.springframework.boot:spring-boot-starter-data-jpa'`、`implementation 'mysql:mysql-connector-java'`  
连接设置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/spring-data
    username: root
    password: root
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
##### 添加实体类和Dao
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
#### Redis配置
引入Redis`implementation 'org.springframework.boot:spring-boot-starter-data-redis'`/`implementation 'org.apache.commons:commons-pool2'`，在spring
```yaml
spring:
  redis:
    # Redis数据库索引（默认为0）
    database: 0
    # Redis服务器地址
    host: localhost
    # Redis服务器连接端口
    port: 6379
    # Redis服务器连接密码（默认为空）
    password:
    lettuce:
      pool:
        # 连接池最大连接数（使用负值表示没有限制） 默认 8
        max-active: 8
        # 连接池最大阻塞等待时间（使用负值表示没有限制） 默认 -1
        max-wait: -1
        # 连接池中的最大空闲连接 默认 8
        max-idle: 8
        # 连接池中的最小空闲连接 默认 0
        min-idle: 0
```

```java
public class TestRedis{
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @Autowired
    private RedisTemplate redisTemplate;

    public void test(){
        // 设置值
        stringRedisTemplate.opsForValue().set("key", "111");
        // 获取值
        stringRedisTemplate.opsForValue().get("key");
        // 删除值
        stringRedisTemplate.delete("key");

        stringRedisTemplate.opsForHash();
        stringRedisTemplate.opsForList();
        stringRedisTemplate.opsForSet();
        stringRedisTemplate.opsForZSet();
        stringRedisTemplate.opsForHyperLogLog();
        stringRedisTemplate.opsForGeo();
        
        // User必须实现Serializable接口
        ValueOperations<String, User> operations = redisTemplate.opsForValue();
        // 设置值
        operations.set("user", user);
        // 设置超时时间
        operations.set("user_timeout", user, 1, TimeUnit.SECONDS);
        // 判断值是否存在
        redisTemplate.hasKey("user_timeout");
    }
}
```

#### 简单Redis缓存设置
添加`Redis`引用，设置`Redis`配置，添加类
```java
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    // 设置key生成规则，默认Key SimpleKey [参数值....]
    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName())
                    .append(".")
                    .append(method.getName());
                for (Object obj : params) {
                    sb.append("_")
                        .append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

    @Override
    public CacheManager cacheManager() {
        return null;
    }

    @Override
    public CacheResolver cacheResolver() {
        return null;
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return null;
    }
}
```
给`@RestController`中的`@RequestMapping("/")`方法增加`@Cacheable`注解  
* `Cacheable`标记在一个类或方法上，标记在类上表示该类下所有方法都支持缓存

| 参数          | 说明                                           |                                                                                                                                                   |
| ------------- | ---------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------- |
| value         | 缓存名称，可以有多个，必填项                   | value = "v1"、value = {"v1", "v2"}                                                                                                                |
| cacheNames    | 与value相同                                    |                                                                                                                                                   |
| key           | 缓存key名称，可以为空，使用SpEL表达式          | key="#id"使用id参数、key="#p0"使用第一个参数、key="#user.id"使用指定值、key="#p0.id"使用第一个参数的值，key = "'name'"使用单引号可以添加固定的key |
| keyGenerator  | 自定义key生成器可以为空，为空时使用默认生成器  | keyGenerator = "myKeyGenerator"                                                                                                                   |
| cacheManager  | 自定义缓存管理，与`keyGenerator`添加方法相同   | cacheManager = "myCacheManager"                                                                                                                   |
| cacheResolver | 自定义缓存解析，与`keyGenerator`添加方法相同   | cacheResolver = "myCacheResolver"                                                                                                                 |
| condition     | 缓存条件，使用SpEL表达式，返回true才会缓存     | condition = "#userName.length()>2"长度大于2才会缓存                                                                                               |
| unless        | 缓存条件与condition相反                        | unless = "#userName.length()>2"长度大于2不会会缓存                                                                                                |
| sync          | 是否同步，当这个属性是`true`时不能使用`unless` | sync = true                                                                                                                                       |

SpEL表达式

| 名称          | 位置               | 描述                              | 示例                 |
| ------------- | ------------------ | --------------------------------- | -------------------- |
| methodName    | root object        | 被调用的方法名称                  | #root.methodName     |
| Method        | root object        | 被调用的方法                      | #root.method.name    |
| Target        | root object        | 当前被调用的目标对象              | #root.target         |
| targetClass   | root object        | 当前被调用的目标对象类            | #root.targetClass    |
| args          | root object        | 被调用方法的参数列表              | #root.args[0]        |
| caches        | root object        | 调用的缓存列表                    | #root.caches[0].name |
| argument name | evaluation context | 方法的参数名称可以直接使用#参数名 | #p0,#a0等等          |
| result        | evaluation context | 执行方法后的返回值                | #result              |

配置key生成器
```java
@Configuration
public class MyCacheConfig {
    @Bean("myKeyGenerator")
    public KeyGenerator keyGenerator(){
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                return method.getName()+"["+ Arrays.asList(params).toString()+"]";
            }
        };
    }
}
.....
@Cacheable(cacheNames = {"emp"},keyGenerator = "myKeyGenerator")
```
* `CachePut`与`Cacheable`参数相同，该注解每次都会获取新的记录然后存储到缓存中
* `CacheEvict`与`Cacheable`参数类似
    * `allEntries`表示清除指定`value`的缓存，忽略`key`，可以大批量删除
    * `beforeInvocation`表示在方法执行前清除缓存，默认在方法执行后清除缓存
* `Caching`可以用来组合不同的cacheable、put和evict
* `CacheConfig`可以在类上统一设置`cacheNames`/`keyGenerator`/`cacheManager`/`cacheResolver`
#### Thymeleaf
Thymeleaf特点
1. Thymeleaf 在有网络和无网络的环境下皆可运行，即它可以让美工在浏览器查看页面的静态效果，也可以让程序员在服务器查看带数据的动态页面效果。这是由于它支持 html 原型，然后在 html 标签里增加额外的属性来达到模板+数据的展示方式。浏览器解释 html 时会忽略未定义的标签属性，所以 Thymeleaf 的模板可以静态地运行；当有数据返回到页面时，Thymeleaf 标签会动态地替换掉静态内容，使页面动态显示。
1. Thymeleaf 开箱即用的特性。它提供标准和 Spring 标准两种方言，可以直接套用模板实现 JSTL、 OGNL表达式效果，避免每天套模板、改 Jstl、改标签的困扰。同时开发人员也可以扩展和创建自定义的方言。
1. Thymeleaf 提供 Spring 标准方言和一个与 SpringMVC 完美集成的可选模块，可以快速的实现表单绑定、属性编辑器、国际化等功能。

Thymeleaf标准表达式分为四类
1. 变量表达式
1. 选择或星号表达式
1. 文字国际化表达式
1. URL 表达式

#### jpa
**jpa是一种规范不是产品**

基本查询分为两种，一种是Spring Data默认实现了的，一种是根据查询方法自动解析SQL
* 默认实现方法
    * 首先使用`interface`继承`JpaRepository`，`public interface UserDao extends JpaRepository<User, Long> {}`
    * 使用默认方法
        ```java
        userDao.findAll();
        userDao.count();
        userDao.delete();
        userDao.deleteAll();
        userDao.exists();
        userDao.save();
        userDao.getOne();
        ```
* 自定义查询，jpa可以根据方法名称自动生成SQL，主要的语法是`findXXBy`,`readAXXBy`,`queryXXBy`,`countXXBy`,`getXXBy`后面跟属性名称

| 关键字            | 例子                                    | 语句                                                           |
| ----------------- | --------------------------------------- | -------------------------------------------------------------- |
| And               | findByLastnameAndFirstname              | … where x.lastname = ?1 and x.firstname = ?2                   |
| Or                | findByLastnameOrFirstname               | … where x.lastname = ?1 or x.firstname = ?2                    |
| Is,Equals         | findByFirstnameIs,findByFirstnameEquals | … where x.firstname = ?1                                       |
| Between           | findByStartDateBetween                  | … where x.startDate between ?1 and ?2                          |
| LessThan          | findByAgeLessThan                       | … where x.age < ?1                                             |
| LessThanEqual     | findByAgeLessThanEqual                  | … where x.age ⇐ ?1                                             |
| GreaterThan       | findByAgeGreaterThan                    | … where x.age > ?1                                             |
| GreaterThanEqual  | findByAgeGreaterThanEqual               | … where x.age >= ?1                                            |
| After             | findByStartDateAfter                    | … where x.startDate > ?1                                       |
| Before            | findByStartDateBefore                   | … where x.startDate < ?1                                       |
| IsNull            | findByAgeIsNull                         | … where x.age is null                                          |
| IsNotNull,NotNull | findByAge(Is)NotNull                    | … where x.age not null                                         |
| Like              | findByFirstnameLike                     | … where x.firstname like ?1                                    |
| NotLike           | findByFirstnameNotLike                  | … where x.firstname not like ?1                                |
| StartingWith      | findByFirstnameStartingWith             | … where x.firstname like ?1 (parameter bound with appended %)  |
| EndingWith        | findByFirstnameEndingWith               | … where x.firstname like ?1 (parameter bound with prepended %) |
| Containing        | findByFirstnameContaining               | … where x.firstname like ?1 (parameter bound wrapped in %)     |
| OrderBy           | findByAgeOrderByLastnameDesc            | … where x.age = ?1 order by x.lastname desc                    |
| Not               | findByLastnameNot                       | … where x.lastname <> ?1                                       |
| In                | findByAgeIn(Collection ages)            | … where x.age in ?1                                            |
| NotIn             | findByAgeNotIn(Collection age)          | … where x.age not in ?1                                        |
| TRUE              | findByActiveTrue()                      | … where x.active = true                                        |
| FALSE             | findByActiveFalse()                     | … where x.active = false                                       |
| IgnoreCase        | findByFirstnameIgnoreCase               | … where UPPER(x.firstame) = UPPER(?1)                          |