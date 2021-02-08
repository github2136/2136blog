# Android 10.0（target 29） 文件读取

默认情况下，对于以 Android 10 及更高版本为目标平台的应用，其访问权限范围限定为外部存储，即分区存储。此类应用可以查看外部存储设备内以下类型的文件，无需请求任何与存储相关的用户权限：

- 特定于应用的目录中的文件（使用 `getExternalFilesDir()`) 访问）。
- 应用创建的照片、视频和音频片段（通过媒体库)访问）。

如果要操作非自己项目目录操作方法有

* 修改`targetSdkVersion`为29以下
* 在`Androidmainfest `的`application`添加`android:requestLegacyExternalStorage="true"`

