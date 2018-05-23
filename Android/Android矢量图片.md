### Android 矢量图片显示
https://developer.android.com/studio/write/vector-asset-studio

在Android5.0（Lollipop API21）后新增对矢量图标的显示。
**Android不能直接使用SVG、PSD文件**
使用AS将SVG文件转换为Android可以使用的vector
![图1](/Android/Android矢量图片/1.png)   
转换结果
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
        android:width="24dp"
        android:height="24dp"
        android:viewportWidth="1024.0"
        android:viewportHeight="1024.0">
    <path
        android:pathData="M308.5,417.6 L238.9,487.2 462.5,710.7 959.4,213.9 889.8,144.3 462.5,571.6 308.5,417.6Z"
        android:fillColor="#1295DA"/>
    <path
        android:pathData="M860,859.8 L164.4,859.8 164.4,164.2l496.9,0L661.3,64.8 164.4,64.8c-54.7,0 -99.4,44.7 -99.4,99.4l0,695.6c0,54.7 44.7,99.4 99.4,99.4l695.6,0c54.7,0 99.4,-44.7 99.4,-99.4L959.4,462.3l-99.4,0L860,859.8 860,859.8z"
        android:fillColor="#1295DA"/>
</vector>

```
**兼容旧版SDK**
Android 4.4（API 级别 20）及更低版本不支持矢量图。如果最低 API 级别设置为上述 API 级别之一，则在使用 Vector Asset Studio 时您有两个选择：生成便携式网络图形 (PNG) 文件（默认）或使用支持库。

为实现向后兼容性，Vector Asset Studio 会生成矢量图的光栅图像。矢量和光栅图一起打包到 APK 中。您可以在 Java 代码中以 Drawable 的形式引用矢量图，或在 XML 代码中以 @drawable 的形式引用矢量图；当您的应用运行时，对应的矢量或光栅图像会自动显示，具体取决于 API 级别。

如果您只想使用矢量图，可以使用 Android 支持库 23.2 或更高版本。要使用此技术，您需要在运行 Vector Asset Studio 之前按照支持库向后兼容性中的说明更改 build.gradle 文件。利用支持库中的 VectorDrawableCompat 类，可实现在 Android 2.1（API 级别 7）及更高版本中支持 VectorDrawable。

对于 Android 5.0（API 级别 21）及更高版本，您可以使用 AnimatedVectorDrawable 类为 VectorDrawable 类的属性添加动画。有了支持库，您可以使用 AnimatedVectorDrawableCompat 类为 Android 3.0（API 级别 11）及更高版本的 VectorDrawable 类添加动画。如需了解详细信息，请参阅为矢量图添加动画。

#### 生成PNG文件
```
defaultConfig {
    vectorDrawables.generatedDensities = ['hdpi','xxhdpi']
}
```
#### 支持库
```
defaultConfig {
    vectorDrawables.useSupportLibrary = true
}
```
当使用支持库时使用矢量图必须使用`app:srcCompat` 属性，而不是 `android:src`
#### SVG 和 PSD 文件注意事项

矢量图适用于简单的图标。Material 图标是适合在应用中用作矢量图的图像类型的一个好例子。相比之下，许多应用的启动图标包含许多细节，因此更适合用作光栅图像。

与对应的光栅图像相比，矢量图首次加载时可能消耗更多的 CPU 资源。之后，二者的内存使用率和性能则不相上下。我们建议您将矢量图像限制为最大 200 x 200 dp；否则，绘制它可能需要耗费很长的时间。

尽管矢量图确实支持一种或多种颜色，但在很多情况下，最好将图标设置为黑色 (android:fillColor="#FF000000")。通过此方法，您可以为布局中放置的矢量图添加 tint 属性，图标颜色将随之变为 tint 颜色。如果图标颜色不是黑色，图标颜色可能反而与 tint 颜色较为搭配。
#### PSD 
Vector Asset Studio 并非支持所有 PSD 文件功能。以下列表归纳了支持和不支持的 PSD 特性以及部分转换详情。