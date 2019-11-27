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