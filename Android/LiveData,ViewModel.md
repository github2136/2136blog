## LiveData
集成https://developer.android.google.cn/topic/libraries/architecture/adding-components
在support26.1.0以上版本会自动集成`LiveData`和`ViewModel`不需另外集成

`LiveData`与`ViewModel`都与生命周期有关，`AppCompatActivity`与`v4`包的`Fragment`都实现了`LifecycleOwner`接口

首先添加`ViewModel`的子类，如果需要使用`Context`可以使用`AndroidViewModel`，`ViewModel`绝不能引用带生命周期的类`Context`，如果需要引用的`View`那必须使用`var mView: WeakReference<V>`弱引用防止内存泄漏
```kotlin
class MyViewModel : ViewModel() {
    private lateinit var users: MutableLiveData<List<User>>

    fun getUsers(): LiveData<List<User>> {
        if (!::users.isInitialized) {
            users = MutableLiveData()
            loadUsers()
        }
        return users
    }

    private fun loadUsers() {
        // Do an asynchronous operation to fetch users.
    }
}
```
**如果在UI线程设置数据使用`MyViewModel.setValue`非UI线程使用`MyViewModel.postValue`**

然后在Activity调用
```kotlin
class MyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Create a ViewModel the first time the system calls an activity's onCreate() method.
        // Re-created activities receive the same MyViewModel instance created by the first activity.
        //新版ViewMoel使用下面方法
        val model = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(MyViewModel::class.java)
        //旧版ViewMoel使用下面方法
        val model = ViewModelProviders.of(this).get(MyViewModel::class.java)
        model.getUsers().observe(this, Observer<List<User>>{ users ->
            // update UI
        })
    }
}
```