package com.konka.mylibrary

import android.view.View
import java.util.*

/**
 *文件: Materias.kt
 *描述: 构建材料
 *作者: SuiHongWei 2020/6/9
 **/
class Materials private constructor(views: Array<out View>) {
    val viewAttrs: ArrayList<ViewAttrs> = ArrayList()

    companion object {
        fun createMaterials(vararg views: View): Materials {
            return Materials(views)
        }
    }

    init {
        for (view in views) {
            view.run {
                val location = IntArray(2)
                getLocationOnScreen(location)
                viewAttrs.add(
                    ViewAttrs(
                        id,
                        alpha,
                        location[0],
                        location[1],
                        width,
                        height
                    )
                )
            }
        }
    }
}