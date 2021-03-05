# DataStore

https://developer.android.google.cn/topic/libraries/architecture/datastore
DsUtil.kt为一个简单工具类

Jetpack DataStore 是一种数据存储解决方案，允许您使用协议缓冲区存储键值对或类型化对象。DataStore 使用 Kotlin 协程和流程以异步、一致的事务方式存储数据。

如果您需要支持大型或复杂数据集、部分更新或参照完整性，请考虑使用Room，而不是 DataStore。DataStore 非常适合简单的小型数据集，**不支持部分更新或参照完整性**。

>  SharedPreferences的缺点
>
>  * 获取保存数据可能会阻塞UI线程
>  * 不能保证类型安全
>  * 加载的数据会一直在内存中
>  * apply()无法获取成功失败的结果
>  * 没有一致性或事物语义

## Preferences DataStore 和 Proto DataStore

DataStore 提供两种不同的实现：Preferences DataStore 和 Proto DataStore。

- **Preferences DataStore** 使用键存储和访问数据。此实现不需要预定义的架构，也不确保类型安全。
- **Proto DataStore** 将数据作为自定义数据类型的实例进行存储。此实现要求您使用协议缓冲区来定义架构，但可以确保类型安全。

* Proto DataStore

  ```groovy
  // Typed DataStore (Typed API surface, such as Proto)
  dependencies {
    implementation "androidx.datastore:datastore:1.0.0-alpha06"
  
    // optional - RxJava2 support
    implementation "androidx.datastore:datastore-rxjava2:1.0.0-alpha06"
  
    // optional - RxJava3 support
    implementation "androidx.datastore:datastore-rxjava3:1.0.0-alpha06"
  }
  // 没有Android相关依赖
  dependencies {
    implementation "androidx.datastore:datastore-core:1.0.0-alpha06"
  }
  ```

* Preferences DataStore

  ```groovy
  // Preferences DataStore (SharedPreferences like APIs)
  dependencies {
    implementation "androidx.datastore:datastore-preferences:1.0.0-alpha06"
  
    // optional - RxJava2 support
    implementation "androidx.datastore:datastore-preferences-rxjava2:1.0.0-alpha06"
  
    // optional - RxJava3 support
    implementation "androidx.datastore:datastore-preferences-rxjava3:1.0.0-alpha06"
  }
  // 没有Android相关依赖
  dependencies {
    implementation "androidx.datastore:datastore-preferences-core:1.0.0-alpha06"
  }
  ```

**如果是旧项目添加DataStore可能会出现同时出现`kotlinx-coroutines-core-jvm`和`kotlinx-coroutines-core`提示类重复的问题。解决方法有两种**

* 引用时移除`kotlinx-coroutines-core-jvm`的引用`exclude group:'org.jetbrains.kotlinx',module:'kotlinx-coroutines-core-jvm'`
* 升级
  * kotlin版本：`ext.kotlin_version = "1.3.72"`
  * gradle-build版本：`classpath "com.android.tools.build:gradle:4.1.1"`
  * gradle-wrapper版本：`distributionUrl=https\://services.gradle.org/distributions/gradle-6.5-bin.zip`
  * gradle.properties增加：`android.useAndroidX=true`

## 使用 Preferences DataStore 存储键值对

* 创建实例，主要永远不要给同一个DataStore创建一个以上的DataStore实例，这样可能会破坏DataStore功能。每次调用createDataStore都会创建一个新的实例

  ```kotlin
  val dataStore: DataStore<Preferences> = context.createDataStore("name")	
  ```

  * name：文件名，文件将会存储在`File（context.filesDir，“ datastore /” +名称+“ .preferences_pb”）`中
  * corruptionHandler：如果在尝试读取数据时遇到`CorruptionException`就会调用corruptionHandler
  * migrations：迁移合并，迁移可能会调用多次因为可能会失败
  * scope：IO操作和转换功能的执行范围

* 读取数据

  由于Preferences 不使用预定架构，并不能保证类型安全，在获取或存储值是需要使用指定类型的key，比如要存储int就要使用`intPreferencesKey()`。然后使用DataStore.data属性，通过Flow提供适当的存储值
	```kotlin
val EXAMPLE_COUNTER = intPreferencesKey("example_counter")
val exampleCounterFlow: Flow<Int> = context.dataStore.data
  .map { 
    it[EXAMPLE_COUNTER] ?: 0
}
	val value = exampleCounterFlow.first()
	```

* 写入数据

  Preferences DataStore提供了一个edit()函数，用于以实物方式更新数据，转换块中的所有代码均被视为单个事务。

  ```kotlin
  suspend fun incrementCounter() {
    context.dataStore.edit { 
      val currentCounterValue = settings[EXAMPLE_COUNTER] ?: 0
      it[EXAMPLE_COUNTER] = currentCounterValue + 1
    }
  }
  ```

## 使用 Proto DataStore 存储类型化的对象



## 在同步代码中使用 DataStore

**注意：请尽可能避免在 DataStore 数据读取时阻塞线程。阻塞界面线程可能会导致 ANR或界面卡顿，而阻塞其他线程可能会导致死锁。**

Kotlin可以使用`runBlocking()`协程构建器来同步读取数据

```kotlin
val exampleData = runBlocking { context.dataStore.data.first() }
```

