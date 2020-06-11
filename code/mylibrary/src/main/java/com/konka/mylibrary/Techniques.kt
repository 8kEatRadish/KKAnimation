package com.konka.mylibrary

/**
 *文件: Techniques.kt
 *描述: 动画枚举类
 *作者: SuiHongWei 2020/6/11
 **/
enum class Techniques(val animatorClazz: Class<*>) {
    BounceAnimator(BounceInAnimator::class.java);

    val animator: KKBaseAnimator
        get() = try {
            animatorClazz.newInstance() as KKBaseAnimator
        } catch (e: Exception) {
            throw Error("Can not init animatorClazz instance")
        }

}
