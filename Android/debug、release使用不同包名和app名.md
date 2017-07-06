debug使用其他名称：首先在创建`\app\src\debug\res\values\strings.xml`然后在xml中添加debug版app名称。   
debug使用其他包名：在app目录下的`build.gradle`中添加buildTypes为debug类型然后使用`applicationIdSuffix '.debug'`添加包名后缀