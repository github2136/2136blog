## 自定义view
1. 创建类继承`android.view.View`
1. 添加自定义属性
    > 1. 在`values`文件夹中添加`attrs.xml`属性文件
    > 1. 添加自定义属性`<attr name="titleText" format="string"/>`
    > 1. 将属性绑定至对应的view`<declare-styleable name="自定义view名"><attr name="titleText"/></declare-styleable>`
3. 在布局文件中使用自定义view并使用`app:titleText=""`添加属性
1. 重写构造函数（需要重写多个），使用`this()`来调用自己的构造函数，使得最后的属性初始化代码在参数最多的构造函数中
1. 获取属性
`TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTitleView, defStyle, 0);`  
使用`a.getxxx(R.styleable.自定义view名_自定义属性名)`来获取不同的属性值
1. 重写`onMeasure(int widthMeasureSpec, int heightMeasureSpec)`方法来计算控件尺寸，**该方法可不重写**
不重新该方法时，使用`wrap_content/match_parent`效果均为`match_parent`，所以如果需要`wrap_content`效果则需要重写该方法
> `MeasureSpec.getMode(widthMeasureSpec);`获取Mode
>>* MeasureSpec.EXACTLY是精确尺寸，当我们将控件的layout_width或layout_height指定为具体数值时如andorid:layout_width="50dip"，或者为match_parent是，都是控件大小已经确定的情况，都是精确尺寸。
>>* MeasureSpec.AT_MOST是最大尺寸，当控件的layout_width或layout_height指定为wrap_content时，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可。因此，此时的mode是AT_MOST，size给出了父控件允许的最大尺寸。
>>* MeasureSpec.UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，通过measure方法传入的模式。

> `MeasureSpec.getSize(widthMeasureSpec);`获取尺寸
>> * 如果Mode为`EXACTLY`则直接将控件尺寸设置为获取到的值，否则才需要计算控件所需的大小

如果重写该方法则使用`setMeasuredDimension(width, height);`来重新设置控件大小  

7. `Paint`基本方法
    * `setStyle`设置绘制模式`FILL`默认填充`STROKE`描边`FILL_AND_STROKE`描边加填充
    * `setColor`设置颜色
    * `setStrokeWidth`设置线条宽度
    * `setTextSize`设置文字大小
    * `setAntiAlias`设置开启抗锯齿
    * `setStrokeCap`设置点或线为圆角**低版本手机不是100%有效**
1. 重新`onDraw()`绘制自定义内容
    * `drawColor` `drawRGB` `drawARGB`给画布设置颜色`RGB`值为0..255
    * `drawRect`绘制矩形使用`left` `right` `top` `bottom`设置矩形四边与屏幕左上角位置或使用`Rect`和`RectF`
        > 如果`Paint`的Style设置为`FILL`则忽略`setStrokeWidth`并完全按坐标显示一个矩形，如果设置为`STROKE`或`FILL_AND_STROKE`并且设置`setStrokeWidth`为较大的值时，则该矩形实际大小为矩形大小加线条宽度的一半
    ![图1](/Android/%E8%87%AA%E5%AE%9A%E4%B9%89view/1.png)  
    * `drawCircle`绘制圆形参数为圆心坐标及半径
    * `drawPoint`设置点坐标使用`setStrokeWidth`设置点直径使用`paint.setStrokeCap(Paint.Cap.ROUND)`设置为圆点，`drawPoints`绘制多个点，每两个一组
    * `drawOval`绘制椭圆与矩形相同
    * `drawLine`画线设置线的起点终点坐标即可，`setStrokeCap`可设置线为圆角。`drawLines`绘制多条线，与点使用方法相同
    * `drawRoundRect`绘制圆角矩形，圆角弧线说明：例如`left`+`rx`为`a`，`top`+`ry`为`b`，在`a`、`b`之间画一条弧线，其他三角类似
    * `drawArc`画弧线或扇形设置，`startAngle`起始位置，中心到右边为0°，顺时针为正，逆时针为负，`sweepAngle`划过的角度，
        * `useCenter:true`+`Style.FILL`：扇形
        * `useCenter:false`+`Style.FILL`：弧形
        * `useCenter:true`+`Style.STROKE`：空心扇形
        * `useCenter:false`+`Style.STROKE`：弧线
    * `drawBitmap`绘制图片
    * `drawText`绘制文字坐标为文字的左下角
    * `drawPath`自定义图形，分为两类方法
        1. 直接绘制，绘制方法分为两种
            * `addXxx()`添加子图形，例如`addCircle`画圆，如果参数里有名为`Direction`则表示绘制方向`CW`顺时针`CCW`逆时针
            * `XxxTo()`画线（直线或曲线），例如`lineTo`表示画线，如果以`r`开始则表示是以相对坐标操作
                > `moveTo`表示移动到某位置而不是画线
                特殊方法`arcTo`和`addArc`只能画弧线而不是扇形，`forceMoveTo`参数表示是是否要抬起移动过去，就是这段弧线是否与之前的线不相连
            * `close()`封闭当前图形
        1. 辅助的设置或计算
            * `setFillType`的使用 https://hencoder.com/ui-1-1/