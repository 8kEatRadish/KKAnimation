package com.konka.mylibrary

enum class Techniques(val animatorClazz: Class<*>) {
    BounceAnimator(BounceInAnimator::class.java);

    val animator: KKBaseAnimator
        get() = try {
            animatorClazz.newInstance() as KKBaseAnimator
        } catch (e: Exception) {
            throw Error("Can not init animatorClazz instance")
        }

}
