adb命令
---

* `adb shell dumpsys activity activities`查看当前activity堆栈

    ```
    Display #0 (activities from top to bottom):
      Stack #1:
      mFullscreen=true
      isSleeping=false
      mBounds=null
        Task id #6411
        mFullscreen=true
        mBounds=null
        mMinWidth=-1
        mMinHeight=-1
        mLastNonFullscreenBounds=null
        * TaskRecord{a52660a #6411 A=com.android.settings U=0 StackId=1 sz=4}
          userId=0 effectiveUid=1000 mCallingUid=u0a15 mUserSetupComplete=true mCallingPackage=com.android.launcher3
          affinity=com.android.settings
          intent={act=android.intent.action.MAIN cat=[android.intent.category.LAUNCHER] flg=0x10200000 cmp=com.android.settings/.Settings}
    ……
    ```
    
    `cmp=com.android.settings/.Settings`就是当前显示的Activity
    
* `adb shell dumpsys activity top`可以查看当前顶部activity

