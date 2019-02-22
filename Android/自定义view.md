## 自定义view
1. 创建类继承`android.view.View`
1. 添加自定义属性
> 1. 在`values`文件夹中添加`attrs.xml`属性文件
> 1. 添加自定义属性`<attr name="titleText" format="string"/>`
> 1. 将属性绑定至对应的view
`<declare-styleable name="自定义view名"><attr name="titleText"/></declare-styleable>`
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

7. 重新`onDraw()`绘制自定义内容
* `drawRect`绘制矩形使用`Rect`或`RectF`来确定矩形四边与屏幕左上角位置
    > 如果`Paint`的Style设置为`FILL`则完全按坐标显示一个完整的矩形，如果设置为`STROKE`或`FILL_AND_STROKE`并且设置`setStrokeWidth`为较大的值则该矩形实际看起来的大小会不一样
    ![图1](/Android/%E8%87%AA%E5%AE%9A%E4%B9%89view/1.png)  
