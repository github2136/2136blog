# Android Studio3.0 flavorDimensions
https://developer.android.google.cn/studio/build/build-variants#workBuildVariants  
Android Studio升级3.0后多渠道提示
```
Error:All flavors must now belong to a named flavor dimension.Learn more at 
https://d.android.com/r/tools/flavorDimensions-missing-error-message.html
```
处理方法
* 在`app`的build.gradle中定义`flavorDimensions`并设置值
* 然后在`productFlavors`中对每个`flavor`指定`dimension`的值，该值必须是在`flavorDimensions`中定义过的值

```Groovy 
android {
    ...
    defaultConfig {...}
    buildTypes {
        debug{...}
        release{...}
    }
    // Specifies one flavor dimension.
    flavorDimensions "version"
    productFlavors {
        demo {
            // Assigns this product flavor to the "version" flavor dimension.
            // This property is optional if you are using only one dimension.
            dimension "version"
            applicationIdSuffix ".demo"
            versionNameSuffix "-demo"
        }
        full {
            dimension "version"
            applicationIdSuffix ".full"
            versionNameSuffix "-full"
        }
    }
}
```