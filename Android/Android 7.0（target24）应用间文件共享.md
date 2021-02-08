# Android 7.0（target24）应用间文件共享
7.0后禁止使用`file://`的URI必须使用`content://`不然会提示`FileUriExposedException`异常  
文件共享方法
1.  首先添加`file_paths.xml`到`res/xml`中文件名可自定义
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <paths>
        <external-path
            name="external_files"
            path="NaturalReserve/video/"  />
    </paths>
    ```
    `name`uri路径段，用于隐藏真实文件路径`path`共享子目录，该路径为目录路径不可指定为单个文件`<paths>`下面节点分为5种，可以有多个共享目录
    |类型|说明|路径|
    |-|-|-|
    |files-path|context.getFilesDir()|/data/user/0/包名/files|
    |cache-path|context.getCacheDir()|/data/user/0/包名/cache|
    |external-path|Environment.getExternalStorageDirectory()|/storage/emulated/0|
    |external-files-path|context.getExternalFilesDir(String)或context.getExternalFilesDir(null)|/storage/emulated/0/Android/data/包名/files|
    |external-cache-path|context.getExternalCacheDir()|/storage/emulated/0/Android/data/包名/cache|
    |external-media-path|context.getExternalMediaDirs()该路径只在api21以上才有|/storage/emulated/0/Android/media/包名|

1. 添加`provider`到`Manifest`
    ```xml
    <!-- android:name="androidx.core.content.FileProvider" -->
    <provider
        android:name="android.support.v4.content.FileProvider"
        android:authorities="xxxx.fileprovider"
        android:exported="false"
        android:grantUriPermissions="true">
        <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths" />
    </provider>
    ```
    `name`可使用固定值`android.support.v4.content.FileProvider`，`authorities`可设置为`${applicationId}`或`${applicationId}.fileprovider`该值在同一台手机中不可重复，`exported`固定为`false`，`grantUriPermissions`固定为`true`。`meta-data`中`name`固定`android.support.FILE_PROVIDER_PATHS`，`resource`为共享文件xml的路径`@xml/file_paths`
1. 生成共享uri
   ```java
   File imagePath = new File(Context.getFilesDir(), "images");
   File newFile = new File(imagePath, "default_image.jpg");
   Uri contentUri = FileProvider.getUriForFile(context, "com.mydomain.fileprovider", newFile);
   ```
1. 授予临时权限
   对于从`getUriForFile`获取到的uri可以执行以下操作之一   
   * 使用`Context.grantUriPermission(package, Uri, mode_flags)`通过mode_flags的值来指定临时目录的权限，权限可以是`FLAG_GRANT_READ_URI_PERMISSION`、`FLAG_GRANT_WRITE_URI_PERMISSION`或两者皆有。权限目录会直到调用`revokeUriPermission()`或重启手机来撤销
   * 通过`setData()`来设置要传递uri，然后通过`Intent.setFlags()`来设置`FLAG_GRANT_READ_URI_PERMISSION`、`FLAG_GRANT_WRITE_URI_PERMISSION`的权限