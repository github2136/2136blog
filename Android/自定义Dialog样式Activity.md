自定义Dialog样式Activity
===

要自定义Dialog样式Activity首先创建一个Activity然后设置Activity的主题为`android:theme="@style/Theme.AppCompat.Dialog"`这就是一个默认样式的Dialog
![图1](自定义Dialog样式Activity\1.png)

使用`android:theme="@style/Theme.AppCompat.Light.Dialog"`可以修改为亮色Dialog

如果要隐藏标题栏则需要创建Style样式

```
<style name="Dialog" parent="Theme.AppCompat.Light.Dialog">
    <!--背景颜色-->
    <item name="android:windowBackground">@drawable/abc_dialog_material_background</item>
    <!--标题栏-->
    <item name="windowNoTitle">false</item>
    <!--边框-->
    <item name="android:windowFrame">@null</item>
    <!--是否浮现在activity之上-->
    <item name="android:windowIsFloating">true</item>
    <!--是否有覆盖-->
    <item name="android:windowContentOverlay">@null</item>
    <!--弹出动画-->
    <item name="android:windowAnimationStyle">@style/Animation.AppCompat.Dialog</item>
    <!--背景是否模糊显示-->
    <item name="android:backgroundDimEnabled">true</item>
    <!--点击dialog外面关闭dialog-->
    <item name="android:windowCloseOnTouchOutside">true</item>
</style>
```

如果要控制dialog弹出位置和尺寸可以在`onCreate`方法中添加

```java
//底部展示
window.setGravity(Gravity.BOTTOM)
//横向全屏
window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
```