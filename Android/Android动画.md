# 动画分类
https://developer.android.google.cn/guide/topics/graphics/index.html
##### 属性动画（Property Animation）
该功能是在Android3.0（API11）时引入，可以为任何对象的属性设置动画，该功能是可扩展的，并允许自动以属性动画
##### 视图动画（View Animation）
视图动画是旧版系统，只能用于view，使用方便，并提供足够的功能满足大部分应用需求
##### 绘制动画（Drawable Animation）
绘制动画包括一帧帧显示Drawable资源，就像电影一样。如果使用要展示一个进度条动画这个方法非常有用

## 属性动画（Property Animation）
属性动画系统是一个强大的框架，可以给任何对象加动画。你可以指定一个动画来随时间来改变属性值。属性动画会在指定时间长度内更改属性值。设置动画可以指定要设置动画属性  
属性动画系统可以设置以下特性
* 持续时间(Duration)：指定动画时间。默认300毫秒
* 时间差值(Time interpolation)：指定如何属性值如何变化
* 重复次数和行为(Repeat count and behavior)：可以指定在动画结束时是否重复，以及重复次数。也可以指定是否要反向播放动画。将其设置为反向然手重复播放
* 动画集合(Animator sets)：可以将动画分组，可以一起或按顺序播放，也可以在延时播放
* 帧刷新延迟(Frame refresh delay)：可以设置指定动画刷新帧频率。默认10毫秒一次，但速度最终取决系统繁忙程度及系统可以为底层定时器提供服务的速度
#### 属性动画如何运行
首先举一个例子，图1描绘了一个假设的对象，该对象使用其X属性进行动画处理，该属性表示其在屏幕上的水平位置。动画持续40毫秒，行进距离为40像素。每10毫秒，这是默认帧刷新率，对象水平移动10像素。在40毫秒结束，动画停止，并且对象在水平位置40结束。这是具有线性差值动画示例，这意味着对象一恒定速度移动
![图1](/Android/Android%E5%8A%A8%E7%94%BB/animation-linear.png)  

还可以指定一个非线程插值。图2展示了一个开始时加速的假设对象，在动画结束时减速。对象仍在40毫秒内移动了40个像素，但是非线性的。一开始加速到中点，让后减速到动画结束。
![图2](/Android/Android%E5%8A%A8%E7%94%BB/animation-nonlinear.png)   

图3显示了属性动画系统的重要组件如何计算上面所示的动画
![图3](/Android/Android%E5%8A%A8%E7%94%BB/valueanimator.png)  

ValueAnimator对象将会跟踪动画的运行，比如动画运行了多久，以及动画属性当前值。 

ValueAnimator封装了动画的差值器TimeInterpolator和一个类型估算器TypeEvaluator，它定义了如何计算动画运行时属性值得变化  

开启动画需要创建一个ValueAnimator ,并为它开始及结束值，动画持续时间。当调用start()时动画开始。在整个过程中ValueAnimator g根据动画持续时间和经过时间，计算0到1。经过的值表示动画完成比，0表示0%，1表示100%。例如，在图1中，它10ms处值是0.25，因为总持续时间为40ms。  

当ValueAnimator完成计算时它调用当前设置的TimeInterpolator来计算差值，如图2中因为缓慢加速，所以10ms时，值为0.15小于0.25。  
#### 属性动画(View Animation)与视图动画区别
视图动画系统仅提供了View对象的动画，因此如果想为非View对象设置则必须自己实现代码。视图动画系统仅能对View对象的几个属性处理，如缩放、旋转，背景图则不能。

视图动画系统的另一个缺点是只能在View绘制的地方修改，而不是实际View本身。例如，将按钮在屏幕上移动，该按钮会绘制正确，但点击的实际位置不会改变，因此必须自己实现逻辑处理此操作

使用属性动画系统，则完全没有这些约束，并且可以动画化对象的任何属性，而且是对象属性值的实际修改。属性动画在执行方面也更加健壮。

但是视图动画系统使用较少的时间及代码编写。如果视图动画能完成你所需要的事，或如果当前代码已经实现那就没必要属性动画

#### API概述
可以在android.animation.中找到大部分属性动画API。由于视图动画系统已经在android.view.animation中定义了许多插值器。因此也可以在属性动画中使用

Animator类提供创建动画的基本结构。通常不直接使用此类，因为它只提供最少的功能，必须扩展才能完全支持动画值，以下子类扩展了Animator

ValueAnimator ObjectAnimator AnimatorSet

估算器告诉属性动画如何计算属性的值。它们使用Animator类提供的开始值、结束值和时间，来计算属性值。属性动画提供以下估算器

IntEvaluator FloatEvaluator ArgbEvaluator TypeEvaluator

时间插值器定义了动画中值如何根据时间来计算值。可以指定线性动画使动画匀速移动。属性动画提供以下插值器

AccelerateDecelerateInterpolator AccelerateInterpolator AnticipateInterpolator AnticipateOvershootInterpolator BounceInterpolator CycleInterpolator DecelerateInterpolator LinearInterpolator OvershootInterpolator TimeInterpolator
