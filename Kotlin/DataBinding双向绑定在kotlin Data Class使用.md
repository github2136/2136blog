# DataBinding双向绑定在Kotlin Data Class使用

如果普通类`class`中直接使用双向绑定`@={}`默认是没有效果的，首先需要继承`BaseObservable()`然后修改需要双向绑定的变量

```kotlin
@get:Bindable
var longitude = ""
    set(value) {
        field = value
        notifyPropertyChanged(BR.longitude)
    }
```

这样就能进行双向绑定了

如果使用数据类`data class`则只能在类中的变量进行双向绑定，构造函数中的变量则不能，加入这个类需要在传入`Intent`中则要注意，如果使用`@Parcelize`注解，则类中的变量不会传递，只会传递构造函数中的变量，此时要么生成`Parcelable`代码，要么使用以下代码

```kotlin
@Parcelize
data class Plant(
    var altitude: Double = 0.0,//海拔    
) : BaseObservable(), Parcelable {
    ///////////////////////////////////////////////////////////////////////////
    // 双向绑定字段
    ///////////////////////////////////////////////////////////////////////////
    @get:Bindable
    var _altitude = 0.0
        set(value) {
            field = value
            altitude = value
            notifyPropertyChanged(BR._altitude)
        }
        get() = altitude 
}
```

在`xml`和`kotlin`赋值时使用`_`开头的变量