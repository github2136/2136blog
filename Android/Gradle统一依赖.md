Gradle统一依赖
===

首先在项目根目录添加`config.gradle`文件（名字可自定义），然后在根目录的`build.gradle`添加`apply from:"config.gradle"`

```groovy
//统一配置
ext {
    xxx = '1.3.41'
    android = [
            compileSdkVersion: 28,
            minSdkVersion    : 21,
            targetSdkVersion : 28,
    ]
    dependencies = [
            //kotlin相关
            'kotlin-stdlib'       : "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$xxx",//如果里面有变量则要使用双引号
            //日志打印
            'logger'              : 'com.orhanobut:logger:2.2.0',
            //网络请求
            'okhttp'              : 'com.squareup.okhttp3:okhttp:4.0.1',
            'okhttp-retrofit'     : 'com.squareup.retrofit2:retrofit:2.6.1',
            'okhttp-gson'         : 'com.squareup.retrofit2:converter-gson:2.6.1'
    ]
}
```

在`config.gradle`或根目录`build.gradle`里声明的`ext.xxxx`变量可以使用`$`来调用。  

在`module`的`build.gradle`中可以直接使用`rootProject.ext.dependencies['kotlin-stdlib']`来引用依赖或者在顶部声明使用

```groovy
def lib = rootProject.ext.dependencies
dependencies {
    implementation lib.kotlinstdlib//如果使用这种写法，在config.gradle中定义名字时不能使用-
    implementation lib['logger']
}
```

