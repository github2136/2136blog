## AndroidX
https://developer.android.google.cn/jetpack/
https://developer.android.google.cn/jetpack/androidx/
AndroidX是用来替代旧版support库AndroidX和support一样和Android操作系统分开，并提供跨Android版本的兼容，AndroidX存放在Android Jetpack 里
* AndroidX所有包都已androidx开头位于同一命名空间下，support已经映射到相应的androidx.*包中
* 与support包不同AndroidX是单独维护和更新的，使用不同AndroidX项目不需要统一版本号
* 所有新support都将在AndroidX中添加维护
### 使用AndroidX
如果需要使用AndroidX需要设置compile SDK 为Android9.0（api28）或更高，并在gradle.properties中将以下两个标志设置为true
* android.useAndroidX：设置为true时Android插件使用相应的AndroidX库而不是support库，默认为false
* android.enableJetifier：设置为true时，Android插件会自动迁移现有第三方库通过重写二进制文件来使用AndroidX，默认为false

**如果不添加有可能会出现**
```
Manifest merger failed : Attribute application@appComponentFactory value=(android.support.v4.app.CoreComponentFactory) from [com.android.support:support-compat:28.0.0] AndroidManifest.xml:22:18-91
	is also present at [androidx.core:core:1.0.1] AndroidManifest.xml:22:18-86 value=(androidx.core.app.CoreComponentFactory).
	Suggestion: add 'tools:replace="android:appComponentFactory"' to <application> element at AndroidManifest.xml:5:5-19:19 to override.
```
