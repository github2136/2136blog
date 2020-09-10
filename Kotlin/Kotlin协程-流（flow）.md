Kotlin协程-流（flow）
===

挂起函数可以异步返回单个值，如果要返回多个值可以使用序列`sequence`

```kotlin
fun simple(): Sequence<Int> = sequence { // 序列构建器
    for (i in 1..3) {
        Thread.sleep(100) // 假装我们正在计算
        yield(i) // 产生下一个值
    }
}

fun main() {
    simple().forEach { value -> println(value) } 
}
```

或者使用挂起函数

```kotlin
suspend fun simple(): List<Int> {
    delay(1000) // 假装我们在这里做了一些异步的事情
    return listOf(1, 2, 3)
}

fun main() = runBlocking<Unit> {
    simple().forEach { value -> println(value) } 
}
```

## 流（flow）

使用 List 结果类型，意味着我们只能一次返回所有值。 为了表示异步计算的值流（stream），我们可以使用 Flow类型（正如同步计算值会使用 Sequence 类型）

```kotlin
fun simple(): Flow<Int> = flow { // 流构建器
    for (i in 1..3) {
        delay(100) // 假装我们在这里做了一些有用的事情
        emit(i) // 发送下一个值
    }
}

fun main() = runBlocking<Unit> {
    // 启动并发的协程以验证主线程并未阻塞
    launch {
        for (k in 1..3) {
            println("I'm not blocked $k")
            delay(100)
        }
    }
    // 收集这个流
    simple().collect { value -> println(value) } 
}
```

**`Flow`只有在调用`collect`时才会运行**

```kotlin
fun simple(): Flow<Int> = flow { 
    println("Flow started")
    for (i in 1..3) {
        delay(100)
        emit(i)
    }
}

fun main() = runBlocking<Unit> {
    println("Calling simple function...")
    val flow = simple()
    println("Calling collect...")
    flow.collect { value -> println(value) } 
    println("Calling collect again...")
    flow.collect { value -> println(value) } 
}
```

打印结果

```
Calling simple function...
Calling collect...
Flow started
1
2
3
Calling collect again...
Flow started
1
2
3
```

## 流取消

流采用与协程同样的协作取消。像往常一样，流的收集可以在当流在一个可取消的挂起函数（例如 `delay`）中挂起的时候取消，通过`kotlinx.coroutines.Job`的`cancel`方法可以手动取消流。

```kotlin
fun simple(): Flow<Int> = flow { 
    for (i in 1..3) {
        delay(100)          
        println("Emitting $i")
        emit(i)
    }
}

fun main() = runBlocking<Unit> {
    withTimeoutOrNull(250) { // 在 250 毫秒后超时
        simple().collect { value -> println(value) } 
    }
    println("Done")
}
```

## 流的构建

除了使用`flow{}`构建器创建，还可以使用

* `flowOf`构建一个固定值的流
* 使用`.asFlow()`扩展函数将集合变成流

## 过渡流操作

可以使用操作符转换流，就像使用集合与序列一样。 过渡操作符应用于上游流，并返回下游流。 这些操作符也是冷操作符，就像流一样。这类操作符本身不是挂起函数。它运行的速度很快，返回新的转换流的定义。

基础的操作符拥有相似的名字，比如 `map` 与 `filter`。 流与序列的主要区别在于这些操作符中的代码可以调用挂起函数。

```kotlin
suspend fun performRequest(request: Int): String {
    delay(1000) // 模仿长时间运行的异步工作
    return "response $request"
}

fun main() = runBlocking<Unit> {
    (1..3).asFlow() // 一个请求流
        .map { request -> performRequest(request) }
        .collect { response -> println(response) }
}
```

## 转换操作符

在流转换操作符中，最通用的一种称为 `transform`。它可以用来模仿简单的转换，例如 `map`与 `filter`，以及实施更复杂的转换。 使用 `transform` 操作符，我们可以`发射(emit)` 任意值任意次。

```kotlin
(1..3).asFlow() // 一个请求流
    .transform { request ->
        emit("Making request $request") 
        emit(performRequest(request)) 
    }
    .collect { response -> println(response) }
```

## 限长操作

限长过渡操作符（例如`take`）在流触及相应限制的时候会将它的执行取消。协程中的取消操作总是通过抛出异常来执行，这样所有的资源管理函数（如 `try {...} finally {...}` 块）会在取消的情况下正常运行：

```kotlin
fun numbers(): Flow<Int> = flow {
    try {                          
        emit(1)
        emit(2) 
        println("This line will not execute")
        emit(3)    
    } finally {
        println("Finally in numbers")
    }
}

fun main() = runBlocking<Unit> {
    numbers() 
        .take(2) // 只获取前两个
        .collect { value -> println(value) }
} 
```

## 末端流操作符

末端操作符是在流上用于启动流收集的*挂起函数*。`collect` 是最基础的末端操作符，但是还有另外一些更方便使用的末端操作符：

* 转化为各种集合，例如`toList`/`toSet`
* 获取第一个`first`，确保流发射单个`single`
* 使用 `reduce` 与 `fold`将流规约到单个值。

```
val sum = (1..5).asFlow()                 
    .reduce { a, b -> a + b } // 求和（末端操作符）
println(sum)//15

val sum = (1..5).asFlow()
    .fold(10) { a, b -> a + b }
println(sum)//25
```

## 流是连续的

流的操作都是按顺序操作

## 流上下文

流的收集总是在调用协程的上下文中发生，如果在`flow{}`中使用`withContext`切换上下文则会报错，如果要切换流的上下文需要使用`flowOn`方法

```kotlin
fun simple(): Flow<Int> = flow {
    for (i in 1..3) {
        Thread.sleep(100) // 假装我们以消耗 CPU 的方式进行计算
        println("${Thread.currentThread().name} Emitting $i")
        emit(i) // 发射下一个值
    }
}.flowOn(Dispatchers.Default) // 在流构建器中改变消耗 CPU 代码上下文的正确方式

fun main() = runBlocking<Unit> {
    simple().collect { value ->
        println("${Thread.currentThread().name} Collected $value")
    }
}
```

## 缓冲

假设流的发射需要100毫秒，而处理需要300毫秒时，每处理一个结果需要400毫秒，3个结果一共就需要1200毫秒

```kotlin
fun simple(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(100) // 假装我们异步等待了 100 毫秒
        emit(i) // 发射下一个值
    }
}

fun main() = runBlocking<Unit> { 
    val time = measureTimeMillis {
        simple().collect { value -> 
            delay(300) // 假装我们花费 300 毫秒来处理它
            println(value) 
        } 
    }   
    println("Collected in $time ms")
}
```

在流上可以使用`buffer`来缓冲发射结果，不用等`collect`处理完再处理下一个

```kotlin
val time = measureTimeMillis {
    simple()
        .buffer() // 缓冲发射项，无需等待
        .collect { value -> 
            delay(300) // 假装我们花费 300 毫秒来处理它
            println(value) 
        } 
}   
println("Collected in $time ms")
```

## 合并结果

当流代表部分操作结果或操作状态更新时，可能没有必要处理每个值，而是只处理最新的那个。在本示例中，当收集器处理它们太慢的时候， `conflate`操作符可以用于跳过中间值。

```kotlin
val time = measureTimeMillis {
    simple()
        .conflate() // 合并发射项，不对每个值进行处理
        .collect { value -> 
            delay(300) // 假装我们花费 300 毫秒来处理它
            println(value) 
        } 
}   
println("Collected in $time ms")
```

## 处理最新结果

当发送和接收都是很慢的时候，合并是一种加快处理的一种方式。他通过删除发送值来实现，另一种处理方式就是取消缓慢的接收，并每次发送新值时重新启动他。有一组与 `xxx` 操作符执行相同基本逻辑的 `xxxLatest` 操作符，但是在新值产生的时候取消执行其块中的代码

```kotlin
val time = measureTimeMillis {
    simple()
        .collectLatest { value -> // 取消并重新发射最后一个值
            println("Collecting $value") 
            delay(300) // 假装我们花费 300 毫秒来处理它
            println("Done $value") 
        } 
}   
println("Collected in $time ms")
```

## 组合流

### Zip

```kotlin
val nums = (1..3).asFlow() // 数字 1..3
val strs = flowOf("one", "two", "three") // 字符串
nums.zip(strs) { a, b -> "$a -> $b" } // 组合单个字符串
    .collect { println(it) } // 收集并打印
```

这个返回值只会按数量少的那个来执行

### Combine

使用`Combine`时`nums`或`strs`流中每次发射值都会打印一行

```kotlin
val nums = (1..3).asFlow().onEach { delay(300) } // 发射数字 1..3，间隔 300 毫秒
val strs = flowOf("one", "two", "three").onEach { delay(400) } // 每 400 毫秒发射一次字符串
val startTime = System.currentTimeMillis() // 记录开始的时间
nums.combine(strs) { a, b -> "$a -> $b" } // 使用“combine”组合单个字符串
    .collect { value -> // 收集并打印
        println("$value at ${System.currentTimeMillis() - startTime} ms from start") 
    } 
```

```
1 -> one at 435 ms from start
2 -> one at 641 ms from start
2 -> two at 838 ms from start
3 -> two at 942 ms from start
3 -> three at 1239 ms from start
```

## 展平流

当在获取流的代码中返回流时就会出现包含流的流`Flow<Flow<String>>`，此时可以使用展平模式将内容展开

```kotlin
fun requestFlow(i: Int): Flow<String> = flow {
    emit("$i: First")
    delay(500) // 等待 500 毫秒
    emit("$i: Second")
}
fun main() = runBlocking<Unit> {
    val startTime = System.currentTimeMillis() // 记录开始时间
    (1..3).asFlow()
            .onEach { delay(100) } // 每 100 毫秒发射一个数字
            .flatMapConcat { requestFlow(it) }
            .collect { value -> // 收集并打印
                println("$value at ${System.currentTimeMillis() - startTime} ms from start")
            }
}
```

```
1: First at 130 ms from start
1: Second at 631 ms from start
2: First at 732 ms from start
2: Second at 1233 ms from start
3: First at 1334 ms from start
3: Second at 1835 ms from start
```

`flatMapConcat`操作符会等待内部流完成后再收集下一个值

```kotlin
fun main() = runBlocking<Unit> {
    val startTime = System.currentTimeMillis() // 记录开始时间
    (1..3).asFlow().onEach { delay(100) } // 每 100 毫秒发射一个数字
            .flatMapMerge { requestFlow(it) }
            .collect { value -> // 收集并打印
                println("$value at ${System.currentTimeMillis() - startTime} ms from start")
            }
}
```

```
1: First at 159 ms from start
2: First at 253 ms from start
3: First at 355 ms from start
1: Second at 659 ms from start
2: Second at 754 ms from start
3: Second at 858 ms from start
```

`flatMapMerge`则会尽快收集值

**展平模式里都可以添加参数（concurrency）来限制并发数量默认情况下值等于 DEFAULT_CONCURRENCY**

展平模式同样有`xxxxLatest`方法来取消旧的值，直接获取新值

## 异常

如果使用`try{}catch{}`包裹流则不管是在哪个阶段（收集(collect)，转换(map)等）的异常，流都将停止。

### 异常透明性

流必须对异常透明，即在`flow{}`中不使用`try{}catch{}`捕获异常。`flow`可以使用`catch`操作符来保留异常透明性并且允许封装异常处理`catch`操作符对于捕获的异常有几种处理方法

* 使用`thow`重新抛出异常
* 使用`emit`将异常当做值发射
* 忽略异常或打印日志，或使用其他代码处


如果异常是在`collect{}`中发生则`catch`并不能捕获异常

### 声明式捕获

可以将`collect`中的代码移动到`onEach`中，这样收集时发生的异常`catch`就能捕获到了，但此时收集就必须使用无参的`collect()`触发

## 流完成

可以使用`try{}finally{}`获取完成，或者使用`onCompletion`操作符，在`onCompletion`中海还可以获取异常，`onCompletion`能观察异常但不负责处理异常，处理异常默认还是在`catch`操作符里处理

所以流程顺序是`flow`->`map/filter`等->`collect`->`onCompletion`->`catch`

## 启动流

如果需要一个`addEventListener`函数可以使用`onEach`操作符来做这件事，`onEach`是一个过渡操作符。如果不调用末端操作符收集`onEach`是无效的

使用`launchIn`替代`collect`可以在单独的协程中启动流的收集，这样就可以立即继续下一步代码

```kotlin
fun main() = runBlocking<Unit> {
    events()
        .onEach { event -> println("Event: $event") }
        .launchIn(this) // <--- 在单独的协程中执行流
    println("Done")
}      
```

```
Done
Event: 1
Event: 2
Event: 3
```

`launchIn`必须指定`coroutneScope`。这样就能在实体取消时，同时取消流，类似于`addEventListener`而不用`RemoveEventListener`

## 流的取消

流对每个发射`emit`的值都会检查，这意味着`flow{}`是可以取消的

```kotlin
fun foo(): Flow<Int> = flow { 
    for (i in 1..5) {
        println("Emitting $i") 
        emit(i) 
    }
}

fun main() = runBlocking<Unit> {
    foo().collect { value -> 
        if (value == 3) cancel()  
        println(value)
    } 
}
```

```
Emitting 1
1
Emitting 2
2
Emitting 3
3
Emitting 4
Exception in thread "main" kotlinx.coroutines.JobCancellationException: BlockingCoroutine was cancelled; 
```

但其他的流操作就不会自动执行例如使用`IntRange.asFlow`

```kotlin
fun main() = runBlocking<Unit> {
    (1..5).asFlow().collect { value -> 
        if (value == 3) cancel()  
        println(value)
    } 
}
```

```
1
2
3
4
5
Exception in thread "main" kotlinx.coroutines.JobCancellationException: BlockingCoroutine was cancelled; 
```

这个flow就将所有值都打印出来了

为了避免这个问题可以添加 `.onEach { currentCoroutineContext().ensureActive() }`，也可以使用现成的操作符`cancellable`

```kotlin
fun main() = runBlocking<Unit> {
    (1..5).asFlow().cancellable().collect { value -> 
        if (value == 3) cancel()  
        println(value)
    } 
}
```

```
1
2
3
Exception in thread "main" kotlinx.coroutines.JobCancellationException: 
```

