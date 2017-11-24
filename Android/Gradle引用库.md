## Gradle引用库
### Gradle3.0以下  
https://developer.android.google.cn/studio/build/dependencies.html#dependency-configurations
* `compile`：将依赖项编译至APK中  
* `apk`：只将依赖项添加至APK中（它不会被添加到编译类路径中）`apk`只能用于jar包，不支持module或AAR  
* `provided`：仅将依赖项添加到编译类路径（它不会添加到APK中）。这在创建Android库模块时非常有用，在编译期间有依赖关系但运行时是可选的，如引用AAR时资源文件将不可使用。

### Gradle3.0
https://developer.android.google.cn/studio/build/gradle-plugin-3-0-0-migration.html#new_configurations