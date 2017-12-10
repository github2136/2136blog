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
#### ValueAnimator动画
ValueAnimator类通过指定一组int、float或颜色的值来实现动画，可以让你在动画持续时间内对类的值进行动画。通过调用其工厂方法之一：ofInt(), ofFloat(), or ofObject()可以获得一个ValueAnimator
```
ValueAnimator animation = ValueAnimator.ofFloat(0f, 100f);
animation.setDuration(1000);
animation.start();
```
这段代码表示当调用start()时ValueAnimator开始计算值从0到100，持续时间1000毫秒

还可以通过执行以下操作来自定义
```
ValueAnimator animation = ValueAnimator.ofObject(new MyTypeEvaluator(), startPropertyValue, endPropertyValue);
animation.setDuration(1000);
animation.start();
```
这段代码表示当调用start()时ValueAnimator开始计算值从startPropertyValue到endPropertyValue，使用MyTypeEvaluator提供的逻辑计算，持续时间100毫秒

可是给ValueAnimator添加AnimatorUpdateListener来使用动画的值
```
animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
    @Override
    public void onAnimationUpdate(ValueAnimator updatedAnimation) {
        // You can use the animated value in a property that uses the
        // same type as the animation. In this case, you can use the
        // float value in the translationX property.
        float animatedValue = (float)updatedAnimation.getAnimatedValue();
        textView.setTranslationX(animatedValue);
    }
});
```
在onAnimationUpdate()方法中可以监听并用于视图的属性，关于更多监听器详见[Animation Listeners](https://developer.android.google.cn/guide/topics/graphics/prop-animation.html#listeners)
#### ObjectAnimator动画
ObjectAnimator是ValueAnimator的一个子类，并结合了ValueAnimator的计时引擎和值计算，并有能力将对象的属性名执行动画。这使任何对象调价动画容易的多,因不需要实现ValueAnimator的AnimatorUpdateListener，因为可以自动更新属性

实例化ObjectAnimator类似于ValueAnimator，但也可以指定对象的属性(作为字符串)以及在其中的值
```
ObjectAnimator animation = ObjectAnimator.ofFloat(textView, "translationX", 100f);
animation.setDuration(1000);
animation.start();
```
要让ObjectAnimator正确的更新属性，必须做以下操作

* 对象的动画属性必须有set<属性名>()。因为ObjectAnimator在动画期间自动更新属性，因此必须能使用这个setter方法访问属性。例如，如果属性名是foo，则需要使用setFoo()方法。如果这个setter方法不存在，则有三个方法：

> * 添加settter方法
> * 使用一个包装类使用有效的setter方法接收该值，并将其转发到原始对象
> * 使用ValueAnimator
* 如果给ObjectAnimator仅指定一个值，则假定为动画的结束值。因此，所做动画的属性必须有一个getter函数，用于获取动画起始值。getter函数必须以get<PropertyName>()的形式出现，例如属性名为foo，则需要一个getFoo()方法
* getter(如果需要)和setter方法必须和指定的ObjectAnimator开始值和结束值类型相同。例如有targetObject.setPropName(float)和targetObject.getPropName(float)
```
ObjectAnimator.ofFloat(targetObject, "propName", 1f)
```
* 根据动画或属性对象，可能需要在view上调用invalidate()方法来强制view更新。可以在onAnimationUpdate()回调中操作。例如Drawable对象的颜色属性进行动画处理时，只会在对象重新绘制到屏幕时更新。view上所有属性setter如setAlpha()和setTranslationX()都可以正确的绘制，因此，调用这些方法的时不用调用invalidate()
#### 使用AnimatorSet编排多个动画
可以将多个动画合并到一个AnimatorSet，以便指定同时、按顺序或指定延迟后执行。也可以将AnimatorSet对象嵌套在一起
1. 播放 bounceAnim.
1. 同时播放 squashAnim1, squashAnim2, stretchAnim1, stretchAnim2 
1. 播放 bounceBackAnim.
1. 播放 fadeAnim.
```
AnimatorSet bouncer = new AnimatorSet();
bouncer.play(bounceAnim).before(squashAnim1);
bouncer.play(squashAnim1).with(squashAnim2);
bouncer.play(squashAnim1).with(stretchAnim1);
bouncer.play(squashAnim1).with(stretchAnim2);
bouncer.play(bounceBackAnim).after(stretchAnim2);
ValueAnimator fadeAnim = ObjectAnimator.ofFloat(newBall, "alpha", 1f, 0f);
fadeAnim.setDuration(250);
AnimatorSet animatorSet = new AnimatorSet();
animatorSet.play(bouncer).before(fadeAnim);
animatorSet.start();
```
#### 动画监听
Animator.AnimatorListener  
onAnimationStart() - 动画开始时调用  
onAnimationEnd() - 动画结束时调用  
onAnimationRepeat() - 动画重复时调用  
onAnimationCancel() - 动画取消时调用。取消时也会调用onAnimationEnd()，不管如何结束都会调用onAnimationEnd()

ValueAnimator.AnimatorUpdateListener  
onAnimationUpdate() - 每一帧更新时调用。使用此监听器监听ValueAnimator在动画过程中值得变化。要使用该值可以调用传入的ValueAnimator对象的getAnimatedValue()方法。如果要使用ValueAnimator，则需要实现该监听器。根据动画的属性或对象，可能需要调用view的invalidate()方法以强制重新绘制自己。例如，Drawable对象的颜色属性只会在重新绘制在屏幕上是刷新。view上的所有属性均能正确使用，因此调用view不需要调用invalidate()方法。

如果不想实现Animator.AnimatorListener接口的所有方法，可以扩展AnimatorListenerAdapter类。AnimatorListenerAdapter类提供了可以选择覆盖方法的空实现

#### ViewGroups的动画布局
属性动画提供了对ViewGroups对象进行动画变化的功能，并提供了一种简单的方法的来为view对象本身设置动画效果。

可以使用LayoutTransition类在ViewGroups内动画布局更改。ViewGroups中的view可以在ViewGroups中添加或删除，或者使用VISIBLE，INVISIBLE或GONE时调用setVisibility()，可以运行动画。在添加或删除view时，ViewGroups中剩余view可以动画到新的位置。可以通过调用setAnimator()并使用下列LayoutTransition常量传入Animator对象来定义动画：
* APPEARING - 表示在容器中出现的项(item)动画
* CHANGE_APPEARING - 表示在容器中出现新的项(item)而改变项(item)运行的动画
* DISAPPEARING - 表示从容器消失的项(item)运行的动画
* CHANGE_DISAPPEARING - 表示由于某项(item)从容器显示而正在改变的项(item)运行的动画
可以为这四种类型时间定义自己的自定义动画，或使用默认动画

api演示中的LayoutAnimations示例展示了如何为布局转换自定义动画，使用android:animateLayoutchanges="true"可以添加默认动画
#### 使用TypeEvaluator