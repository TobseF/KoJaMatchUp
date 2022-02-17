package de.tfr.kojamatch.game

import com.soywiz.klock.seconds
import com.soywiz.korge.tween.get
import com.soywiz.korge.tween.tween
import com.soywiz.korge.view.BaseImage
import com.soywiz.korge.view.alignRightToRightOf
import com.soywiz.korge.view.alignTopToTopOf
import com.soywiz.korge.view.scale
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korma.geom.degrees
import com.soywiz.korma.interpolation.Easing

class KorgeLogo(bitmap: Bitmap) : BaseImage(bitmap) {
    val minDegrees = (-16).degrees
    val maxDegrees = (+16).degrees

    suspend fun init() {
        rotation = maxDegrees
        scale(.2)
            .alignRightToRightOf(parent!!, 20).alignTopToTopOf(parent!!, 20)
        while (true) {
            tween(::rotation[minDegrees], time = 1.seconds, easing = Easing.EASE_IN_OUT)
            tween(::rotation[maxDegrees], time = 1.seconds, easing = Easing.EASE_IN_OUT)
        }
    }

}