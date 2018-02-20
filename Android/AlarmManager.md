# AlarmManager
AlarmManager是一个全局的警报管理，可以在指定时间启动Service、Activity、BroadcastReceiver
**注意**从API19开始报警将是不准确的
```
//初始化
AlarmManager mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
Intent intent = new Intent(this, UDPService.class);
mPendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

private void startAlarm(PendingIntent mPendingIntent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + mAlarmDelayed, mPendingIntent);
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        mAlarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + mAlarmDelayed, mPendingIntent);
    } else {
        mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + mAlarmDelayed, mPendingIntent);
    }
}
```