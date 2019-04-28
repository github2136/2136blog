package com.jxgis.reserve.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.provider.Settings
import android.util.ArrayMap
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.github2136.base.BaseActivity
import com.github2136.base.util.Constant
import com.jxgis.reserve.R
import com.jxgis.reserve.presenter.LaunchPresenter
import java.util.*


/**
 * 启动页
 */
class LaunchActivity : BaseActivity<LaunchPresenter>() {
    val permissionArrayMap = ArrayMap<String, String>()
    var startTime = 0L
    val loadTime = 3000L
    override fun getLayoutId() = R.layout.activity_launch

    override fun initData(savedInstanceState: Bundle?) {
        permissionArrayMap[Manifest.permission.ACCESS_FINE_LOCATION] = "定位"
        permissionArrayMap[Manifest.permission.READ_PHONE_STATE] = "获取手机信息"
        permissionArrayMap[Manifest.permission.WRITE_EXTERNAL_STORAGE] = "读写手机存储"
        permissionArrayMap[Manifest.permission.CAMERA] = "拍摄照片和视频"
        permissionArrayMap[Manifest.permission.RECORD_AUDIO] = "录音"
        //请求权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //允许
            //PackageManager.PERMISSION_GRANTED
            //拒绝
            //PackageManager.PERMISSION_DENIED
            var permissionStatus = PackageManager.PERMISSION_GRANTED
            //检查权限
            for (per in permissionArrayMap) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                    //有拒绝权限
                    permissionStatus = PackageManager.PERMISSION_DENIED
                    break
                }
            }
            if (permissionStatus == PackageManager.PERMISSION_DENIED) {
                requestPermission()

            } else {
                next()
            }
        } else {
            next()
        }
    }

    //请求权限
    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    *permissionArrayMap.keys.toTypedArray()
                ), 1
            )
        }
    }

    private fun next() {
        startTime = Date().time
        if (mPresenter.mSpUtil.contains(Constant.SP_USER_ID)) {
            mPresenter.login()
        } else {
            mHandler.sendEmptyMessageDelayed(0, loadTime)
        }
    }

    override fun onBackPressed() {}

    override fun handleMessage(msg: Message) {
        when (msg.what) {
            0 -> {
                finish()
                startActivity(Intent(this, LoginActivity::class.java))
            }
            1 -> {
                val endTime = Date().time
                val t = loadTime - (endTime - startTime)
                if (t > 0) {
                    mHandler.sendEmptyMessageDelayed(1, t)
                } else {
                    finish()
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //requestCode请求编码
        //permissions请求的权限
        //grantResults授予结果
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            return
        }
        var allow = true
        val permissionStr = StringBuilder("缺少")
        for ((i, permission) in permissions.withIndex()) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                allow = false
                permissionStr.append(" ${permissionArrayMap[permission]}")
                //判断是否点击不再提示
                val showRationale = shouldShowRequestPermissionRationale(permission)
                if (!showRationale) {
                    // 用户点击不再提醒，打开设置页让用户开启权限
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                    break
                }
            }
        }
        permissionStr.append(" 权限，重新请求权限")
        if (allow) {
            next()
        } else {
            // 用户点击了取消...
            AlertDialog.Builder(this)
                .setTitle("警告")
                .setMessage(permissionStr)
                .setPositiveButton("请求权限") { _, _ -> requestPermission() }
                .setNegativeButton("关闭应用") { _, _ -> finish() }
                .show()
        }
    }

    override fun initObserve() {
        mPresenter.ldLogin.observe(this, Observer { t ->
            if (t.status == 10000) {
                mHandler.sendEmptyMessage(1)
            } else {
                t.msg?.let {
                    showToast(it)
                }
                mHandler.sendEmptyMessage(0)
            }
        })
    }
}
