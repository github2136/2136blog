Coroutines协程
===

https://developer.android.google.cn/topic/libraries/architecture/coroutines?hl=zh_cn

在Android是用协程需要添加`implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.0'`，然后添加合适的KTX

- 对于 `ViewModelScope`，请使用 `androidx.lifecycle:lifecycle-viewmodel-ktx:2.1.0-beta01` 或更高版本。
- 对于 `LifecycleScope`，请使用 `androidx.lifecycle:lifecycle-runtime-ktx:2.2.0-alpha01` 或更高版本。
- 对于 `liveData`，请使用 `androidx.lifecycle:lifecycle-livedata-ktx:2.2.0-alpha01` 或更高版本。

## 生命周期感知型协程范围

* ViewModelScope

  为应用中每个`ViewModel`定义`ViewModelScope`。如果`ViewModel`清除则协程都会自动取消

* LifecycleScope

  为每个`Lifecycle`定义`LifecycleScope`。在此范围内的协程会在`Lifecycle`销毁时取消

* 协程与LiveData使用

  当`LiveData`需要使用异步计算值时可以使用`liveData`调用`suspend`函数将结果返回给`LiveData`使用`emit`或`emitSource`发送结果

  ```kotlin
  val user: LiveData<User> = liveData {
          val data = database.loadUser() // loadUser is a suspend function.
          emit(data)
      }
  ```

  如果在`LiveData`超时或在获取结果之前取消了，则`LiveData`再次变为活动时将会重启协程再次获取结果，但如果是代码内的异常则不会重启

  在代码块中可以调用多次`emit`

