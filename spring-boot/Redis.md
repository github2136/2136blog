#### Redis
Redis是一种非关系型数据库，它通常被称为数据结构服务器，因为值（value）可以是 字符串(String), 哈希(Hash), 列表(list), 集合(sets)，有序集合(sorted sets)，位图（bitmap），地理信息（geo）等类型。
基本数据结构
* String: 字符串
* Hash: 散列
* List: 列表
* Set: 集合
* Sorted Set: 有序集合
* Bitmap：位图（版本>=2.2.0）
* Geo：地理信息（版本>=3.2.0）

Redis 与其他 key - value 缓存产品有以下三个特点：
* Redis支持数据的持久化，可以将内存中的数据保存在磁盘中，重启的时候可以再次加载进行使用。
* Redis不仅仅支持简单的key-value类型的数据，同时还提供list，set，zset，hash等数据结构的存储。
* Redis支持数据的备份，即master-slave模式的数据备份。

Redis 优势
* 性能极高 – Redis能读的速度是110000次/s,写的速度是81000次/s 。
* 丰富的数据类型 – Redis支持二进制案例的 Strings, Lists, Hashes, Sets 及 Ordered Sets 数据类型操作。
* 原子 – Redis的所有操作都是原子性的，意思就是要么成功执行要么失败完全不执行。单个操作是原子性的。多个操作也支持事务，即原子性，通过MULTI和EXEC指令包起来。
* 丰富的特性 – Redis还支持 publish/subscribe, 通知, key 过期等等特性。

Redis与其他key-value存储有什么不同？
* Redis有着更为复杂的数据结构并且提供对他们的原子性操作，这是一个不同于其他数据库的进化路径。Redis的数据类型都是基于基本数据结构的同时对程序员透明，无需进行额外的抽象。

* Redis运行在内存中但是可以持久化到磁盘，所以在对不同数据集进行高速读写时需要权衡内存，因为数据量不能大于硬件内存。在内存数据库方面的另一个优点是，相比在磁盘上相同的复杂的数据结构，在内存中操作起来非常简单，这样Redis可以做很多内部复杂性很强的事情。同时，在磁盘格式方面他们是紧凑的以追加的方式产生的，因为他们并不需要进行随机访问。
#### Redis安装
Windows下载地址：https://github.com/MSOpenTech/redis/releases  
下载解压后运行`redis-server.exe`或者用命令行执行`redis-server.exe redis.windows.conf`按配置启动`服务端`，然后开启一个新的命令行界面，切换到`redis`目录运行`redis-cli.exe -h 127.0.0.1 -p 6379`打开`客户端`，在启动`redis-cli.exe`命令后面加上`--raw`可以避免中文乱码问题  
* 设置值`set myKey abc`
* 取值`get myKey`
##### Redis配置
`Redis`的Windows版配置在安装目录的`redis.windows.conf`里，可以通过config命令查看或设置配置项
查看log级别
```
redis 127.0.0.1:6379> CONFIG GET loglevel

1) "loglevel"
2) "notice"
```
使用*获取所有配置`redis 127.0.0.1:6379> CONFIG GET *`  
可以通过修改`redis.windows.conf`或使用`config set`命令来修改配置
```
redis 127.0.0.1:6379> CONFIG SET loglevel "notice"
OK
redis 127.0.0.1:6379> CONFIG GET loglevel

1) "loglevel"
2) "notice"
```
参数说明

|配置项|说明|
|-|-|
|daemonize no|Redis 默认不是以守护进程的方式运行，可以通过该配置项修改，使用 yes 启用守护进程（Windows 不支持守护线程的配置为 no ）|	
|pidfile /var/run/redis.pid|当 Redis 以守护进程方式运行时，Redis 默认会把 pid 写入 /var/run/redis.pid 文件，可以通过 pidfile 指定|
|port 6379|指定 Redis 监听端口，默认端口为 6379，作者在自己的一篇博文中解释了为什么选用 6379 作为默认端口，因为 6379 在手机按键上 MERZ 对应的号码，而 MERZ 取自意大利歌女 Alessia Merz 的名字|
|bind 127.0.0.1|绑定的主机地址|
|timeout 300|当客户端闲置多长时间后关闭连接，如果指定为 0，表示关闭该功能|
|loglevel notice|指定日志记录级别，Redis 总共支持四个级别：debug、verbose、notice、warning，默认为 notice|
|logfile stdout|日志记录方式，默认为标准输出，如果配置 Redis 为守护进程方式运行，而这里又配置为日志记录方式为标准输出，则日志将会发送给 /dev/null|
|databases 16|设置数据库的数量，默认数据库为0，可以使用SELECT 命令在连接上指定数据库id|
|save &lt;seconds&gt;&lt;changes&gt;Redis 默认配置文件中提供了三个条件：<br/>`save 900 1`<br/>`save 300 10`<br/>`save 60 10000`<br/>分别表示 900 秒（15 分钟）内有 1 个更改,300 秒（5 分钟）内有 10 个更改以及60 秒内有 10000 个更改。|指定在多长时间内，有多少次更新操作，就将数据同步到数据文件，可以多个条件配合|
|rdbcompression yes|指定存储至本地数据库时是否压缩数据，默认为 yes，Redis 采用 LZF 压缩，如果为了节省 CPU 时间，可以关闭该选项，但会导致数据库文件变的巨大|
|dbfilename dump.rdb|指定本地数据库文件名，默认值为 dump.rdb|
|dir ./|指定本地数据库存放目录|
|slaveof &lt;masterip&gt; &lt;masterport&gt;|设置当本机为 slav 服务时，设置 master 服务的 IP 地址及端口，在 Redis 启动时，它会自动从 master 进行数据同步|
|masterauth &lt;aster-password&gt;|当 master 服务设置了密码保护时，slav 服务连接 master 的密码|
|requirepass foobared|设置 Redis 连接密码，如果配置了连接密码，客户端在连接 Redis 时需要通过 AUTH &lt;password&gt; 命令提供密码，默认关闭|
|maxclients 128|设置同一时间最大客户端连接数，默认无限制，Redis 可以同时打开的客户端连接数为 Redis 进程可以打开的最大文件描述符数，如果设置 maxclients 0，表示不作限制。当客户端连接数到达限制时，Redis 会关闭新的连接并向客户端返回 max number of clients reached 错误信息|
|maxmemory &lt;bytes&gt;|指定 Redis 最大内存限制，Redis 在启动时会把数据加载到内存中，达到最大内存后，Redis 会先尝试清除已到期或即将到期的 Key，当此方法处理 后，仍然到达最大内存设置，将无法再进行写入操作，但仍然可以进行读取操作。Redis 新的 vm 机制，会把 Key 存放内存，Value 会存放在 swap 区|
|appendonly no|指定是否在每次更新操作后进行日志记录，Redis 在默认情况下是异步的把数据写入磁盘，如果不开启，可能会在断电时导致一段时间内的数据丢失。因为 redis 本身同步数据文件是按上面 save 条件来同步的，所以有的数据会在一段时间内只存在于内存中。默认为 no|
|appendfilename appendonly.aof|指定更新日志文件名，默认为 appendonly.aof|
|appendfsync everysec|指定更新日志条件，共有 3 个可选值：`no`：表示等操作系统进行数据缓存同步到磁盘（快）`always`：表示每次更新操作后手动调用 fsync() 将数据写到磁盘（慢，安全）`everysec`：表示每秒同步一次（折中，默认值）|
|vm-enabled no|指定是否启用虚拟内存机制，默认值为 no，简单的介绍一下，VM 机制将数据分页存放，由 Redis 将访问量较少的页即冷数据 swap 到磁盘上，访问多的页面由磁盘自动换出到内存中（在后面的文章我会仔细分析 Redis 的 VM 机制）|
|vm-swap-file /tmp/redis.swap|虚拟内存文件路径，默认值为 /tmp/redis.swap，不可多个 Redis 实例共享|
|vm-max-memory 0|将所有大于 vm-max-memory 的数据存入虚拟内存，无论 vm-max-memory 设置多小，所有索引数据都是内存存储的(Redis 的索引数据 就是 keys)，也就是说，当 vm-max-memory 设置为 0 的时候，其实是所有 value 都存在于磁盘。默认值为 0|
|vm-page-size 32|Redis swap 文件分成了很多的 page，一个对象可以保存在多个 page 上面，但一个 page 上不能被多个对象共享，vm-page-size 是要根据存储的 数据大小来设定的，作者建议如果存储很多小对象，page 大小最好设置为 32 或者 64bytes；如果存储很大大对象，则可以使用更大的 page，如果不确定，就使用默认值|
|vm-pages 134217728|设置 swap 文件中的 page 数量，由于页表（一种表示页面空闲或使用的 bitmap）是在放在内存中的，，在磁盘上每 8 个 pages 将消耗 1byte 的内存。|
|vm-max-threads 4|设置访问swap文件的线程数,最好不要超过机器的核数,如果设置为0,那么所有对swap文件的操作都是串行的，可能会造成比较长时间的延迟。默认值为4|
|glueoutputbuf yes|设置在向客户端应答时，是否把较小的包合并为一个包发送，默认为开启|
|hash-max-zipmap-entries 64<br/>hash-max-zipmap-value 512|指定在超过一定的数量或者最大的元素超过某一临界值时，采用一种特殊的哈希算法|
|activerehashing yes|指定是否激活重置哈希，默认为开启（后面在介绍 Redis 的哈希算法时具体介绍）|
|include /path/to/local.conf|指定包含其它的配置文件，可以在同一主机上多个Redis实例之间使用同一份配置文件，而同时各个实例又拥有自己的特定配置文件|
##### Redis数据类型
Redis支持五种数据类型：String（字符串），Hash（哈希），List（列表），Set（集合），Zset（有序集合）
* String是Redis基本数据类型，一个key对应一个value。String类型是二进制安全的，意思是String可以包含任何数据，比如jpg图片或者序列号对象，最大能存512MB。
    > 添加：**set** key member  
    > 获取：**get** key

    ```
    redis 127.0.0.1:6379> set runoob "菜鸟教程"
    OK
    redis 127.0.0.1:6379> get runoob
    "菜鸟教程"
    ```
* Hash是一个键值对集合(key=>value)，hash是一个String类型的field和value的映射表，适合存储对象。使用`hmset`设置值，`hget`获取数据，每个hash可以存储2<sup>32</sup>-1约42亿个键值对。
    > 添加：**hmset** key field_name1 value1 field_name2 value2  
    > 获取：**hget** key field_name
    ```
    redis 127.0.0.1:6379> hmset runoob1 field1 "Hello" field2 "World"
    "OK"
    redis 127.0.0.1:6379> hget runoob1 field1
    "Hello"
    redis 127.0.0.1:6379> hget runoob1 field2
    "World"
    ```
* List是简单的字符串列表，按照插入顺序排序，可以添加一个元素到表头（左边）或表尾（右边），使用`lpush`添加到表头，`rpush`添加到表尾，`lrange`获取指定范围的值，列表最多存储2<sup>32</sup>-1约42亿个数据
    > 添加：左边 **lpush** key value，右边 **rpush** key value  
    > 获取：**lrange** key range_start range_end
    ```
    127.0.0.1:6379> lpush runoob redis
    (integer) 1
    127.0.0.1:6379> lpush runoob mongodb
    (integer) 2
    127.0.0.1:6379> rpush runoob rabitmq
    (integer) 3
    127.0.0.1:6379> lrange runoob 0 10
    1) "mongodb"
    2) "redis"
    3) "rabitmq"
    ```
* Set是String类型的无序集合。集合是通过哈希表实现所以添加删除查找复杂的都是O(1)。使用`sadd`添加元素如果添加成功返回1，如果已经存在返回0，使用`smembers`查询集合，集合中不允许出现重复元素，集合最多存储2<sup>32</sup>-1约42亿个数据
    > 添加：**sadd** key value  
    > 获取：**smembers** key
    ```
    redis 127.0.0.1:6379> sadd runoob redis
    (integer) 1
    redis 127.0.0.1:6379> sadd runoob mongodb
    (integer) 1
    redis 127.0.0.1:6379> sadd runoob rabitmq
    (integer) 1
    redis 127.0.0.1:6379> sadd runoob rabitmq
    (integer) 0
    redis 127.0.0.1:6379> smembers runoob

    1) "redis"
    2) "rabitmq"
    3) "mongodb"
    ```
* Zset添加时会关联一个`double`类型的的分数，Redis正是通过分数来对集合进行从小到大排序。分数可以重复单成员不能重复。使用`zadd`添加元素，使用`zrangerbyscore`查询元素
    > 添加：**zadd** key score value  
    > 查询：**zrangebyscore** key range_start range_end
    ```
    redis 127.0.0.1:6379> zadd runoob 0 redis
    (integer) 1
    redis 127.0.0.1:6379> zadd runoob 0 mongodb
    (integer) 1
    redis 127.0.0.1:6379> zadd runoob 0 rabitmq
    (integer) 1
    redis 127.0.0.1:6379> zadd runoob 0 rabitmq
    (integer) 0
    redis 127.0.0.1:6379> > zrangebyscore runoob 0 1000
    1) "mongodb"
    2) "rabitmq"
    3) "redis"
    ```
##### Redis命令
打开`redis-cli.exe`会启动连接本地的客户端，使用`ping`命令来检测Redis服务是否开启
```
127.0.0.1:6379> ping
PONG
127.0.0.1:6379>
```
如果要连接远程服务器需要加上ip端口和密码`redis-cli.exe -h host -p port -a password`，密码可以在`redis.windows.conf`配置文件里面添加或者使用`config set `命令动态修改具体可以看上方**Redis的参数说明**
```
E:\redis-64.3.0.503>redis-cli.exe -h 127.0.0.1 -p 6379 -a password
127.0.0.1:6379> ping
PONG
127.0.0.1:6379>
```

|命令|说明|
|-|-|
|del key|该命令用于在 key 存在时删除 key。|
|dump key|序列化给定 key ，并返回被序列化的值。|
|exists key|检查给定 key 是否存在。|
|expire key seconds|为给定 key 设置过期时间，以秒计。|
|expireat key timestamp|EXPIREAT 的作用和 EXPIRE 类似，都用于为 key 设置过期时间。 不同在于 EXPIREAT 命令接受的时间参数是 unix 时间戳(unix timestamp)。|
|pexpire key milliseconds|设置 key 的过期时间以毫秒计。|
|pexpireat key milliseconds-timestamp|设置 key 过期时间的时间戳(unix timestamp) 以毫秒计|
|keys pattern|查找所有符合给定模式(pattern)的 key 。|
|move key db|将当前数据库的 key 移动到给定的数据库 db 当中。|
|persist key|移除 key 的过期时间，key 将持久保持。|
|pttl key|以毫秒为单位返回 key 的剩余的过期时间。|
|ttl key|以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。|
|randomkey|从当前数据库中随机返回一个 key 。|
|rename key newkey|修改 key 的名称|
|renamenx key newkey|仅当 newkey 不存在时，将 key 改名为 newkey 。|
|type key|返回 key 所储存的值的类型。|
更多命令：https://redis.io/commands
**以下命令如果使用到范围start end都表示从开始位置到结束位置（包括结束位置），例如getrange a 0 0则会返回第一个字符**
**使用scan迭代使用count参数可能出现返回结果多余count的情况**
* String常用命令

    |命令|说明|
    |-|-|
    |set key value|设置指定 key 的值|
    |get key|获取指定 key 的值。|
    |getrange key start end|返回 key 中字符串值的子字符|
    |getset key value|将给定 key 的值设为 value ，并返回 key 的旧值(old value)。|
    |mget key1 [key2..]|获取所有(一个或多个)给定 key 的值。|
    |setex key seconds value|将值 value 关联到 key ，并将 key 的过期时间设为 seconds (以秒为单位)。|
    |setnx key value|只有在 key 不存在时设置 key 的值。|
    |setrange key offset value|用 value 参数覆写给定 key 所储存的字符串值，从偏移量 offset 开始。|
    |strlen key|返回 key 所储存的字符串值的长度。|
    |mset key value [key value ...]|同时设置一个或多个 key-value 对。|
    |msetnx key value [key value ...] |同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在。|
    |psetex key milliseconds value|这个命令和 setex 命令相似，但它以毫秒为单位设置 key 的生存时间，而不是像 setex 命令那样，以秒为单位。|
    |incr key|将 key 中储存的数字值增一。|
    |incrby key increment|将 key 所储存的值加上给定的增量值（increment） 。|
    |incrbyfloat key increment|将 key 所储存的值加上给定的浮点增量值（increment） 。|
    |decr key|将 key 中储存的数字值减一。|
    |decrby key decrementkey |所储存的值减去给定的减量值（decrement） 。|
    |append key value|如果 key 已经存在并且是一个字符串， APPEND 命令将指定的 value 追加到该 key 原来值（value）的末尾。|
    |scan cursor [match pattern] [count count] |迭代所有key，返回值里面两个返回值，本次迭代出来的下标和本次迭代的key，迭代时可以做过滤。版本需要大于2.8.0|
* Hash常用命令

    |命令|说明|
    |-|-|
    |hdel key field1 [field2] |删除一个或多个哈希表字段|
    |hexists key field |查看哈希表 key 中，指定的字段是否存在。|
    |hget key field| 获取存储在哈希表中指定字段的值。|
    |hgetall key| 获取在哈希表中指定 key 的所有字段和值|
    |hincrby key field increment |为哈希表 key 中的指定字段的整数值加上增量 increment 。|
    |hincrbyfloat key field increment |为哈希表 key 中的指定字段的浮点数值加上增量 increment 。|
    |hkeys key |获取所有哈希表中的字段|
    |hlen key |获取哈希表中字段的数量|
    |hmget key field1 [field2] |获取所有给定字段的值|
    |hmset key field1 value1 [field2 value2 ]| 同时将多个 field-value (域-值)对设置到哈希表 key 中。|
    |hset key field value |将哈希表 key 中的字段 field 的值设为 value 。|
    |hsetnx key field value |只有在字段 field 不存在时，设置哈希表字段的值。|
    |hvals key |获取哈希表中所有值|
    |hscan key cursor [match pattern] [count count] |迭代哈希表中指定key的field|
* List常用命令

    |命令|说明|
    |-|-|
    |blpop key1 [key2 ] timeout |移出并获取列表的第一个元素，返回key和弹出的元素，如果列表没有元素会阻塞列表直到等待超时（秒）或发现可弹出元素为止。|
    |brpop key1 [key2 ] timeout |移出并获取列表的最后一个元素，返回key和弹出的元素，如果列表没有元素会阻塞列表直到等待超时（秒）或发现可弹出元素为止。|
    |brpoplpush source destination timeout |从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。|
    |lindex key index |通过索引获取列表中的元素|
    |linsert key before|after pivot value |在列表的元素前或者后插入元素|
    |llen key |获取列表长度|
    |lpop key |移出并获取列表的第一个元素|
    |lpush key value1 [value2] |将一个或多个值插入到列表头部|
    |lpushx key value| 将一个值插入到已存在的列表头部，列表不存在时操作无效|
    |lrange key start stop |获取列表指定范围内的元素，可以使用负数表示倒数位置。0 -1表示从第0个到最后一个|
    |lrem key count value |移除列表元素|
    |lset key index value |通过索引设置列表元素的值|
    |ltrim key start stop |对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。|
    |rpop key |移除列表的最后一个元素，返回值为移除的元素。|
    |rpoplpush source destination |移除列表的最后一个元素，并将该元素添加到另一个列表并返回|
    |rpush key value1 [value2] |在列表中添加一个或多个值|
    |rpushx key value |将一个值插入到已存在的列表尾部，列表不存在时操作无效|
* Set常用命令

    |命令|说明|
    |-|-|
    |sadd key member1 [member2] |向集合添加一个或多个成员|
    |scard key |获取集合的成员数|
    |sdiff key1 [key2] |返回给定所有集合的差集，以第一个为基准返回不在后面几个集合出现过的内容|
    |sdiffstore destination key1 [key2] |返回给定所有集合的差集并存储在 destination 中|
    |sinter key1 [key2] |返回给定所有集合的交集|
    |sinterstore destination key1 [key2] |返回给定所有集合的交集并存储在 destination 中|
    |sismember key member |判断 member 元素是否是集合 key 的成员|
    |smembers key |返回集合中的所有成员|
    |smove source destination member |将 member 元素从 source 集合移动到 destination 集合|
    |spop key |移除并返回集合中的一个随机元素|
    |srandmember key [count] |返回集合中一个或多个随机数|
    |srem key member1 [member2] |移除集合中一个或多个成员|
    |sunion key1 [key2] |返回所有给定集合的并集|
    |sunionstore destination key1 [key2] |所有给定集合的并集存储在 destination 集合中|
    |sscan key cursor [match pattern] [count count]| 迭代集合中的元素|
* SortedSet常用方法

    |命令|说明|
    |-|-|
    |zadd key score1 member1 [score2 member2] |向有序集合添加一个或多个成员，或者更新已存在成员的分数|
    |zcard key |获取有序集合的成员数|
    |zcount key min max |计算在有序集合中指定区间分数的成员数|
    |zincrby key increment member |有序集合中对指定成员的分数加上增量 increment|
    |zinterstore destination numkeys key [key ...] |计算给定的一个或多个有序集的交集并将结果集存储在新的有序集合 key 中，会将分数相加|
    |zlexcount key min max |在有序集合中计算指定字典区间内成员数量|
    |zrange key start stop [withscores] |通过索引区间返回有序集合指定区间内的成员|
    |zrangebylex key min max [limit offset count] |通过字典区间返回有序集合的成员，min max分别表示存入的元素而不是下标，`(a [c`表示从a-c不包括a，`- [c`表示从头到c|
    |zrangebyscore key min max [withscores] [limit] |通过分数返回有序集合指定区间内的成员|
    |zrank key member |返回有序集合中指定成员的索引|
    |zrem key member [member ...] |移除有序集合中的一个或多个成员|
    |zremrangebylex key min max |移除有序集合中给定的字典区间的所有成员
    |zremrangebyrank key start stop |移除有序集合中给定的排名区间的所有成员|
    |zremrangebyscore key min max| 移除有序集合中给定的分数区间的所有成员|
    |zrevrange key start stop [withscores]| 返回有序集中指定区间内的成员，通过索引，分数从高到低|
    |zrevrangebyscore key max min [withscores] |返回有序集中指定分数区间内的成员，分数从高到低排序|
    |zrevrank key member| 返回有序集合中指定成员的排名，有序集成员按分数值递减(从大到小)排序|
    |zscore key member |返回有序集中，成员的分数值|
    |zunionstore destination numkeys key [key ...] |计算给定的一个或多个有序集的并集，并存储在新的 key 中|
    |zscan key cursor [match pattern] [count count]| 迭代有序集合中的元素（包括元素成员和元素分值）|
*  Bitmap常用方法

    |命令|说明|
    |-|-|
    |setbit key offset value|对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)。|
    |getbit key offset|对 key 所储存的字符串值，获取指定偏移量上的位(bit)。|
    |bitcount key start end|计算给定字符串中，被设置为 1 的比特位的数量，也可指定范围。|
    |bitpos key start end|返回位图中第一个值为 bit 的二进制位的位置，也可指定范围。|
    |bitop subcommand destkey key [key ...]|对一个或多个保存二进制位的字符串 key 进行位元操作，并将结果保存到 destkey 上。|
    |bitfield|命令可以将一个 Redis 字符串看作是一个由二进制位组成的数组， 并对这个数组中储存的长度不同的整数进行访问 （被储存的整数无需进行对齐）|
    * bitop的subcommand 除了 not 操作之外，其他操作都可以接受一个或多个 key 作为输入。
        * and 对一个或多个 key 求逻辑并，并将结果保存到 destkey 。
        * or 对一个或多个 key 求逻辑或，并将结果保存到 destkey 。
        * xor 对一个或多个 key 求逻辑异或，并将结果保存到 destkey 。
        * not 对给定 key 求逻辑非，并将结果保存到 destkey 。

* Geo常用方法  
    GEOADD 命令以标准的 x,y 格式接受参数， 所以用户必须先输入经度， 然后再输入纬度。 GEOADD 能够记录的坐标是有限的： 非常接近两极的区域是无法被索引的。 精确的坐标限制由 EPSG:900913 / EPSG:3785 / OSGEO:41001 等坐标系统定义， 具体如下：
    * 有效的经度介于 -180 度至 180 度之间。
    * 有效的纬度介于 -85.05112878 度至 85.05112878 度之间。  
    
    当用户尝试输入一个超出范围的经度或者纬度时， GEOADD 命令将返回一个错误。

    |命令|说明|
    |-|-|
    |geoadd key longitude latitude member |增加某个地理位置的坐标|
    |geopos key member [member …]|获取某个地理位置的坐标|
    |geodist key member1 member2 [unit]|获取两个地理位置的距离|
    |georadius key longitude latitude radius m|km|ft|mi [withcoord] [withdist] [withhash] [asc|desc] [count count]|根据给定地理位置坐标获取指定范围内的地理位置集合|
    |georadiusbymember key member radius m|km|ft|mi [withcoord] [withdist] [withhash] [asc|desc] [count count]|根据给定地理位置获取指定范围内的地理位置集合|
    |geohash key member [member …]|获取某个地理位置的 geohash 值|

#### 基数统计HyperLogLog
HyperLogLog用来做基数统计算法，在输入预算数量或体积非常大是，计算基数所需控件总是固定的并且很小。Redis里面HyperLogLog使用12k内存记录基数，且不会一次占用12k，HyperLogLog只是用来计算基数不会保存各个元素   
数据集中有{1, 3, 5, 7, 5, 7, 8}，那么这个数据集的基数集为{1, 3, 5, 7, 8}，基数为5，基数有0.81%的误差

|命令|说明|
|-|-|
|pfadd key element [element ...] |添加指定元素到 HyperLogLog 中。|
|pfcount key [key ...] 返回给定 HyperLogLog |的基数估算值。|
|pfmerge destkey sourcekey [sourcekey ...] |将多个 HyperLogLog 合并为一个 HyperLogLog|
#### 发布订阅
由一个客户端发布订阅创建频道，再由另一个客户端发送信息

|命令|说明|
|-|-|
|psubscribe pattern [pattern ...] |订阅一个或多个符合给定模式的频道。以\*号做通配符，例如it\*表示接收已it开头的频道|
|pubsub subcommand [argument [argument ...]] |查看订阅与发布系统状态。|
|publish channel message |将信息发送到指定的频道。|
|punsubscribe [pattern [pattern ...]] |退订所有给定模式的频道。|
|subscribe channel [channel ...] |订阅给定的一个或多个频道的信息。|
|unsubscribe [channel [channel ...]] |指退订给定的频道。|
* pubsub 的subcommand
    * channels 列出当前活跃频道，至少有一个订阅者，可以指定过滤频道类型例如`i*`表示以i开始的频道
    * numsub 返回指定频道订阅数量（subscribe）
    * numpat 返回订阅模式数量（psubscribe）
#### 事务
Redis 事务可以一次执行多个命令， 并且带有以下三个重要的保证：
* 批量操作在发送 EXEC 命令前被放入队列缓存。
* 收到 EXEC 命令后进入事务执行，事务中任意命令执行失败，其余的命令依然被执行。
* 在事务执行过程，其他客户端提交的命令请求不会插入到事务执行命令序列中。

一个事务从开始到执行会经历以下三个阶段：
* 开始事务。
* 命令入队。
* 执行事务。

事务由`multi`开始，然后将多个命令放到队列中，最后由`exec`执行触发，Redis的命令有原子性，但事务没有，事务执行中间出错并不会回滚之前操作的命令，事务更像一个命令打包，用来防止在一串命令中被插入其他命令

|命令|说明|
|-|-|
|discard |取消事务，放弃执行事务块内的所有命令。|
|exec |执行所有事务块内的命令。|
|multi|标记一个事务块的开始。|
|unwatch |取消 watch 命令对所有 key 的监视。|
|watch key [key ...] |监视一个(或多个) key ，如果在事务执行之前这个(或这些) key 被其他命令所改动，那么事务将被打断。|
```
127.0.0.1:6379> watch a
OK
127.0.0.1:6379> multi
OK
127.0.0.1:6379> set a 2
QUEUED
127.0.0.1:6379> exec
(nil)
```
如果在`watch`后其他客户端修改了a的值，那么后面的事务将不会执行
#### 脚本
Redis 脚本使用 Lua 解释器来执行脚本。 Redis 2.6 版本通过内嵌支持 Lua 环境。执行脚本的常用命令为 EVAL。

|命令|说明|
|-|-|
|eval script numkeys key [key ...] arg [arg ...] |执行 lua 脚本。|
|evalsha sha1 numkeys key [key ...] arg [arg ...] |执行 lua 脚本。|
|script exists script [script ...] |查看指定的脚本是否已经被保存在缓存当中。|
|script flush |从脚本缓存中移除所有脚本。|
|script kill |杀死当前正在运行的 lua 脚本。|
|script load script 将脚本 script |添加到脚本缓存中，但并不立即执行这个脚本。|
#### 连接
|命令|说明|
|-|-|
|auth password |验证密码是否正确|
|echo message |打印字符串|
|ping |查看服务是否运行|
|quit |关闭当前连接|
|select index |切换到指定的数据库|
#### 服务器命令
|命令|说明|
|-|-|
|bgrewriteaof| 异步执行一个 aof（appendonly file） 文件重写操作|
|bgsave |在后台异步保存当前数据库的数据到磁盘|
|client kill [ip:port] [id client-id] 关|闭客户端连接|
|client list |获取连接到服务器的客户端连接列表|
|client getname |获取连接的名称|
|client pause timeout |在指定时间内终止运行来自客户端的命令|
|client setname connection-name |设置当前连接的名称|
|cluster slots |获取集群节点的映射数组|
|command |获取 redis 命令详情数组|
|command count |获取 redis 命令总数|
|command getkeys |获取给定命令的所有键|
|time |返回当前服务器时间|
|command info command-name [command-name ...] |获取指定 redis 命令描述的数组|
|config get parameter |获取指定配置参数的值|
|config rewrite |对启动 redis 服务器时所指定的 redis.conf 配置文件进行改写|
|config set parameter value |修改 redis 配置参数，无需重启|
|config resetstat |重置 info 命令中的某些统计数据|
|dbsize |返回当前数据库的 key 的数量|
|debug object key |获取 key 的调试信息|
|debug segfault |让 redis 服务崩溃|
|flushall |删除所有数据库的所有key|
|flushdb |删除当前数据库的所有key|
|info [section] |获取 redis 服务器的各种信息和统计数值|
|lastsave| 返回最近一次 redis 成功将数据保存到磁盘上的时间，以 unix 时间戳格式表示|
|monitor |实时打印出 redis 服务器接收到的命令，调试用|
|role |返回主从实例所属的角色|
|save |同步保存数据到硬盘|
|shutdown [nosave] [save] |异步保存数据到硬盘，并关闭服务器|
|slaveof host port |将当前服务器转变为指定服务器的从属服务器(slave server)|
|slowlog subcommand [argument] |管理 redis 的慢日志|
|sync |用于复制功能(replication)的内部命令|
