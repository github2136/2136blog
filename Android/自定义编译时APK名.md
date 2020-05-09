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
            outputFileName = "${var.productFlavors[0].name}-${var.name}-${var.versionName}-${var.versionCode}-${releaseTime()}.apk"
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

* `var.productFlavors[0].name`：Flavors的name 
* `var.name`：`Build Variants`中显示的`Active Build Variant`
* `var.versionName`：显示的版本名称
* `var.versionCode`：版本号
* `releaseTime`：时间

示例：**dev-devRelease-1.0-1-20200509152127.apk**