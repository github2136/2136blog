Android Studio配置
===
默认Android和Gradle的缓存都在C盘，可以使用以下方法修改默认位置
* 配置`ANDROID_SDK_HOME`例如`D:\Work\IDE\Android\sdk`强烈建议设置在实际`SDK`目录下（在C盘.android还会有adbkey和adbkey.pub两个文件）
* 配置`GRADLE_USER_HOME`环境变量例如`D:\Work\IDE\cache\.gradle`将.gradle文件夹移动到指定位置或者修改AS的Gradle配置目录
* 找到AS bin目录下\idea.properties文件夹修改.AndroidStudio3.5位置
 添加
 `idea.config.path=移动后, 你的config文件夹路径`
 `idea.system.path=移动后, 你的system文件夹路径`
 例如:
 `idea.config.path=E:/ide/AndroidStudioSdk/.AndroidStudio3.5/config`
 `idea.system.path=E:/ide/AndroidStudioSdk/.AndroidStudio3.5/system`
 C盘还会有.AndroidStudioxx/system空文件夹