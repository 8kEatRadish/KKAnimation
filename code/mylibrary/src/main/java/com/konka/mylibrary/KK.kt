package com.konka.mylibrary

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.NonNull
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import java.util.*

/**
 *文件: KK.kt
 *描述: konka动画
 *作者: SuiHongWei 2020/6/9
 **/
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "UNCHECKED_CAST")
class KK private constructor(animationComposer: AnimationComposer) {
    //动画实例
    private val animator: KKBaseAnimator

    //执行时间
    private val duration: Long

    //延迟时间
    private val delay: Long

    //是否重复播放
    private val repeat: Boolean

    //重复时间
    private val repeatTimes: Int

    //重复模式
    private val repeatMode: Int

    //差值器
    private val interpolator: Interpolator?

    //目标坐标x
    private val pivotX: Float

    //目标坐标y
    private val pivotY: Float

    //动画监听回调
    private val callbacks: List<Animator.AnimatorListener>

    //目标view
    private val target: View?


    interface AnimatorCallback {
        fun call(animator: Animator?)
    }


    private open class EmptyAnimatorListener :
        Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {}
        override fun onAnimationEnd(animation: Animator) {}
        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    }

    //动画创造器
    class AnimationComposer {
        internal val callbacks: MutableList<Animator.AnimatorListener> =
            ArrayList()
        var animator: KKBaseAnimator
        var duration = DURATION
        var delay = NO_DELAY
        var repeat = false
        var repeatTimes = 0
        internal var repeatMode = ValueAnimator.RESTART
        var pivotX = CENTER_PIVOT
        internal var pivotY = CENTER_PIVOT
        var interpolator: Interpolator? = null
        internal var target: View? = null

        internal constructor(techniques: Techniques) {
            animator = techniques.animator
        }

        internal constructor(animator: KKBaseAnimator) {
            this.animator = animator
        }

        fun duration(duration: Long): AnimationComposer {
            this.duration = duration
            return this
        }

        fun delay(delay: Long): AnimationComposer {
            this.delay = delay
            return this
        }

        fun interpolate(interpolator: Interpolator?): AnimationComposer {
            this.interpolator = interpolator
            return this
        }

        fun pivot(pivotX: Float, pivotY: Float): AnimationComposer {
            this.pivotX = pivotX
            this.pivotY = pivotY
            return this
        }

        fun pivotX(pivotX: Float): AnimationComposer {
            this.pivotX = pivotX
            return this
        }

        fun pivotY(pivotY: Float): AnimationComposer {
            this.pivotY = pivotY
            return this
        }

        fun repeat(times: Int): AnimationComposer {
            if (times < INFINITE) {
                throw RuntimeException("Can not be less than -1, -1 is infinite loop")
            }
            repeat = times != 0
            repeatTimes = times
            return this
        }

        fun repeatMode(mode: Int): AnimationComposer {
            repeatMode = mode
            return this
        }

        fun withListener(listener: Animator.AnimatorListener): AnimationComposer {
            callbacks.add(listener)
            return this
        }

        fun onStart(callback: AnimatorCallback): AnimationComposer {
            callbacks.add(object : EmptyAnimatorListener() {
                override fun onAnimationStart(animation: Animator) {
                    callback.call(animation)
                }
            })
            return this
        }

        fun onEnd(callback: AnimatorCallback): AnimationComposer {
            callbacks.add(object : EmptyAnimatorListener() {
                override fun onAnimationEnd(animation: Animator) {
                    callback.call(animation)
                }
            })
            return this
        }

        fun onCancel(callback: AnimatorCallback): AnimationComposer {
            callbacks.add(object : EmptyAnimatorListener() {
                override fun onAnimationCancel(animation: Animator) {
                    callback.call(animation)
                }
            })
            return this
        }

        fun onRepeat(callback: AnimatorCallback): AnimationComposer {
            callbacks.add(object : EmptyAnimatorListener() {
                override fun onAnimationRepeat(animation: Animator) {
                    callback.call(animation)
                }
            })
            return this
        }

        fun playOn(target: View?): KKControl {
            this.target = target
            return KKControl(KK(this).play(), this.target)
        }
    }

    //动画控制器
    class KKControl internal constructor(
        private val animator: KKBaseAnimator,
        private val target: View?
    ) {
        val isStarted: Boolean
            get() = animator.isStarted()

        val isRunning: Boolean
            get() = animator.isRunning()

        @JvmOverloads
        fun stop(reset: Boolean = true) {
            animator.cancel()
            if (reset) animator.reset(target)
        }

    }

    //播放动画
    private fun play(): KKBaseAnimator {
        animator.setTarget(target)
        if (pivotX == CENTER_PIVOT) {
            ViewCompat.setPivotX(target, target!!.measuredWidth / 2.0f)
        } else {
            target!!.pivotX = pivotX
        }
        if (pivotY == CENTER_PIVOT) {
            ViewCompat.setPivotY(target, target.measuredHeight / 2.0f)
        } else {
            target.pivotY = pivotY
        }
        animator.setDuration(duration)
            .setRepeatTimes(repeatTimes)
            .setRepeatMode(repeatMode)
            .setInterpolator(interpolator)
            .setStartDelay(delay)
        if (callbacks.isNotEmpty()) {
            for (callback in callbacks) {
                animator.addAnimatorListener(callback)
            }
        }
        animator.animate()
        return animator
    }

    companion object {
        //动画时间
        private const val DURATION: Long = KKBaseAnimator.DURATION

        //延迟时间
        private const val NO_DELAY: Long = 0

        const val INFINITE = -1

        val CENTER_PIVOT = Float.MAX_VALUE

        //是否在入场动画
        private var isEnter = false

        //使用反射创建
        fun with(techniques: Techniques): AnimationComposer {
            return AnimationComposer(techniques)
        }

        //使用实例创建
        fun with(animator: KKBaseAnimator): AnimationComposer {
            return AnimationComposer(animator)
        }

        //默认差值器
        private val DEFAULT_TIME_INTERPOLATOR: TimeInterpolator = LinearInterpolator()

        //默认过长动画时间长度
        private val DEFAULT_TRANSITION_DURATION: Long = 600

        //共享元素名称
        private val TRANSITION_MATERIALS = "KK_ANIMATION_TRANSITION_MATERIALS"

        /**
         * 跳转方法
         */
        fun startActivity(
            @NonNull intent: Intent?,
            @NonNull act: Activity?,
            @NonNull vararg views: View?
        ) {
            startActivityForResult(intent!!, -1, act!!, *views)
        }

        fun startActivity(
            @NonNull intent: Intent,
            @NonNull frag: Fragment,
            @NonNull vararg views: View?
        ) {
            startActivityForResult(intent, -1, frag, *views)
        }

        fun startActivityForResult(
            @NonNull intent: Intent,
            requestCode: Int,
            @NonNull activity: Activity,
            @NonNull vararg views: View?
        ) {
            val materials: Materials = Materials.createMaterials(*views as Array<out View>)
            intent.putParcelableArrayListExtra(TRANSITION_MATERIALS, materials.viewAttrs)
            activity.startActivityForResult(intent, requestCode)
            // Disable system default transition animation
            activity.overridePendingTransition(0, 0)
        }

        fun startActivityForResult(
            @NonNull intent: Intent,
            requestCode: Int,
            @NonNull frag: androidx.fragment.app.Fragment,
            @NonNull vararg views: View?
        ) {
            val materials: Materials = Materials.createMaterials(*views as Array<out View>)
            intent.putParcelableArrayListExtra(Companion.TRANSITION_MATERIALS, materials.viewAttrs)
            frag.startActivityForResult(intent, requestCode)
            val activity = frag.activity
            activity?.overridePendingTransition(0, 0)
        }

        /**
         * 进入动画
         */
        fun runEnterAnim(activity: Activity) {
            runEnterAnim(activity, DEFAULT_TRANSITION_DURATION)
        }

        fun runEnterAnim(activity: Activity, duration: Long) {
            runEnterAnim(activity, duration, DEFAULT_TIME_INTERPOLATOR)
        }

        fun runEnterAnim(
            activity: Activity,
            listener: Animator.AnimatorListener?
        ) {
            runEnterAnim(
                activity, DEFAULT_TRANSITION_DURATION,
                DEFAULT_TIME_INTERPOLATOR, listener
            )
        }

        fun runEnterAnim(
            activity: Activity,
            duration: Long,
            interpolator: TimeInterpolator?
        ) {
            runEnterAnim(activity, duration, interpolator, null)
        }

        fun runEnterAnim(
            activity: Activity,
            duration: Long,
            listener: Animator.AnimatorListener?
        ) {
            runEnterAnim(activity, duration, DEFAULT_TIME_INTERPOLATOR, listener)
        }

        /**
         * 界面进入过渡动画
         *
         * @param activity
         * @param duration
         * @param interpolator
         * @param listener
         */
        fun runEnterAnim(
            activity: Activity,
            duration: Long,
            interpolator: TimeInterpolator?,
            listener: Animator.AnimatorListener?
        ) {
            val attrs: ArrayList<ViewAttrs> =
                activity.intent.getParcelableArrayListExtra(TRANSITION_MATERIALS)
            if (attrs.isEmpty()) {
                return
            }
            isEnter = true
            for (attr in attrs) {
                val view: View = activity.findViewById(attr.id) ?: continue
                view.viewTreeObserver
                    .addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                        override fun onPreDraw(): Boolean {
                            view.viewTreeObserver.removeOnPreDrawListener(this)
                            val location = IntArray(2)
                            view.getLocationOnScreen(location)
                            view.pivotX = 0f
                            view.pivotY = 0f
                            view.translationX = (attr.screenX - location[0]).toFloat()
                            view.translationY = (attr.screenY - location[1]).toFloat()
                            view.scaleX = attr.width * 1.0f / view.width
                            view.scaleY = attr.height * 1.0f / view.height
                            view.alpha = attr.alpha
                            val srcAlpha = view.alpha
                            view.animate().alpha(srcAlpha)
                                .translationX(0f)
                                .translationY(0f)
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(duration)
                                .setInterpolator(interpolator)
                                .setListener(listener)
                                .start()
                            return true
                        }
                    })
            }
            Handler().postDelayed({
                isEnter = false
            }, duration)
        }

        /**
         * 退出动画
         */
        fun runExitAnim(kk: KK, activity: Activity) {
            runExitAnim(activity, DEFAULT_TRANSITION_DURATION)
        }

        fun runExitAnim(
            activity: Activity,
            duration: Long,
            interpolator: TimeInterpolator?
        ) {
            runExitAnim(activity, duration, interpolator, null)
        }


        fun runExitAnim(activity: Activity, duration: Long) {
            runExitAnim(activity, duration, DEFAULT_TIME_INTERPOLATOR)
        }

        fun runExitAnim(
            activity: Activity,
            duration: Long,
            listener: Animator.AnimatorListener?
        ) {
            runExitAnim(activity, duration, DEFAULT_TIME_INTERPOLATOR, listener)
        }

        /**
         * 界面退出还原动画
         *
         * @param activity
         * @param duration
         * @param interpolator
         * @param listener
         */
        fun runExitAnim(
            activity: Activity, duration: Long,
            interpolator: TimeInterpolator?, listener: Animator.AnimatorListener?
        ) {
            //防止进场动画没有完成就退出
            if (isEnter) return
            val attrs: ArrayList<ViewAttrs> =
                activity.intent.getParcelableArrayListExtra<ViewAttrs>(Companion.TRANSITION_MATERIALS)
            //防止没有携带attrs导致activity无法关闭
            if (attrs.isEmpty()) {
                activity.finish()
                return
            }
            for (attr in attrs) {
                val view: View = activity.findViewById(attr.id) ?: continue
                val location = IntArray(2)
                view.getLocationOnScreen(location)
                view.pivotX = 0f
                view.pivotY = 0f
                view.animate().alpha(attr.alpha)
                    .translationX((attr.screenX - location[0]).toFloat())
                    .translationY((attr.screenY - location[1]).toFloat())
                    .scaleX(attr.width * 1.00f / view.width)
                    .scaleY(attr.height * 1.00f / view.height)
                    .setDuration(duration)
                    .setInterpolator(interpolator)
                    .setListener(listener)
                    .start()
            }
            Handler().postDelayed({
                activity.finish()
                activity.overridePendingTransition(
                    0,
                    0
                )
            }, duration)
        }
    }

    init {
        animator = animationComposer.animator
        duration = animationComposer.duration
        delay = animationComposer.delay
        repeat = animationComposer.repeat
        repeatTimes = animationComposer.repeatTimes
        repeatMode = animationComposer.repeatMode
        interpolator = animationComposer.interpolator
        pivotX = animationComposer.pivotX
        pivotY = animationComposer.pivotY
        callbacks = animationComposer.callbacks
        target = animationComposer.target
    }
}
