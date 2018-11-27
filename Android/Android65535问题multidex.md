## Android65535问题
https://developer.android.google.cn/studio/build/multidex
当Android中方法超过65536个时编译apk时将会有个错误   
处理具体步骤
* 如果是`minSdkVersion`为5.0（api21）及之后只需要在build.gradle中添加`multiDexEnabled true`
```
android {
    defaultConfig {
        ...
        minSdkVersion 21 
        targetSdkVersion 28
        *multiDexEnabled true*
    }
    ...
}
```
* 如果是`minSdkVersion`（api20）及之前的版本需要添加`multiDexEnabled true`和`androidx.multidex:multidex:2.0.0`
> * 如果没有替换`Application`直接在`manifest`的`application`节点添加`android:name="android.support.multidex.MultiDexApplication"`
> * 如果替换了`Application`则继承`MultiDexApplication`并添加到`manifest`
> * 如果替换`Application`类，但无法更改基类，则修改`attachBaseContext()`并调用`MultiDex.install(this)`