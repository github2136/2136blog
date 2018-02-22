## 系统下载管理类
```java
//        下载管理对象
        DownloadManager dmManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (dmManager != null) {
//          文件下载地址
            Uri uri = Uri.parse("http://d1.music.126.net/dmusic/cloudmusicsetup_2_3_0_196231.exe");
//          下载对象
            DownloadManager.Request download = new DownloadManager.Request(uri);
            download.setShowRunningNotification(false);
//          是否显示在系统下载UI里面默认显示
            download.setVisibleInDownloadsUi(true);
//          文件保存位置 dirType下载目录如果为 “”则表示为根目录，目录只会创建一级，如果存放在多级目录下则必须事先创建否则报错
//          外部存储目录
//            download.setDestinationInExternalPublicDir("111111", "cloudmusicsetup_2_3_0_196231.exe");
//            存储目录URI
//            download.setDestinationUri(Uri.parse(""));
//            外部私有目录不需要文件写入权限
            download.setDestinationInExternalFilesDir(this, "111111", "cloudmusicsetup_2_3_0_196231.exe");
//          显示标题
            download.setTitle("title");
//          显示说明
            download.setDescription("Description desc");
//           下载通知等级默认只会在下载时显示
//           VISIBILITY_HIDDEN：不显示需要android.permission.DOWNLOAD_WITHOUT_NOTIFICATION权限
//           VISIBILITY_VISIBLE：下载时显示默认值
//           VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION：只在完成时显示仅适用addCompletedDownload（String，String，boolean，String，String，long，boolean
//           VISIBILITY_VISIBLE_NOTIFY_COMPLETED：下载和完成时显示
            download.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//          选择允许下载的网络类型默认所有都允许,建议使用setAllowedOverMetered(boolean)NETWORK_WIFI等于setAllowedOverMetered(false)
            download.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
//          设置此下载是否可以通过计量的网络连接进行。 默认情况下，允许测量网络
//          download.setAllowedOverMetered(true);
//          设置文件类型用于文件下载完成点击通知栏操作
            download.setMimeType("application/cn.trinea.download.file");
//            下载请求头
//            download.addRequestHeader("","");
//            进入下载队列返回下载ID
            long downloadId = dmManager.enqueue(download);
        }
```
监听下载完成
```java
registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//广播返回的ID
intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
```
查看下载器状态
```java
private void queryDownloadStatus() {
DownloadManager.Query query = new DownloadManager.Query();
query.setFilterById(downloadId);
Cursor c = dmManager.query(query);
if(c.moveToFirst()) { 
        int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
        switch(status) {
        case DownloadManager.STATUS_PAUSED:
                Log.v("down", "STATUS_PAUSED");
        case DownloadManager.STATUS_PENDING:
                Log.v("down", "STATUS_PENDING");
        case DownloadManager.STATUS_RUNNING:
                //正在下载，不做任何事情
                Log.v("down", "STATUS_RUNNING");
                break;
        case DownloadManager.STATUS_SUCCESSFUL:
                //完成
                Log.v("down", "下载完成");
                break;
        case DownloadManager.STATUS_FAILED:
                //清除已下载的内容，重新下载
                Log.v("down", "STATUS_FAILED");
                break;
        }
}
}
```