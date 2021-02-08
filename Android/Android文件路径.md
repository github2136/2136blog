# Android文件路径

| 代码                                                  | 最低版本 | 路径                                        | 说明                                                         |
| ----------------------------------------------------- | -------- | ------------------------------------------- | ------------------------------------------------------------ |
| context.getDir(String name, int mode)                 | 1        | /data/user/0/包名/app_xx                    | name:表示文件夹名称；mode:表示操作模式默认为：Context.MODE_PRIVATE |
| context.getFilesDir()                                 | 1        | /data/user/0/包名/files                     | 内部私有文件目录openFileOutput（String，int）                |
| context.getCacheDir()                                 | 1        | /data/user/0/包名/cache                     | 内部私有缓存目录                                             |
| context.getDataDir()                                  | 24       | /data/user/0/包名                           | 内部私有目录                                                 |
| context.getCodeCacheDir()                             | 21       | /data/user/0/包名/code_cache                | 返回应用代码缓存文件                                         |
| context.getNoBackupFilesDir()                         | 21       | /data/user/0/包名/no_backup                 | 类似于getFilesDir()，但这个部分内容不会被备份                |
| context.getExternalFilesDirs()                        | 19       | /storage/emulated/0/Android/data/包名/files | 返回外部文件存储目录集合，可能为空，getExternalFilesDir(String) |
| context.getExternalCacheDirs()                        | 19       | /storage/emulated/0/Android/data/包名/cache | 返回外部缓存目录集合，可能为空，getExternalCacheDir()        |
| context.getExternalMediaDirs()                        | 21       | /storage/emulated/0/Android/media/包名      | 外部媒体文件夹集合，可能为空                                 |
| context.getExternalFilesDir(String)                   | 8        | /storage/emulated/0/Android/data/包名/files | 外部私有文件目录，参数可为下一级目录可以为空；返回的值可能为空 |
| context.getExternalCacheDir()                         | 8        | /storage/emulated/0/Android/data/包名/cache | 外部私有缓存目录，可能为空                                   |
| context.getObbDir()                                   | 11       | /storage/emulated/0/Android/obb/包名        | 返回obb文件夹，通常游戏会使用这个目录，卸载APP不会删除该目录内容，返回的文件可能为空 |
| context.getObbDirs()                                  | 19       | /storage/emulated/0/Android/obb/包名        | 返回obb文件夹集合，可能为空，第一个与getObbDir值相同         |
| Environment.getDataDirectory()                        | 1        | /data                                       | 返回用户数据目录                                             |
| Environment.getDownloadCacheDirectory()               | 1        | /data/cache                                 | 外部下载或缓存目录                                           |
| Environment.getExternalStoragePublicDirectory(String) | 8        | /storage/emulated/0/xx                      | 指定类型的外部存储目录                                       |
| Environment.getRootDirectory()                        | 1        | /system                                     | 返回系统分区目录，始终只读                                   |
| Environment.getExternalStorageDirectory()             | 1        | /storage/emulated/0                         | 外部存储根目录                                               |

* 下级目录可以使用`Environment.DIRECTORY_XXX`相关变量
* Environment相关目录都需要文件读取权限才能操作
* Android 10以后默认不允许随意操作外部存储