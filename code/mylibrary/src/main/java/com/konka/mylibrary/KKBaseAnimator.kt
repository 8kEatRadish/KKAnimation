package com.konka.mylibrary

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.Interpolator

/**
 *文件: KKBaseAnimator.kt
 *描述: Kk动画基础类
 *作者: SuiHongWei 2020/6/8
 **/

abstract class KKBaseAnimator {
    private var mAnimatorSet: AnimatorSet? = null
    private var mDuration = DURATION
    private var mRepeatTimes = 0
    private var mRepeatMode = ValueAnimator.RESTART
    protected abstract fun prepare(target: View?)
    fun setTarget(target: View?): KKBaseAnimator {
        reset(target)
        prepare(target)
        return this
    }

    fun animate() {
        start()
    }

    fun restart() {
        mAnimatorSet = mAnimatorSet!!.clone()
        start()
    }

    /**
     * reset the view to default status
     *
     * @param target
     */
    fun reset(target: View?) {
        target?.run {
            alpha = 1f
            scaleX = 1f
            scaleY = 1f
            translationX = 0f
            translationY = 0f
            rotation = 0f
            rotationX = 0f
            rotationY = 0f
        }
    }

    /**
     * start to animate
     */
    fun start() {
        for (animator in mAnimatorSet!!.childAnimations) {
            if (animator is ValueAnimator) {
                animator.run {
                    repeatCount = mRepeatTimes
                    repeatMode = mRepeatMode
                }
            }
        }
        mAnimatorSet!!.run {
            duration = mDuration
            start()
        }
    }

    fun setDuration(duration: Long): KKBaseAnimator {
        mDuration = duration
        return this
    }

    fun setStartDelay(delay: Long): KKBaseAnimator {
        getAnimatorAgent()!!.startDelay = delay
        return this
    }

    fun getStartDelay(): Long {
        return mAnimatorSet!!.startDelay
    }

    fun addAnimatorListener(l: Animator.AnimatorListener?): KKBaseAnimator {
        mAnimatorSet!!.addListener(l)
        return this
    }

    fun cancel() {
        mAnimatorSet!!.cancel()
    }

    fun isRunning(): Boolean {
        return mAnimatorSet!!.isRunning
    }

    fun isStarted(): Boolean {
        return mAnimatorSet!!.isStarted
    }

    fun removeAnimatorListener(l: Animator.AnimatorListener?) {
        mAnimatorSet!!.removeListener(l)
    }

    fun removeAllListener() {
        mAnimatorSet!!.removeAllListeners()
    }

    fun setInterpolator(interpolator: Interpolator?): KKBaseAnimator {
        mAnimatorSet!!.interpolator = interpolator
        return this
    }

    fun getDuration(): Long {
        return mDuration
    }

    fun getAnimatorAgent(): AnimatorSet? {
        return mAnimatorSet
    }

    fun setRepeatTimes(repeatTimes: Int): KKBaseAnimator {
        mRepeatTimes = repeatTimes
        return this
    }

    fun setRepeatMode(repeatMode: Int): KKBaseAnimator {
        mRepeatMode = repeatMode
        return this
    }

    companion object {
        const val DURATION: Long = 1000
    }

    init {
        mAnimatorSet = AnimatorSet()
    }
}
