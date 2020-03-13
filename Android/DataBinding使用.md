# DataBinding
**如果是使用java编写逻辑代码，并且在布局xml的DataBinding操作中使用了中文则会提示`Invalid byte 3 of 3-byte UTF-8 sequence.`，此时则需要在`gradle.properties`中添加`org.gradle.jvmargs=-Xmx1536m -Dfile.encoding=UTF-8`**

**如果是使用kotlin编写逻辑代码，并且在布局xml的DataBinding操作中使用了中文则会提示`3 字节的 UTF-8 序列的字节 3 无效。`，如果没使用`kapt`则可以和上面java版使用同样方法处理。如果必须使用`kapt`，则只能在`strings.xml`引用或使用变量**
## 集成DataBinding
* AS3.0以下
    在项目module的`build.gradle`中添加
    ```gradle
    apply plugin: 'kotlin-kapt'//需要使用kapt作为注解 处理器
    kapt {
        generateStubs = true
    }
    android{
        ....
        dataBinding {
            enabled = true
        }
    }
    dependencies{
        ///...
        kapt "com.android.databinding:compiler:/*与com.android.tools.build:gradle版本号相同*/"
        kapt 'android.arch.lifecycle:compiler:1.1.1'
    }
    ```
* AS3.0及以上
  ```
  android{
  ....
      dataBinding {
          enabled = true
      }
  }
  ```
## 布局使用
在布局文件的根节点上使用`alt+enter`->`Convert to data binding layout`将布局改为DataBinding的布局
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <data>

        <variable
            name="user"
            type="com.github2136.base.MainActivity" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <Text
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{user.firstName, default=my_default}" />

    </LinearLayout>
</layout>
```
使用default在预览时显示内容
## 布局和绑定表达式
在`data`节点中使用`variable`来引入对象
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
   <data>
       <variable name="user" type="com.example.User"/>
   </data>
   <LinearLayout
       android:orientation="vertical"
       android:layout_width="match_parent"
       android:layout_height="match_parent">
       <TextView android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:text="@{user.firstName}"/>
   </LinearLayout>
</layout>
```
在`onCreate`中使用`DataBindingUtil`设置布局
```kotlin
val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
binding.user = User("Test", "User")
```
如果要在`Fragment`，`ListView`，`RecyclerView`使用DataBinding则可以使用`inflate()`
```kotlin
//ListItemBinding是由list_item布局文件转换为DataBinding布局文件生成的
val listItemBinding = ListItemBinding.inflate(layoutInflater, viewGroup, false)
// or
val listItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, viewGroup, false)
```
### 表达式
* 数学 + - / * %
* 字符串连接 +
* 逻辑 && ||
* 二进制 & | ^
* 一元运算 + - ! ~
* 移位 >> >>> <<
* 比较 == > < >= <=
* instanceof
* 分组 ()
* 文字 - 字符，字符串，数字，null
* Cast
* 方法调用
* 数据访问 []
* 三元运算 ?:
```xml
android:text="@{String.valueOf(index + 1)}"
android:visibility="@{age > 13 ? View.GONE : View.VISIBLE}"
android:transitionName='@{"image_" + id}'
```
### 不支持的操作
* this
* super
* new
* 显式泛型调用
### 空合并操作
```xml
android:text="@{user.displayName ?? user.lastName}"
```
等效于
```xml
android:text="@{user.displayName != null ? user.displayName : user.lastName}"
```
### 避免空异常
DataBinding会自己检查空值并避免空异常，例如使用`@{user.name}`如果`user`为空则`user.name`的默认值为`null`，当`user.age`数据类型是`Int`时默认值为0
### 集合
为了方便可以直接使用`[]`访问arry、list、map等集合，如果在需要在map中使用字符串常量使用值则同时使用单引号和双引号来调用值`android:text='@{map["firstName"]}'`
```xml
<data>
    <import type="android.util.SparseArray"/>
    <import type="java.util.Map"/>
    <import type="java.util.List"/>
    <variable name="list" type="List&lt;String>"/>
    <variable name="sparse" type="SparseArray&lt;String>"/>
    <variable name="map" type="Map&lt;String, String>"/>
    <variable name="index" type="int"/>
    <variable name="key" type="String"/>
</data>
…
android:text="@{list[index]}"
…
android:text="@{sparse[index]}"
…
android:text="@{map[key]}"
```
**使用左尖括号时必须使用转义符&lt;**
### 资源调用
可以使用一下方法调用资源
```xml
android:padding="@{large? @dimen/largePadding : @dimen/smallPadding}"
```
使用字符格式化显示时可以使用
```xml
android:text="@{@string/nameFormat(firstName, lastName)}"
```
资源文件
```
<string name="nameFormat">nameFormat with %1$s and %2$s</string>
```
某些资源需要显示的判断类型
|类型|正常使用|表达式使用|
|-|-|-|
|String[]           |@array	    |@stringArray|
|int[]              |@array     |@intArray|
|TypedArray	        |@array     |@typedArray|
|Animator	        |@animator  |@animator|
|StateListAnimator  |@animator	|@stateListAnimator|
|color int          |@color     |@color|
|ColorStateList     |@color     |@colorStateList|
## 事件处理
有部分的click事件和与android:onClick冲突必须使用其他属性

|类|监听方法|属性|
|-|-|-|
|SearchView     |setOnSearchClickListener(View.OnClickListener) |android:onSearchClick|
|ZoomControls   |setOnZoomInClickListener(View.OnClickListener) |android:onZoomIn|
|ZoomControls   |setOnZoomOutClickListener(View.OnClickListener)|android:onZoomOut|

DataBinding可以从布局中添加事件处理（例如androidd:onClick），事件添加有两种方式
* 方法引用：引用方法需要定义一个方法，必须与对应的事件声明完全一致（包括入参、返回），方法引用在数据绑定的时候就已经添加
```kotlin
class MyHandlers {
    fun onClickFriend(view: View) { ... }
}
```
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
   <data>
       <variable name="handlers" type="com.example.MyHandlers"/>
       <variable name="user" type="com.example.User"/>
   </data>
   <LinearLayout
       android:orientation="vertical"
       android:layout_width="match_parent"
       android:layout_height="match_parent">
       <TextView android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:text="@{user.firstName}"
           android:onClick="@{handlers::onClickFriend}"/>
   </LinearLayout>
</layout>
```
* 监听器绑定：此功能适用于Gradl2.0及以上版本，在监听器绑定中，如果原本的事件返回的是`void`，则只要入参相同即可，如果原本事件有返回值则必须按原本返回值设置。例如点击事件默认参数为View返回值为void `override fun onClick(v: View?) { }`，那么定义监听器绑定时没有任何限制可以有任意参数和任意返回值，再例如长按事件默认参数为View返回值为void `override fun onLongClick(v: View?): Boolean {}`，那么定义的监听器绑定必须以`Boolean`做为返回值。当事件触发时才会执行绑定表达式操作

```kotlin
class Presenter {
    fun onSaveClick(task: Task){}
}
```
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable name="task" type="com.android.example.Task" />
        <variable name="presenter" type="com.android.example.Presenter" />
    </data>
    <LinearLayout android:layout_width="match_parent" android:layout_height="match_parent">
        <Button android:layout_width="wrap_content" android:layout_height="wrap_content"
        android:onClick="@{() -> presenter.onSaveClick(task)}" />
    </LinearLayout>
</layout>
```
如果原版事件有参数则可以在添加数据绑定时加入到lambda中`android:onClick="@{(view) -> presenter.onSaveClick(view)}"`、`<CheckBox android:layout_width="wrap_content" android:layout_height="wrap_content" android:onCheckedChanged="@{(cb, isChecked) -> presenter.completeChanged(task, isChecked)}" />`，事件中还可以使用三元表达式`android:onClick="@{(v) -> v.isVisible() ? doSomething() : void}"`
## 导入、变量、引入
### 导入
```xml
<data>
    <import type="android.view.View"/>
</data>
```
```kotlin
<TextView
   android:text="@{user.lastName}"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"
   android:visibility="@{user.isAdult ? View.VISIBLE : View.GONE}"/>
```
### 别名
```xml
<import type="android.view.View"/>
<import type="com.example.real.estate.View"
        alias="Vista"/>
```
可以在表达式中使用强制转换
```xml
<TextView
   android:text="@{((User)(user.connection)).lastName}"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"/>
```
在表达式中引用静态字段和方法时，可以使用`import`
```xml
<data>
    <import type="com.example.MyStringUtils"/>
    <variable name="user" type="com.example.User"/>
</data>
…
<TextView
   android:text="@{MyStringUtils.capitalize(user.lastName)}"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"/>
```
### 变量
使用`variable`设置变量
```xml
<data>
    <import type="android.graphics.drawable.Drawable"/>
    <variable name="user" type="com.example.User"/>
    <variable name="image" type="Drawable"/>
    <variable name="note" type="String"/>
</data>
```
在编译是会检查类型，如果变量实现了`Observable`或他的子类则会在修改变量时反映在界面上  
当存在各种不同布局时（例如，横向纵向）布局文件中不能有重复的变量
### 引入
通过使用app命名空间和属性中的变量名，变量可以从包含的布局传递到包含的布局绑定中。
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:bind="http://schemas.android.com/apk/res-auto">
   <data>
       <variable name="user" type="com.example.User"/>
   </data>
   <LinearLayout
       android:orientation="vertical"
       android:layout_width="match_parent"
       android:layout_height="match_parent">
       <include layout="@layout/name"
           bind:user="@{user}"/>
       <include layout="@layout/contact"
           bind:user="@{user}"/>
   </LinearLayout>
</layout>
```
DataBinding不支持include作为merge元素的直接子元素。
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:bind="http://schemas.android.com/apk/res-auto">
   <data>
       <variable name="user" type="com.example.User"/>
   </data>
   <merge><!-- Doesn't work -->
       <include layout="@layout/name"
           bind:user="@{user}"/>
       <include layout="@layout/contact"
           bind:user="@{user}"/>
   </merge>
</layout>
```
## 常用观察类
* ObservableBoolean
* ObservableByte
* ObservableChar
* ObservableShort
* ObservableInt
* ObservableLong
* ObservableFloat
* ObservableDouble
* ObservableParcelable
* ObservableField<T>

```java
private static class User extends BaseObservable {
    private String firstName;
    private String lastName;

    @Bindable
    public String getFirstName() {
        return this.firstName;
    }

    @Bindable
    public String getLastName() {
        return this.lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        //notifyChange();刷新所有值
        notifyPropertyChanged(BR.firstName);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }
}
```
kotlin使用`Observable`子类定义变量
```kotlin
class DataBindingEntity : BaseObservable() {
    @get:Bindable//这个bindable可以只放在get方法
    var test: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.test)
        }
}
```
## 生成绑定类
绑定类有几种方法
* DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
* MyLayoutBinding.inflate(layoutInflater)
* MyLayoutBinding.inflate(getLayoutInflater(), viewGroup, false)
* MyLayoutBinding.bind(viewRoot)
* val viewRoot = LayoutInflater.from(this).inflate(layoutId, parent, attachToParent); val binding: ViewDataBinding? = DataBindingUtil.bind(viewRoot)
如果要在`Fragment`、`ListView`、`RecyclerView`中使用DataBinding建议按以下方式添加
```kotlin
val listItemBinding = ListItemBinding.inflate(layoutInflater, viewGroup, false)
// or
val listItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, viewGroup, false)
```
如果使用DataBinding时不能直接使用`ViewStubs`需要使用`ViewStubProxy`  
当变量改变是但有需要立即绑定则可以使用`executePendingBindings()`

可以在引用`ViewStub`地方使用
```xml
<ViewStub
    android:id="@+id/vs1"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout="@layout/view_stub_base_data_1"
    bind:basedata="@{view.baseData}" />
```
来自动设置值，`basedata`是`view_stub_base_data_1`布局中使用到的变量，或者在调用`inflate()`方法之前设置`setOnInflateListener`监听然后手动设置值
```kotlin
vs1.setOnInflateListener { _, inflated ->
    val bind: ViewStubBaseData1Binding? = DataBindingUtil.bind(inflated)
    bind?.basedata = baseData
}
vs1.inflate()
```


## 高级绑定
### 动态绑定
```kotlin
override fun onBindViewHolder(holder: BindingHolder, position: Int) {
    item: T = items.get(position)
    holder.binding.setVariable(BR.item, item);
    holder.binding.executePendingBindings();
}
```
### 后台线程
可以在后台线程中改变数据模型，只要不是数据集合即可。DataBinding会评估每个字段变量避免并发问题
### 自定义绑定类名
默认绑定类名为布局文件去除`_`首字母大写`Binding`结尾的类，可以在使用以下方法自定义类名
```xml
<data class="ContactItem">
    …
</data>
```
或者指定完整包名
```xml
<data class="com.example.ContactItem">
    …
</data>
```
## 设置属性值
一般情况下都是使用`setXXX`设置变量值，但如果方法名不是`setXXX`则需要注解来设置
```kotlin
@BindingMethods(value = [
    BindingMethod(
        type = android.widget.ImageView::class,
        attribute = "android:tint",
        method = "setImageTintList")])

```
### 自定义逻辑
有时需要自定义一些属性值的操作，操作方法必须为静态方法
```kotlin
@BindingAdapter("android:paddingLeft")
fun setPaddingLeft(view: View, padding: Int) {
    view.setPadding(padding,
                view.getPaddingTop(),
                view.getPaddingRight(),
                view.getPaddingBottom())
}
```
```kotlin
@BindingAdapter("imageUrl", "error")
fun loadImage(view: ImageView, url: String, error: Drawable) {
    Picasso.get().load(url).error(error).into(view)
}

```
```xml
<ImageView app:imageUrl="@{venue.imageUrl}" app:error="@{@drawable/venueError}" />
```
### 对象转换
默认情况下DataBinding会将object转换为所选方法对应的数据类型  
### 自定义转换
```kotlin
@BindingConversion
fun convertColorToDrawable(color: Int) = ColorDrawable(color)
```
```xml
<View
   android:background="@{isError ? @drawable/error : @color/white}"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"/>
```
## 双向绑定
使用`@={}`来双向绑定数据
```xml
<CheckBox
    android:id="@+id/rememberMeCheckBox"
    android:checked="@={viewmodel.rememberMe}"
/>
```
不建议在EditText或其他可编辑的控件中使用数字属性（Int，Long等等）双向绑定，如果要使用可以使用以下方法
```kotlin
object Other {
    @BindingAdapter("android:text")
    @JvmStatic
    fun setText(view: EditText, value: Int) {
        if (view.text.toString() != value.toString()) {
            view.setText(Integer.toString(value))
        }
    }

    @InverseBindingAdapter(attribute = "android:text")
    @JvmStatic
    fun getText(view: EditText): Int? {
        val str = view.text.toString()
        return if (str.isEmpty()) {
            null
        } else {
            Integer.parseInt(str)
        }
    }
}
```
使用此方法后该编辑框不会出现无字符串情形至少会有个0，或使用
`app:onFocusChangeListener="@{(view, hasFocus) -> activity.setText(((EditText)view).getText().toString(), hasFocus)}"`、`app:addTextChangedListener="@{activity.textWatcher}"`等方法监听内容变化自行操作对象
### 自定义属性双向绑定
例如要对自定义类`MyView`的自定义`time`属性设置双向绑定
1. 注解@BindingAdapter设置初始化方法
```kotlin
@BindingAdapter("time")
@JvmStatic fun setTime(view: MyView, newValue: Time) {
    // 防止循环调用
    if (view.time != newValue) {
        view.time = newValue
    }
}
```
2. 使用@InverseBindingAdapter注解从View中读取值
```kotlin
@InverseBindingAdapter("time")
@JvmStatic fun getTime(view: MyView) : Time {
    return view.getTime()
}
```
### 设置自定义监听器
```kotlin
@BindingAdapter("app:timeAttrChanged")
@JvmStatic fun setListeners(
        view: MyView,
        attrChange: InverseBindingListener
) {
    // Set a listener for click, focus, touch, etc.
}
```

### 转换
如果绑定到View的变量需要在显示之前以魔咒方式进行格式化、翻译或更改可以使用Converter
```kotlin
object Converter {
    @InverseMethod("stringToDate")
    fun dateToString(
        view: EditText, oldValue: Long,
        value: Long
    ): String {
        // Converts long to String.
    }

    fun stringToDate(
        view: EditText, oldValue: String,
        value: String
    ): Long {
        // Converts String to long.
    }
}
```
```xml
<EditText
    android:id="@+id/birth_date"
    android:text="@={Converter.dateToString(viewmodel.birthDate)}"
/>
```
### 双向属性
以下属性由平台支持双向绑定

|类|属性|绑定类|
|-|-|-|
|AdapterView	|android:selectedItemPosition android:selection |AdapterViewBindingAdapter|
|CalendarView   |android:date                                   |CalendarViewBindingAdapter|
|CompoundButton	|android:checked                                |CompoundButtonBindingAdapter|
|DatePicker     |android:year android:month android:day	        |DatePickerBindingAdapter|
|NumberPicker	|android:value                                  |NumberPickerBindingAdapter|
|RadioButton	|android:checkedButton                          |RadioGroupBindingAdapter|
|RatingBar      |android:rating                                 |RatingBarBindingAdapter|
|SeekBar	    |android:progress	                            |SeekBarBindingAdapter|
|TabHost        |android:currentTab	                            |TabHostBindingAdapter|
|TextView       |android:text                                   |TextViewBindingAdapter|
|TimePicker	    |android:hour android:minute                    |TimePickerBindingAdapter|