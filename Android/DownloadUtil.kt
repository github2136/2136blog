package com.jxgis.reserve.util

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri

/**
 * Created by YB on 2019/5/9
 */
object DownloadUtil {
    lateinit var app: Application
    lateinit var downloadManager: DownloadManager
    fun init(app: Application) {
        this.app = app
    }

    fun download(url: String, dir: String, fileName: String): Long {
        return if (::app.isInitialized) {
            val service = app.getSystemService(Context.DOWNLOAD_SERVICE)
            if (service != null) {
                //系统没有下载服务
                downloadManager = service as DownloadManager
                val download = DownloadManager.Request(Uri.parse(url))
                //是否显示在系统下载UI里面默认显示
                download.setVisibleInDownloadsUi(false)
                //下载目录
                download.setDestinationInExternalPublicDir(dir, fileName)
                //通知栏是否显示
                download.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
                //下载网络限制
                //download.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                //是否允许通过流量下载
                //download.setAllowedOverMetered(true)
                downloadManager.enqueue(download)
            } else {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.data = Uri.parse(url)
                app.startActivity(intent)
                -1
            }
        } else {
            -1
        }
    }

    fun query(downloadId: Long): Cursor? {
        //获取下载完成后本地地址
        //val uri = getString(getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
        //File(URI.create(uri))
        return if (::downloadManager.isInitialized) {
            val query = DownloadManager.Query()
            query.setFilterById(downloadId)
            val c = downloadManager.query(query)
            if (c.moveToFirst()) {
                c
            } else {
                null
            }
        } else {
            null
        }
    }
}