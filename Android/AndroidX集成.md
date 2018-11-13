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