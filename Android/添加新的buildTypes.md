添加新的buildTypes必须先添加一个新的签名，首先将签名文件添加至`\app\keystore\`目录下，然后添加签名文件代码
```
signingConfigs {
    debug {
        storeFile file("/keystore/debug.keystore")
        storePassword "android"
        keyAlias "androiddebugkey"
        keyPassword "android"
    }
}
```
在buildTypes中添加新的类型
```
_test {
    applicationIdSuffix '.test'    
    signingConfig signingConfigs.debug
}
```