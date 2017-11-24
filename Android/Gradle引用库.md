## Gradlei插件版本
https://developer.android.google.cn/studio/releases/gradle-plugin.html#updating-plugin
### 3.0以下  
https://developer.android.google.cn/studio/build/dependencies.html#dependency-configurations
* `compile`：将依赖项编译至APK中  
* `apk`：只将依赖项添加至APK中（它不会被添加到编译类路径中）`apk`只能用于jar包，不支持module或AAR  
* `provided`：仅将依赖项添加到编译类路径（它不会添加到APK中）。这在创建Android库模块时非常有用，在编译期间有依赖关系但运行时是可选的，如引用AAR时资源文件将不可使用。

### 3.0
https://developer.android.google.cn/studio/build/gradle-plugin-3-0-0-migration.html#new_configurations
* `implementation`：与之前的`compile`相似，区别在于这个依赖不会传递给其他module，大多数app和test的module应使用这个
* `api`：与之前的`compile`相同
* `compileOnly`：与之前的`provided`相同
* `runtimeOnly`：与之前的`apk`相同