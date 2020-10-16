# Kotlin回调

符合kotlin风格多回调累，首先定义回调类，每个回调对应一个变量一个方法

```kotlin
class RepositoryCallback {
    var mComplete1: (() -> Unit)? = null
    var mComplete1: (() -> Unit)? = null
    
    fun onComplete1(action: () -> Unit) {
        mComplete1 = action
    }
    fun onComplete2(action: () -> Unit) {
        mComplete2 = action
    }
}
```

创建使用多回调的方法

```kotlin
fun getFun(param: String, callback: RepositoryCallback.() -> Unit) {
    val mCallback = RepositoryCallback().apply(callback)
    //调用指定回调
    mCallback.mComplete1?.invoke()
    mCallback.mComplete2?.invoke()
}
```

使用多回调

```kotlin
getFun("param") {
    onComplete1 {}
    onComplete1 {}
}
```

