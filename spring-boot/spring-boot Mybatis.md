 Mybatis
===
配置Mybatis`implementation 'mysql:mysql-connector-java'`、`implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:2.1.1'`
## 无配置文件注解版
***
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/spring-data
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
mybatis:
  type-aliases-package: com.demo.springgradleproject.entity #实体类路径
```
在`@SpringBootApplication`类上配置mapper文件夹路径`@MapperScan("com.demo.springgradleproject.mapper")`或者给每个mapper类添加`@Mapper`

设置`mapper`
```java
public interface UserInfoMapper {
    @Select("SELECT * FROM users")
    @Results({
            @Result(property = "userSex", column = "user_sex", javaType = UserSexEnum.class),
            @Result(property = "nickName", column = "nick_name")
    })
    List<UserEntity> getAll();

    @Select("SELECT * FROM users WHERE id = #{id}")
    @Results({
            @Result(property = "userSex", column = "user_sex", javaType = UserSexEnum.class),
            @Result(property = "nickName", column = "nick_name")
    })
    UserEntity getOne(Long id);

    @Insert("INSERT INTO users(userName,passWord,user_sex) VALUES(#{userName}, #{passWord}, #{userSex})")
    void insert(UserEntity user);

    @Update("UPDATE users SET userName=#{userName},nick_name=#{nickName} WHERE id =#{id}")
    void update(UserEntity user);

    @Delete("DELETE FROM users WHERE id =#{id}")
    void delete(Long id);
}
```
* @Select 是查询类的注解，所有的查询均使用这个
* @Result 修饰返回的结果集，关联实体类属性和数据库字段一一对应，如果实体类属性和数据库属性名保持一致，就不需要这个属性来修饰。
* @Insert 插入数据库使用，直接传入实体类会自动解析属性到对应的值
* @Update 负责修改，也可以直接传入对象
* @Delete 负责删除

**注意，使用#符号和$符号的不同**
* `#`表示创建一个准备好的语句` select * from teacher where name = ?;`
* `$`表示创建一个内联语句`select * from teacher where name = 'someName';`
* `#`有预编译语句，可以放置sql注入
* 能用`#`就不用`$`，`$`主要用于order by

使用`@Autowired(required = false)`来添加Mapper，如果不添加`required = false`会提示`Could not Autowired`，不影响运行但会有警告提示

## XML版
***
* 添加实体类
* 添加Mapper
  ```java
  @Mapper
  @Repository
  public interface UserMapper {
      List<User> queryUserList();
      User queryUserById(int id);
      int addUser(User user);
      int updateUser(User user);
      int deleteUser(int id);
  }
  ```
* 在`resources`目录下添加mapper的xml
  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
      PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
      "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <mapper namespace="com.example.mapper.UserMapper">
      <select id="queryUserList" resultType="User">
          select * from user
      </select>
      <select id="queryUserById" resultType="User">
          select * from user
      </select>
      <insert id="addUser" parameterType="User">
          insert  into user(name,pwd) values (#{name},#{pwd})
      </insert>
      <update id="updateUser" parameterType="User">
          update user set name=#{name} ,pwd =#{pwd} where id= #{id}
      </update>
      <delete id="deleteUser" parameterType="int">
          delete from user where id= #{id}
      </delete>
  </mapper>
  ```
* yaml中添加配置
  ```yaml
  mybatis:
    mapper-locations: classpath:mybatis/mapper/*.xml
    type-aliases-package: com.example.pojo
  ```
  
## 极简XML版
***
配置文件额外添加
```yaml
mybatis:
  config-location: classpath:mybatis/mybatis-config.xml
  mapper-locations: classpath:mybatis/mapper/*.xml
```
**`classpath`是指`src/main/resources`目录**

## mybatis-config.xml
***
https://mybatis.org/mybatis-3/zh/configuration.html
```xml
<?xml version="1.0" encoding="utf-8" ?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <typeAliases>
        <typeAlias alias="Integer" type="java.lang.Integer"/>
        <typeAlias alias="Long" type="java.lang.Long"/>
        <typeAlias alias="HashMap" type="java.util.HashMap"/>
        <typeAlias alias="LinkedHashMap" type="java.util.LinkedHashMap"/>
        <typeAlias alias="ArrayList" type="java.util.ArrayList"/>
        <typeAlias alias="LinkedList" type="java.util.LinkedList"/>
    </typeAliases>
</configuration>
```
mapper https://mybatis.org/mybatis-3/zh/sqlmap-xml.html
```xml
<mapper namespace="com.neo.mapper.UserMapper" >
    <resultMap id="BaseResultMap" type="com.neo.entity.UserEntity" >
        <id column="id" property="id" jdbcType="BIGINT" />
        <result column="userName" property="userName" jdbcType="VARCHAR" />
        <result column="passWord" property="passWord" jdbcType="VARCHAR" />
        <result column="user_sex" property="userSex" javaType="com.neo.enums.UserSexEnum"/>
        <result column="nick_name" property="nickName" jdbcType="VARCHAR" />
    </resultMap>
    
    <sql id="Base_Column_List" >
        id, userName, passWord, user_sex, nick_name
    </sql>

    <select id="getAll" resultMap="BaseResultMap"  >
       SELECT 
       <include refid="Base_Column_List" />
	   FROM users
    </select>
    <select id="getOne" parameterType="java.lang.Long" resultMap="BaseResultMap" >
        SELECT 
       <include refid="Base_Column_List" />
	   FROM users
	   WHERE id = #{id}
    </select>
    <insert id="insert" parameterType="com.neo.entity.UserEntity" >
       INSERT INTO 
       		users
       		(userName,passWord,user_sex) 
       	VALUES
       		(#{userName}, #{passWord}, #{userSex})
    </insert>    
    <update id="update" parameterType="com.neo.entity.UserEntity" >
       UPDATE 
       		users 
       SET 
       	<if test="userName != null">userName = #{userName},</if>
       	<if test="passWord != null">passWord = #{passWord},</if>
       	nick_name = #{nickName}
       WHERE 
       		id = #{id}
    </update>
    
    <delete id="delete" parameterType="java.lang.Long" >
       DELETE FROM
       		 users 
       WHERE 
       		 id =#{id}
    </delete>
</mapper>
```
* namespace是Mapper文件完整路径
* resultMap中type是实体类完整路径
* colume是数据库列名，
* property是实体类属性名
* jdbcType是JDBC 类型`BIT`、`FLOAT`、`CHAR`、`TIMESTAMP`、`OTHER`、`UNDEFINED`、`TINYINT`、`REAL`、`VARCHAR`、`BINARY`、`BLOB`、`NVARCHAR`、`SMALLINT`、`DOUBLE`、`LONGVARCHAR`、`VARBINARY`、`CLOB`、`NCHAR`、`INTEGER`、`NUMERIC`、`DATE`、`LONGVARBINARY`、`BOOLEAN`、`NCLOB`、`BIGINT`、`DECIMAL`、`TIME`、`NULL`、`CURSOR`、`ARRAY`
* javaType是一个 Java 类的完全限定名，或一个类型别名 

添加Mapper代码
```java
public interface UserMapper {
	
	List<UserEntity> getAll();
	
	UserEntity getOne(Long id);

	void insert(UserEntity user);

	void update(UserEntity user);

	void delete(Long id);

}
```
使用方法和注解版一样