## ToolBar菜单图标颜色
* 方法1 
直接使用彩色图标
* 方法2
使用矢量图标
添加支持
```
defaultConfig {
    vectorDrawables.useSupportLibrary = true
}
```
给Toolbar设置主题
```xml
<style name="Picker.ToolBar" parent="@style/ThemeOverlay.AppCompat.Dark">
    <!--ToolBar背景颜色-->
    <item name="colorPrimary">#597cf9</item>
    <!--状态栏颜色-->
    <item name="colorPrimaryDark">#597cf9</item>
    <!--文字颜色-->
    <item name="android:textColorPrimary">#fff000</item>
    <!--图标颜色-->
    <item name="android:tint">#dc0cef</item>
</style>
```
在`drawable`中添加`vector`默认图片
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
        android:width="24dp"
        android:height="24dp"
        android:viewportHeight="24.0"        
        android:viewportWidth="24.0">
    <path
        android:fillColor="#FF000000"
        android:pathData="M19,13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/>
</vector>
```
在`drawable-v21`中添加`vector`高版本图片
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
        android:width="24dp"
        android:height="24dp"
        android:viewportHeight="24.0"
        android:tint="?android:tint"
        android:viewportWidth="24.0">
    <path
        android:fillColor="#FF000000"
        android:pathData="M19,13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/>
</vector>
```
在v21目录下使用`android:tint="?android:tint"`可以在高版本中正确的为矢量图添加颜色