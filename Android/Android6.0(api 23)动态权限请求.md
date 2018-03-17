# Android动态权限请求
https://developer.android.google.cn/guide/topics/permissions/index.html

从 Android 6.0（API 级别 23）开始，用户开始在应用运行时向其授予权限，而不是在应用安装时授予。此方法可以简化应用安装过程，因为用户在安装或更新应用时不需要授予权限。它还让用户可以对应用的功能进行更多控制；例如，用户可以选择为相机应用提供相机访问权限，而不提供设备位置的访问权限。用户可以随时进入应用的“Settings”屏幕调用权限。

**当授权的权限属于同一权限组时，会在一个请求中提示是否允许**

以下危险权限必须动态授权

|权限组|权限|
|-|-|
|CALENDAR   |READ_CALENDAR|
|           |WRITE_CALENDAR|
|CAMERA     |CAMERA|
|CONTACTS   |READ_CONTACTS|
|           |WRITE_CONTACTS|
|           |GET_ACCOUNTS|
|LOCATION   |ACCESS_FINE_LOCATION|
|           |ACCESS_COARSE_LOCATION|
|MICROPHONE |RECORD_AUDIO|
|PHONE      |READ_PHONE_STATE|
|           |READ_PHONE_NUMBERS|
|           |CALL_PHONE|
|           |ANSWER_PHONE_CALLS (must request at runtime)|
|           |READ_CALL_LOG|
|           |WRITE_CALL_LOG|
|           |ADD_VOICEMAIL|
|           |USE_SIP|
|           |PROCESS_OUTGOING_CALLS|
|           |ANSWER_PHONE_CALLS|
|           |READ_PHONE_NUMBERS|
|SENSORS    |BODY_SENSORS|
|SMS        |SEND_SMS|
|           |RECEIVE_SMS|
|           |READ_SMS|
|           |RECEIVE_WAP_PUSH|
|           |RECEIVE_MMS|
|STORAGE    |READ_EXTERNAL_STORAGE|
|           |WRITE_EXTERNAL_STORAGE|

权限请求
```java
//请求权限
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    //检查权限
    int permission = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
    //允许
    //PackageManager.PERMISSION_GRANTED
    //拒绝
    //PackageManager.PERMISSION_DENIED
    //请求权限
    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECORD_AUDIO}, 10);
}
```
响应
```java
//权限响应
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    //requestCode请求编码
    //permissions请求的权限
    //grantResults授予结果
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    // 版本兼容
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M)
        return;

    for (int i = 0, len = permissions.length; i < len; i++) {
        String permission = permissions[i];
        //  拒绝的权限
        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
            //判断是否点击不再提示
            boolean showRationale = shouldShowRequestPermissionRationale(permission);
            if (!showRationale) {
                // 用户点击不再提醒，打开设置页让用户开启权限
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                break;
            } else {
                // 用户点击了取消...
                Toast.makeText(this, "没有对应权限无法正常使用", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
```
