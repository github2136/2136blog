# APP每次点击图标重启

如果出现打包后使用HOME键跳转到主页然后点击APP图标后重新打开启动页，但从多任务页面打开正常时可以使用以下几个方法
**首先启动页必须是默认`launchMode`**

* 在启动页`onCreate中添加`

  ```kotlin
  if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
      finish()
      return
  }
  ```

* 给`Manifest`中启动页节点添加

  ```xml
  android:alwaysRetainTaskState="true"
  android:clearTaskOnLaunch="false"
  ```

* 重启手机

