package com.konka.kkanimation

import android.animation.Animator
import android.os.Bundle
import android.view.animation.BounceInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.konka.mylibrary.KK
import com.tencent.mars.xlog.Log

class SecendActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val str = intent.getStringExtra("test")
        findViewById<TextView>(R.id.textview_first).text = str
        KK.Companion.runEnterAnim(this,1800,object : Animator.AnimatorListener{
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
    }

    override fun onBackPressed() {
        KK.runExitAnim(this,800)
    }
}