package com.konka.mylibrary

import android.animation.ObjectAnimator
import android.view.View

/**
 *文件: BounceInAnimator.kt
 *描述: 弹性动画
 *作者: SuiHongWei 2020/6/11
 **/
class BounceInAnimator : KKBaseAnimator() {
    override fun prepare(target: View?) {
        //这里可以自己排列动画顺序，具体可以看一下AnimatorSet的用法
        getAnimatorAgent()?.playTogether(
            ObjectAnimator.ofFloat(target, "alpha", 0f, 1f, 1f, 1f),
            ObjectAnimator.ofFloat(target, "scaleX", 0.3f, 1.05f, 0.9f, 1f),
            ObjectAnimator.ofFloat(target, "scaleY", 0.3f, 1.05f, 0.9f, 1f)
        )
    }
}