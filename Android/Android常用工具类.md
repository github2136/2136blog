## Android常用工具类
**以API26说明**  
### android.text.TextUtils 文字工具类
* concat(CharSequence... text) 文字拼接
 ```java
 CharSequence str = TextUtils.concat("a", "b");
 ```
>str结果为ab
* copySpansFrom(Spanned source, int start, int end, Class kind, Spannable dest, int destoff)  
* ellipsize(CharSequence text, TextPaint p, float avail, TruncateAt where) 文字裁剪
```java
CharSequence str = TextUtils.ellipsize("Returns the original text if it fits in the specified width given the properties of the specified Pain",
                tv1.getPaint(), 150.0f, TextUtils.TruncateAt.MIDDLE);
```
> str结果为Returns...d Pain
通过TruncateAt参数来控制省略号在哪个位置
* ellipsize(CharSequence text, TextPaint paint, float avail, TruncateAt where, boolean preserveLength, EllipsizeCallback callback) 文字裁剪
```java
TextUtils.EllipsizeCallback callback = new TextUtils.EllipsizeCallback() {
    @Override
    public void ellipsized(int start, int end) {
        Log.e("tag", start + " " + end);
    }
};
CharSequence str2 = TextUtils.ellipsize("Returns the original text if it fits in the specified width given the properties of the specified Pain",
        tv1.getPaint(), 150.0f, TextUtils.TruncateAt.MIDDLE, true, callback);
```
> str结果为Returns.d Pain
如果preserveLength为true则会在填充零宽度空格而不是裁剪，查看返回的文字长度与原文字长度一致
callback负责返回替换的起始结束坐标
* equals(CharSequence a, CharSequence b) 文字比较
>内容可以为空
* expandTemplate(CharSequence template, CharSequence... values) 内容替换将\^1-\^9替换为指定下标内容最多9个
```java
String template = "This is a ^1 of the ^2 broadcast ^3.";
CharSequence expandTemplate = TextUtils.expandTemplate(template, "test", "emergency", "system");
```
> expandTemplate结果为This is a test of the emergency broadcast system.
* getCapsMode(CharSequence cs, int off, int reqModes) 确定文本中当前偏移量应该生效的上限
* getChars(CharSequence s, int start, int end, char[] dest, int destoff) 将s转成char数组
* getLayoutDirectionFromLocale(Locale locale) 根据语言返回文字布局习惯，左到右或右到左
* getOffsetAfter(CharSequence text, int offset) 获得偏移后的值
* getOffsetBefore(CharSequence text, int offset) 获得偏移之前的值
* getTrimmedLength(CharSequence s) 返回等于调用trim()之后的文字长度
* htmlEncode(String s) HTML编码 与TextUtilsCompat.htmlEncode(String s)类似
* isEmpty(@Nullable CharSequence str) 文字是否为空
* indexOf(CharSequence s, char ch) 获取文字下标
* isDigitsOnly(CharSequence str) 是否只包含数字
* isGraphic(CharSequence str) 是否有可打印的字符
* join(CharSequence delimiter, Object[] tokens) 使用指定字符拼接内容
* join(CharSequence delimiter, Iterable tokens) 同上
* lastIndexOf(CharSequence s, char ch) 获取文字倒序下标
* listEllipsize(@Nullable Context context,
            @Nullable List<CharSequence> elements, @NonNull String separator,
            @NonNull TextPaint paint, @FloatRange(from=0.0,fromInclusive=false) float avail,
            @PluralsRes int moreId)
* regionMatches(CharSequence one, int toffset, CharSequence two, int ooffset, int len) 判断one字符串从toffset，two字符串从ooffset长度为len的内容是否相同
* replace(CharSequence template,String[] sources, CharSequence[] destinations) 字符串替换，仅首次出现
替换
* split(String text, String expression) 内容分割为数组
* stringOrSpannedString(CharSequence source) 如果是 String 返回 String ，spannedString 或 spanned 返回 spannedString。
* substring(CharSequence source, int start, int end) 文字裁剪
* writeToParcel(CharSequence cs, Parcel p, int parcelableFlags)

### android.text.format.DateUtils 日期管理
* formatDateRange(Context context, long startMillis, long endMillis, int flags) 返回两个时间范围
```java
DateUtils.formatDateRange(this,
    mCalendar.getTimeInMillis(),
    mCalendar.getTimeInMillis() + 1000,
    DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_TIME);
```
返回12:44 – 12:44 星期六
>使用`DateUtils.FORMAT_SHOW_YEAR`之类的flag来标示显示那些参数
* formatDateTime(Context context, long millis, int flags) 返回格式化显示时间
* formatElapsedTime(long elapsedSeconds) 返回格式化经过的时间参数为秒 格式为"MM:SS" 或 "H:MM:SS"
* formatElapsedTime(StringBuilder recycle, long elapsedSeconds) 同上，如果recycle不为空则会将内容添加到recycle里面
* CharSequence formatSameDayTime(long then, long now, int dateStyle, int timeStyle) 格式化日期时间如果两个时间在同一天则显示时间，如果不在同一天则显示日期，日期时间格式有DateFormat.DEFAULT/FULL/LONG/MEDIUM/SHORT
* getRelativeDateTimeString(Context c, long time, long minResolution, long transitionResolution, int flags) 返回传入时间与当前时间的差距以及显示具体时间
> minResolution 表示最短时间如使用DateUtils.SECOND_IN_MILLIS经过3秒后显示为‘0分钟前’
transitionResolution 表示以停止使用相对时间大于此时间将由‘xx天/小时前’变为‘12月12日’
* getRelativeTimeSpanString(long startTime) 返回相对的时间，如果在当天显示时间，如果在当年显示月日，否则显示年月日
* getRelativeTimeSpanString(Context c, long millis) 返回一个具体时间，如果在当天显示时间，如果在当年显示月日，否则显示年月日
* isToday(long when) 传入时间是否在当天