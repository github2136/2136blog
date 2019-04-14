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
    * `setStrokeWidth`设置线条宽度默认0
    * `setTextSize`设置文字大小
    * `setAntiAlias`设置开启抗锯齿
    * `setStrokeCap`设置点或线为圆角（注意关闭硬件加速，不然可能没有效果）
    * `setStrokeJoin`拐角样式`MITER`尖角、`BEVEL`平角和`ROUND`圆角。默认为`MITER`
    * `setStrokeMiter`拐角延长线最大值
    * `setDither`图像抖动
    * `setFilterBitmap`双线性过滤
    * `setPathEffect`绘制路线效果
    * `setShadowLayer`文字阴影绘制
    * `setPathEffect`线路效果
    * `setMaskFilter`模糊效果`BlurMaskFilter`浮雕效果`EmbossMaskFilter`
    * `getFillPath`获得图形的绘制路径
    * `getTextPath`获得文字的绘制路径
    * `setTypeface`设置文字字体
    * `setFakeBoldText`设置伪粗体
    * `setStrikeThruText`删除线
    * `setUnderlineText`下划线
    * `setTextSkewX`设置文字倾斜角度
    * `setTextScaleX`文字横向缩放
    * `setLetterSpacing`文字间距
    * `setFontFeatureSettings`以CSS的`font-feature-settings`方式设置文字
    * `setTextAlign`文字对其方式
    * `setTextLocale`设置文字所属区域，`Locale.CHINA/Locale.JAPAN`等
    * `setHinting`字体微调（现在作用不大）
    * `setSubpixelText`次级像素抗锯齿（现在作用不大）
    * `setLinearText`
    * `getFontSpacing`获取行高
    * `getFontMetrics`获取文字指标`ascent/descent/top/bottom/leading`
    * `getTextBounds`获取文字尺寸
    * `measureText`获取文字宽度
    * `getTextWidths`获取每个字符宽度
    * `breakText`根据输入的宽度限制返回可显示文字数量
    * `getRunAdvance`获取光标位置
    * `hasGlyph`检查指定字符串中是否是一个单独的字形
1. PathEffect路径效果 
    * `CornerPathEffect`圆角效果
    * `DiscretePathEffect`随机偏离
    * `DashPathEffect`虚线效果
    * `PathDashPathEffect`指定路径虚线绘制
    * `SumPathEffect`叠加效果，将两个效果叠加绘制
    * `ComposePathEffect`组合效果，将两个效果组合绘制
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
    * `drawTextOnPath`按指定路径绘制文字`hOffset`表示文字水平起始位置，只能为正值，`vOffset`表示文字垂直位置，正值为路线下方，负值为路线上方
    * `drawPath`自定义图形，分为两类方法
        1. 直接绘制，绘制方法分为两种
            * `addXxx()`添加子图形，例如`addCircle`画圆，如果参数里有名为`Direction`则表示绘制方向`CW`顺时针`CCW`逆时针
            * `XxxTo()`画线（直线或曲线），例如`lineTo`表示画线，如果以`r`开始则表示是以相对坐标操作
                > `moveTo`表示移动到某位置而不是画线
                特殊方法`arcTo`和`addArc`只能画弧线而不是扇形，`forceMoveTo`参数表示是是否要抬起移动过去，就是这段弧线是否与之前的线不相连
            * `close()`封闭当前图形
        1. 辅助的设置或计算
            * `setFillType`的使用 https://hencoder.com/ui-1-1/

1. Shader着色器
    * `LinearGradient`线性渐变
    * `RadialGradient`辐射渐变
    * `SweepGradient`扫描渐变
    * `BitmapShader`图片绘制
    * `ComposeShader`混合着色，将两个Shader混合使用

1. ColorFilter
    * `LightingColorFilter`通过模拟灯光效果的颜色滤镜
    * `ColorMatrixColorFilter`通过4*5的颜色矩阵改变颜色
    * `PorterDuffColorFilter`通过单色和特定Porter-Duff模式对像素过滤
1. Xfermode
    * `PorterDuffXfermode`通过特定的Porter-Duff来设置两个图形的叠加绘制，绘制是注意使用离屏缓冲`Canvas.saveLayer();canvas.restoreToCount(saved);`
1. StaticLayout文字绘制可根据设置的宽设置自动给文字换行
1. 范围裁切
    * 使用`clipRect()`可以裁剪出一个矩形让只有绘制在矩形内的才显示
    * 使用`clipPath()`可以裁切出一个自定义图形，在`Path`中使用`setFillType`还可以指定填充方式
    ![图2](/Android/%E8%87%AA%E5%AE%9A%E4%B9%89view/2.jpg)
1. 几何变换
    * `Canvas`
        * `translate(float dx, float dy)`平移
        * `rotate(float degrees, float px, float py)`旋转
        * `scale(float sx, float sy, float px, float py)`缩放
        * `skew(float sx, float sy)`错切
    * `Matrix`        
        * `setXXX`表示清空之前的变换并设置为当前变换
        * `preXXX`表示插入变换队列的头部
        * `postXXX`表示插入变换队列的尾部
        * `setRectToRect`表示将`RectF src`变换为`RectF dst`变换类型有4种
            * `FILL`填充放大
            * `START`左上对齐
            * `CENTER`居中
            * `END`右下对齐

            ![图3](/Android/%E8%87%AA%E5%AE%9A%E4%B9%89view/3.png)
        * `setPolyToPoly`针对四角的变换，最多4个点（每个点一个X坐标一个Y坐标）
            * 变换一个点表示平移
            * 变换两个点表示旋转
            * 变换三个点表示错切
            * 变换四个点表示透视
        * `invert`矩阵反转
        * 注意使用`Matrix`时建议使用`canvas.concat(matrix);`，在不同系统中`canvas.setMatrix(matrix);`显示的结果可能不同
    * `Camera`三维变换有XYZ三个坐标系，以左上角为原点，`X轴`右正左负，`Y轴`上正下负，`Z轴`内正外负，下图为`Camera`的旋转方向
        ![图4](/Android/%E8%87%AA%E5%AE%9A%E4%B9%89view/4.png)
        * `applyToCanvas(canvas)`将变换应用到Canvas
        * `rotate*()`可以针对三个坐标系进行旋转
        * 建议使用`Canvas`的`translate`来移动原点
        * `setLocation(X,Y,Z)`移动虚拟相机位置，移动单位为英寸一英寸为72像素默认为-8英寸
        
自定义view硬件限制
![图5](/Android/%E8%87%AA%E5%AE%9A%E4%B9%89view/5.jpg)