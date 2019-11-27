#### jpa
**jpa是一种规范不是产品**，注意使用不同的表结构引擎，默认的`MyISAM`不支持事务、外键，`InnoDB`支持事务、外键
```yaml
spring:
  jpa:
    database-platform: org.hibernate.dialect.MySQL5InnoDBDialect #切换表引擎
```

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
        userDao.getOne();//返回hibernate代理对象，里面没有值
        userDao.saveAndFlush();//保存或修改并返回更新后的对象
        ```
        `deleteAll`会先查出所有记录然后一条条删除，最后提交事务，`deleteAllInBatch`会直接删除一条sql语句，`deleteInBatch`会根据传入的参数拼接成一条语句执行
        ```java
        if (userDao.existsById(id)) {
            Optional<User> optional = userDao.findById(id);//通过ID查询
            if (optional.isPresent()) {
                User user = optional.get();
                user.name = random;
                userDao.saveAndFlush(user);
            }

            User u1 = new User();
            u1.name2="xx-524672933";
            Example<User> example = Example.of(u1);
            Optional<User> optional2 = userDao.findOne(example);
            if (optional2.isPresent()) {
                User user = optional2.get();
                user.name = random;
                userDao.saveAndFlush(user);
            }
        }
        ```

* 自定义查询，jpa可以根据方法名称自动生成SQL，查询语句规则为`前缀+全局修饰+By+实体的属性名称+限定词+连接词+ …(其它实体属性)+OrderBy+排序属性+排序方向`，前缀为`find`、`read`、`get`，全局修饰符`Distinct`（去除重复的结果）、`Top`(前几个结果)、`First`(第一个结果)，不是必填项

| 关键字            | 例子                                    | 语句                                                           |
| ----------------- | --------------------------------------- | -------------------------------------------------------------- |
| And               | findByLastnameAndFirstname              | … where x.lastname = ?1 and x.firstname = ?2                   |
| Or                | findByLastnameOrFirstname               | … where x.lastname = ?1 or x.firstname = ?2                    |
| Is,Equals         | findByFirstnameIs,findByFirstnameEquals | … where x.firstname = ?1                                       |
| Between           | findByStartDateBetween                  | … where x.startDate between ?1 and ?2                          |
| LessThan          | findByAgeLessThan                       | … where x.age < ?1                                             |
| LessThanEqual     | findByAgeLessThanEqual                  | … where x.age <= ?1                                            |
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

当需要使用分页查询时只要传入`Pageable`就可以，里面有页码、每页条数、排序方式  
当需要查询前一个或几个可以使用以下类似语句或者使用`Pageable`
```java
    //获取第一个
    User findFirstByOrderByIdAsc();
    User findTopByOrderByIdAsc();
    //前两个
    List<User> findTop2ByOrderByIdAsc();
    List<User> findFirst2ByOrderByIdAsc();
```
在实体类上使用`@NamedQuery`来自定义查询语句，如果有多个可以使用`@NamedQueries`
```java
@Entity
@NamedQuery(name="User.get1",query="from User o where o.id >= ?1")
public class User implements Serializable {}
```
然后在`JpaRepository`Dao子类中就可以直接使用定义的查询语句
* 自定义sql语句查询，当使用jpa生成的语句不足实现要求时可以使用自定义sql语句来做操作  
首先添加`Qquery`注解，输入sql语句，如果操作涉及删除修改还需有添加`@Modifying`，如果还用到了事务可以添加`@Transactional`，设置例如超时时间等，修改和删除会返回受影响行数，必须添加事务
```java
@Modifying
@Query("update User u set u.userName = ?1 where u.id = ?2")
int modifyByIdAndUserId(String  userName, Long id);
	
@Transactional
@Modifying
@Query("delete from User where id = ?1")
void deleteByUserId(Long id);
  
@Transactional(timeout = 10)
@Query("from User u where u.emailAddress = ?1")
User findByEmailAddress(String emailAddress);
```
**上面NamedQuery或Query用到的语句是JPQL不是SQL**
如果要设置为本地查询可以使用NativeQuery
```java
@Query(value="select * from tbl_user where name like %?1" ,nativeQuery=true)
public List<UserModel> findByUuidOrAge(String name);
```
如果要使用命名化参数使用`@Param`
```java

@Query(value="from UserModel o where o.name like %:nn")
public List<UserModel> findByUuidOrAge(@Param("nn") String name);
```
* 外键设置
  使用`@OneToOne`/`@OneToMany`/`@ManyToOne`/`@ManyToMany`，来设置外键是一对一、一对多、多对一、多对多，添加`fetch`表示加载模式，FetchType.EAGER表示积极加载、FetchType.LAZY表示懒加载，`cascade`表示为级联模式
  * `@OneToOne`：可以单向绑定，也可双向绑定  
    * 单向绑定，只在有外键的实体上加上注解，在`@OneToOne`上还可以加上`(targetEntity = User1o1Unidirectional2.class)`指定类，但一般可以不用。`@JoinColumn`可以加上`referencedColumnName = "id"`来指定绑定的外键哪个外键列，默认是被绑定表的主键，默认在做持久化时是没有关联的，必须手动调用两次保存分别保存对象，当给`@OneToOne`加上`cascade = CascadeType.PERSIST`则表示给关联表添加权限。`CascadeType.REMOVE`表示给关联表删除权限
        ```java
        @OneToOne()
        @JoinColumn(name = "u2")//或者使用@MapsId，自动生成列名“表名_主键名”，且将该字段设置为主键
        public User1o1Unidirectional2 user;
        ```
    * 双向绑定，在被绑定实体上加上`@OneToOne`并指定`mappedBy`为有外键实体对象变量名，建议在被绑定实体上加上`@JsonIgnore`，以防止转换json时无限循环的问题
        ```java
        @OneToOne(mappedBy = "user")
        @JsonIgnore
        public User1o1TwoWay user;
        ```
  * `@OneToMany`：一对多多对一一般成对使用，一对多为主表，多对一为从表
     ```java
    @JoinColumn(name = "oneToMany")
    @OneToMany
    private List<UserOneToMany> oneToMany;
    ```
  * `@ManyToOne`   
    ```java
    @ManyToOne
    @JoinColumn(name = "user_id")
    public UserOne2Many user;
    ```
  * `@ManyToMany`：多对多会使用一张关系表，默认由关系维护表建立默认名称为`主表名_多对多变量名`，关系表只有两个字段，分别为`从表多对多变量名_主键名`和`主表多对多变量名_主键名`，使用`@JoinTable(name = "t1")`可以指定关系表名称，`joinColumns = @JoinColumn(name = "u11111")`指定关系表中主表字段，`inverseJoinColumns = @JoinColumn(name = "u333333")`指定关系表中从表字段，当删除主表信息时会自动删除关系表里面的数据，删除从表必须先删除关系表里面的数据
    主表
    ```java
    @ManyToMany
    @JoinTable(name = "t1",
            joinColumns = @JoinColumn(name = "u11111"),
            inverseJoinColumns = @JoinColumn(name = "u333333")
    )
    public List<UserMany2Many2> u2;
    ```
    从表
    ```java
    @ManyToMany(mappedBy = "u2")
    public List<UserMany2Many> u3;
    ```
    添加时先添加从表信息，然后添加把添加后的从表对象放到主表对象中，然后再保存主表
* 双向绑定循环引用问题  
  默认使用双向绑定会出现循环引用问题，转换json时会无线嵌套，spring-boot默认使用`jackson`可以使用以下方法解决json无线嵌套问题
  * 在两个互相绑定的实体类中互相引用的变量上加上`@JsonIgnoreProperties`并指定忽略的json字段名，两个类都需要加上
  * 在互相绑定的实体类上添加`@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)`，在实体类的父类上效果一样，使用这个会在json里增加在一个默认名称为`@id`的字段，在多对一情况下会出现返回json错误的情况，请勿使用该方法防止循环引用
  * 在主表变量上加上`@JsonManagedReference`，从表变量上加上`@JsonBackReference`，这样主表就能查到从表数据，从表查不到主表数据，也可以反过来
* 延迟加载提示`No serializer found for class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS)`      
  使用外键获取数据时使用`FetchType.LAZY`会默认会有错误，给延迟加载变量加上`@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"}, ignoreUnknown = true)`可以避免这个错误
* 多表查询，多表查询 Spring Boot Jpa 中有两种实现方式，第一种是利用 Hibernate 的级联查询来实现，第二种是创建一个结果集的接口来接收连表查询后的结果，这里主要第二种方式。
  * 首先定义结果集，结果集有两种方式类和接口
    1. 类
       ```java
       public class ViewInfo implements Serializable {

            private UserInfo userInfo;
            private Address address;

            public ViewInfo(UserInfo userInfo) {
                Address address = new Address();
                this.userInfo = userInfo;
                this.address = address;
            }

            public ViewInfo(Address address) {
                UserInfo userInfo = new UserInfo();
                this.userInfo = userInfo;
                this.address = address;
            }

            public ViewInfo(UserInfo userInfo, Address address) {
                this.userInfo = userInfo;
                this.address = address;
            }
            // getter setter
        }
       ``` 
    2. 接口
       ```java
       public interface ViewInfo2 {
           UserInfo getUserInfo();
           Address getAddress();
           default Long getUserId() {
               return getUserInfo() == null ? null : getUserInfo().getUserId();
           }
       }
       ```
   * 设置查询语句
       1. 类
          ```java
          @Query(value = "SELECT new com.demo.springgradleproject.entity.ViewInfo(u, a) FROM UserInfo u, Address a WHERE u.addressId = a.addressId")
          List<ViewInfo> findViewInfo();
          ``` 
       2. 接口
          ```java
          @Query(value = "SELECT u as UserInfo,a as Address FROM UserInfo u, Address a WHERE u.addressId = a.addressId")
          List<ViewInfo2> findViewInfo2();
          ```

使用枚举 

使用枚举的时候，我们希望数据库中存储的是枚举对应的 String 类型，而不是枚举的索引值，需要在属性上面添加 `@Enumerated(EnumType.STRING)`注解
```java
@Enumerated(EnumType.STRING) 
@Column(nullable = true)
private UserType type;
```
##### 多数据库支持
* 同源数据库支持
  日常项目中因为使用的分布式开发模式，不同的服务有不同的数据源，常常需要在一个项目中使用多个数据源，因此需要配置 Spring Boot Jpa 对多数据源的使用，一般分一下为三步：
  1. 配置多数据源
  2. 不同源的实体类放入不同包路径
  3. 声明不同的包路径下使用不同的数据源、事务支持