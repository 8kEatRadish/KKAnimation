# KKAnimation

## 介绍

![UML](https://gitee.com/suihw/KKAnimation/raw/master/image/uml.jpg)

整体采用工厂模式和建造者模式搭建，动画执行以及执行顺序部分使用AnimatorSet来控制，整体统一，通过反射创建动画类实例。共享元素进入动画采用传递共享view信息到第二个场景，在第二个场景下实现共享元素的动画，退出动画是第二个场景做过场动画，动画执行完毕后，跳转到第一个场景。

## 使用方法

#### view的动画

1.继承抽象类KKBaseAnimator完成自己需求的动画

2.把自己实现的动画类添加到Techniques枚举类中

3.使用如下方式调用

```kotlin
KK.with(Techniques.BounceAnimator)//BounceAnimator为需求实现类
    .duration(2000)
    .pivot(KK.CENTER_PIVOT,KK.CENTER_PIVOT)
    .interpolate(AccelerateDecelerateInterpolator())
    .repeat(KK.INFINITE)
    .withListener(object : Animator.AnimatorListener{
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
        }

    })
    .playOn(textView)//textView是目标view
```

#### 过场动画（场景1跳转到场景2）

1.在场景1中调用

```kotlin
/**
* fun startActivity(
*     @NonNull intent: Intent,
*     @NonNull frag: Fragment,
*     @NonNull vararg views: View?
*  )
* 更多方法重载请具体查看代码
*/
KK.startActivity(intent,this,textView , test_image)
//views为想要共享的view元素，要求id相同
```

2.在场景2中调用

```kotlin
/*
* fun runEnterAnim(
*     activity: Activity,
*     duration: Long,
*     listener: Animator.AnimatorListener?
*  )
* 更多方法重载请具体查看代码
*/
KK.runEnterAnim(this,1800,object : Animator.AnimatorListener{
    override fun onAnimationRepeat(animation: Animator?) {
    }

    override fun onAnimationEnd(animation: Animator?) {
        Log.d("suihw >>" , "入场动画完成")
    }

    override fun onAnimationCancel(animation: Animator?) {
    }

    override fun onAnimationStart(animation: Animator?) {
    }
})
```