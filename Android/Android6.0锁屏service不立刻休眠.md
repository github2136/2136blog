# Android6.0锁屏service不立刻休眠
https://www.oudahe.com/p/40029/

添加权限：`<uses-permission android:name="android.permission.WAKE_LOCK"/>`

给Activity加代码
```java
PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "myservice");
wl.acquire();
```
在onDestory中释放`wl.release();`