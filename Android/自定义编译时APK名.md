# 自定义编译时APK名

首先修改项目`build.gradle`文件

```
apply plugin: 'com.android.application'
……
android {
    signingConfigs {
        release {
            versionCode 1
            versionName "1.0"
            ……
        }
    }
    buildTypes {
        release {
            ……
        }
    }
    flavorDimensions "def"
    productFlavors {
        dev {
            ……
            dimension "def"
        }
    }
    android.applicationVariants.all { var ->
        var.outputs.all {
            def flavorName = ""
            for (f in var.productFlavors) {
                flavorName += f.name
            }
            outputFileName = "${flavorName}-${var.buildType.name}-${var.name}-${var.versionName}-${var.versionCode}-${releaseTime()}.apk"
        }
    }
}

def releaseTime() {
    return new Date().format("yyyyMMddHHmmss", TimeZone.getTimeZone("GMT+08"))
}

dependencies {
	……
}

```

* `flavorName`：Flavors的name 如果只有一个Flavors可以使用`var.productFlavors[0].name`
* `${var.buildType.name}`：buildTypes的name
* `var.name`：`Build Variants`中显示的`Active Build Variant`（flavor1flavor2...buildType）
* `var.versionName`：显示的版本名称
* `var.versionCode`：版本号
* `releaseTime`：时间

示例：**money1channel1-debug-money1Channel1Debug-1.0-1-20200511141148.apk**