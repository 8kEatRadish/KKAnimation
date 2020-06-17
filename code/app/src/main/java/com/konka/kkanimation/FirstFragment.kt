package com.konka.kkanimation

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.konka.mylibrary.BounceInAnimator
import com.konka.mylibrary.KK
import com.konka.mylibrary.Techniques
import com.tencent.mars.xlog.Log
import kotlinx.android.synthetic.main.fragment_first.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var textView = view.findViewById<TextView>(R.id.textview_first)
        view.findViewById<Button>(R.id.button_first).setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            Log.d("suihw >> " , " 微信的log")
            var intent = Intent(context,SecendActivity::class.java)
            intent.putExtra("test","我是测试的")
//            KK.startActivity(intent,this,textView , test_image)
            KK.Companion.with(Techniques.BounceAnimator)
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
                .playOn(textView)

        }
    }
}