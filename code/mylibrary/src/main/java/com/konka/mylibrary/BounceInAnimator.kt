package com.konka.mylibrary

import android.animation.ObjectAnimator
import android.view.View

class BounceInAnimator : KKBaseAnimator() {
    override fun prepare(target: View?) {
        getAnimatorAgent()?.playTogether(
            ObjectAnimator.ofFloat(target, "alpha", 0f, 1f, 1f, 1f),
            ObjectAnimator.ofFloat(target, "scaleX", 0.3f, 1.05f, 0.9f, 1f),
            ObjectAnimator.ofFloat(target, "scaleY", 0.3f, 1.05f, 0.9f, 1f)
        )
    }
}