package com.dungnguyen.qdcore.support

import androidx.navigation.NavOptions
import com.dungnguyen.qdcore.R

class NavConst {

    companion object {
        val anim = NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_right)
            .build()

    }

}