# 协程
[协程官方文档](https://www.kotlincn.net/docs/reference/coroutines/coroutines-guide.html)

异步或非阻塞程序设计是新的现实。无论我们创建服务端应用、桌面应用还是移动端应用，都很重要的一点是， 我们提供的体验不仅是从用户角度看着流畅，而且还能在需要时伸缩（scalable，可扩充/缩减规模）  
kotlin默认不包括协程如需使用协程可查看以下网址添加  
https://github.com/hltj/kotlinx.coroutines-cn/blob/master/README.md#using-in-your-projects
` implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0'`

## 协程的基础使用

运行一个协程

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking<Unit> { // 开始执行主协程，使用runBlocking包装方法就可以在方法内使用协程相关代码
    GlobalScope.launch { // 在后台启动一个新的协程并继续
        delay(1000L)//延迟协程
        println("World!")
    }
    println("Hello,") // 主协程在这里会立即执行
    delay(2000L)      // 延迟 2 秒来保证 JVM 存活
}
```

## 作业创建

```kotlin
val job: kotlinx.coroutines.Job = GlobalScope.launch { // 启动一个新协程并保持对这个作业的引用
    delay(1000L)
    println("World!")
}
println("Hello,")
job.join() // 等待直到子协程执行结束，和Thread.join()效果相同
```

## 结构化的并发

当使用`GlobalScope.launch`时会创建一个顶层协程。虽然它很轻量但运行时仍会消耗内存资源。如果忘记对新协程的引用他将会继续运行，如果手动保持对已启动的协程引用并`join`则很容易出错。

更好的处理方法就是使用结构化并发

例如前面实例使用`runBlocking`协程构建器将`main`函数变为协程

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking { // this: CoroutineScope
    launch { // 在 runBlocking 作用域中启动一个新协程
        delay(1000L)
        println("World!")
    }
    println("Hello,")
}
```

## 作用域构建器

除了使用不同构建器提供协程作用域外，还可以使用`coroutineScope`构建器声明自己的作用域，在作用域内的子协程执行完成前是不会结束

`runBlocking`和`coroutineScope`看起来类似，他们都会等子协程结束，区别在于`runBlocking`会`阻塞`当前线程等待`coroutineScope`则是挂起，释放底层线程用于其他用途，所以`runBlocking`是常规函数`coroutineScope`是挂起函数

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking { // this: CoroutineScope
    launch { 
        delay(200L)
        println("Task from runBlocking")
    }
    coroutineScope { // 创建一个协程作用域
        launch {
            delay(500L) 
            println("Task from nested launch")
        }
        delay(100L)
        println("Task from coroutine scope") // 这一行会在内嵌 launch 之前输出
    }
    println("Coroutine scope is over") // 这一行在内嵌 launch 执行完毕后才输出
}
```

```
Task from coroutine scope
Task from runBlocking
Task from nested launch
Coroutine scope is over
```

[coroutineScope说明解释](https://www.cnblogs.com/webor2006/p/11736509.html)

首先遇到第一个`launch`等待200毫秒输出，然后继续到`coroutineScope`挂起协程，在子协程完成前后续代码将不会执行，当100毫秒到时执行`Task from coroutine scope`，由于不是阻塞，200毫秒时执行`Task from runBlocking`，500毫秒时执行`Task from nested launch`，此时`coroutineScope`内容已经完全执行完了，立刻执行了`Coroutine scope is over`

## 提取函数重构

只要在方法前添加`suspend`就可以表示这是一个可挂起函数

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking {
    launch { doWorld() }
    println("Hello,")
}

// 这是你的第一个挂起函数
suspend fun doWorld() {
    delay(1000L)
    println("World!")
}
```

## 全局线程像守护线程

当在`GlobalScope`中启动了一个长期运行的协程，当主函数结束时协程也会结束，不会和`Thread`一样不停运行

## 取消与超时

取消协程可以使用`cancel`方法取消

```kotlin
val job = launch {
    repeat(1000) { i ->
        println("job: I'm sleeping $i ...")
        delay(500L)
    }
}
delay(1300L) // 延迟一段时间
println("main: I'm tired of waiting!")
job.cancel() // 取消该作业
job.join() // 等待作业执行结束
println("main: Now I can quit.")
```

也可以直接使用`cancelAndJoin`这个方法组合了`cancel`和`join`

协程取消需要检查是否取消否则不会取消，当检查取消时会抛出`CancellationException`

```kotlin
val startTime = System.currentTimeMillis()
val job = launch(Dispatchers.Default) {//注意此处使用了Dispatchers.Default
    var nextPrintTime = startTime
    var i = 0
    while (i < 5) { // 一个执行计算的循环，只是为了占用 CPU
        // 每秒打印消息两次
        if (System.currentTimeMillis() >= nextPrintTime) {
            println("job: I'm sleeping ${i++} ...")
            nextPrintTime += 500L
        }
    }
}
delay(1300L) // 等待一段时间
println("main: I'm tired of waiting!")
job.cancelAndJoin() // 取消一个作业并且等待它结束
println("main: Now I can quit.")
```

这段代码实际并不会取消

要使代码可取消方法有两种

* 调用`yield`让出cpu，如果取消则会显示`CancellationException`
* 使用`isActive`来检查是否已经调用取消方法

## 取消时释放资源

在取消时会抛出`CancellationException`异常，可以使用`try……finally……`捕获然后处理释放资源

```kotlin
val job = launch {
    try {
        repeat(1000) { i ->
            println("job: I'm sleeping $i ...")
            delay(500L)
        }
    } finally {
        println("job: I'm running finally")
    }
}
delay(1300L) // 延迟一段时间
println("main: I'm tired of waiting!")
job.cancelAndJoin() // 取消该作业并且等待它结束
println("main: Now I can quit.")
```

任何在`finally`中尝试调用挂起函数都将会抛出` CancellationException`，如果要运行不可取消代码块可以使用`NonCancellable`

```kotlin
val job = launch {
    try {
        repeat(1000) { i ->
            println("job: I'm sleeping $i ...")
            delay(500L)
        }
    } finally {
        withContext(NonCancellable) {
            println("job: I'm running finally")
            delay(1000L)
            println("job: And I've just delayed for 1 sec because I'm non-cancellable")
        }
    }
}
delay(1300L) // 延迟一段时间
println("main: I'm tired of waiting!")
job.cancelAndJoin() // 取消该作业并等待它结束
println("main: Now I can quit.")
```

## 超时

如果要给协程设置超时时间可以使用`withTimeout`

```kotlin
withTimeout(1300L) {
    repeat(1000) { i ->
        println("I'm sleeping $i ...")
        delay(500L)
    }
}
```

`TimeoutCancellationException`超时异常是`CancellationException`的子类，所以要触发和取消协程一样需要`delay`获得异常或`isActive`判断是否取消

`withTimeoutOrNull`可以在未超出时返回值

```kotlin
val result = withTimeoutOrNull(1300L) {
    repeat(1000) { i ->
        println("I'm sleeping $i ...")
        delay(500L)
    }
    "Done" // 在它运行得到结果之前取消它
}
println("Result is $result")
```

## 组合挂起函数

挂起函数默认顺序执行

先定义两个方法

```kotlin
suspend fun doSomethingUsefulOne(): Int {
    delay(1000L) // 假设我们在这里做了一些有用的事
    return 13
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1000L) // 假设我们在这里也做了一些有用的事
    return 29
}
```

```kotlin
val time = measureTimeMillis {
    val one = doSomethingUsefulOne()
    val two = doSomethingUsefulTwo()
    println("The answer is ${one + two}")
}
println("Completed in $time ms")
```

运行后发现两个方法是顺序执行

使用`async`可以并发方法，如果方法之间没有依赖则可以使用并发

```kotlin
val time = measureTimeMillis {
    val one = async { doSomethingUsefulOne() }
    val two = async { doSomethingUsefulTwo() }
    println("The answer is ${one.await() + two.await()}")
}
println("Completed in $time ms")
```

惰性启动`async`，把方法参数设置为`start = CoroutineStart.LAZY`这样就必须手动调用`start`来再调用`await`

```kotlin
val time = measureTimeMillis {
    val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
    val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
    // 执行一些计算
    one.start() // 启动第一个
    two.start() // 启动第二个
    println("The answer is ${one.await() + two.await()}")
}
println("Completed in $time ms")
```

如果没用调用`start`直接调用`await`则会变成同步顺序执行

可以使用`coroutineScope`来组合方法

```kotlin
suspend fun concurrentSum(): Int = coroutineScope {
    val one = async { doSomethingUsefulOne() }
    val two = async { doSomethingUsefulTwo() }
    one.await() + two.await()
}
```

使用`async`的结构化并发时如果有一个方法异常，该作用域内所有子协程都会被取消

## 协程上下文与调度器

所有协程构建器例如`launch`和`async`都有一个可选的`CoroutineContext`它可以显式的指定上调度器

```kotlin
launch { // 运行在父协程的上下文中，即 runBlocking 主协程
    println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
}
launch(Dispatchers.Unconfined) { // 不受限的——将工作在主线程中
    println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
}
launch(Dispatchers.Default) { // 将会获取默认调度器
    println("Default               : I'm working in thread ${Thread.currentThread().name}")
}
launch(newSingleThreadContext("MyOwnThread")) { // 将使它获得一个新的线程
    println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
}
```

`newSingleThreadContext` 为协程的运行启动了一个线程。 一个专用的线程是一种非常昂贵的资源。 在真实的应用程序中两者都必须被释放，当不再需要的时候，使用 `close` 函数，或存储在一个顶层变量中使它在整个应用程序中被重用。

## 共享的可变状态与并发

在协程中执行并发操作时一般来说会出现错误的结果例如

```kotlin
suspend fun massiveRun(action: suspend () -> Unit) {
    val n = 100  // 启动的协程数量
    val k = 1000 // 每个协程重复执行同一动作的次数
    val time = measureTimeMillis {
        coroutineScope { // 协程的作用域
            repeat(n) {
                launch {
                    repeat(k) { action() }
                }
            }
        }
    }
    println("Completed ${n * k} actions in $time ms")    
}
var counter = 0

fun main() = runBlocking {
    withContext(Dispatchers.Default) {
        massiveRun {
            counter++
        }
    }
    println("Counter = $counter")
}
```

这段代码大概率不能打印出`Counter = 100000`，处理方法一般为

* 使用`AtomicInteger`之类的线程安全类

  ```kotlin
  val counter = AtomicInteger()
  
  fun main() = runBlocking {
      withContext(Dispatchers.Default) {
          massiveRun {
              counter.incrementAndGet()
          }
      }
      println("Counter = $counter")
  }
  ```

* 以细粒度限制线程，对特定共享状态的所有访问都限制在单个线程中。它通常应用于UI线程中

  ```kotlin
  val counterContext = newSingleThreadContext("CounterContext")
  var counter = 0
  
  fun main() = runBlocking {
      withContext(Dispatchers.Default) {
          massiveRun {
              // 将每次自增限制在单线程上下文中
              withContext(counterContext) {
                  counter++
              }
          }
      }
      println("Counter = $counter")
  }
  ```

  这段代码运行十分缓慢，因为每次增量都要切换都要通过`withContext`切换协程

* 以粗粒度限制线程，将所有操作都限制到单线程中

  ```kotlin
  val counterContext = newSingleThreadContext("CounterContext")
  var counter = 0
  
  fun main() = runBlocking {
      // 将一切都限制在单线程上下文中
      withContext(counterContext) {
          massiveRun {
              counter++
          }
      }
      println("Counter = $counter")
  }
  ```

* 互斥，在协程中不需要使用`synchronized`或`ReentrantLock`，使用`Mutex`互斥锁即可，`Mutex.lock()`将会挂起函数通常的写法是`mutex.lock(); try { …… } finally { mutex.unlock() }`，但是可以使用`withLock`扩展函数替代，下面的代码属于细粒度控制，因此会比较耗时

  ```kotlin
  val mutex = Mutex()
  var counter = 0
  
  fun main() = runBlocking {
      withContext(Dispatchers.Default) {
          massiveRun {
              // 用锁保护每次自增
              mutex.withLock {
                  counter++
              }
          }
      }
      println("Counter = $counter")
  }
  ```

* Actors，

