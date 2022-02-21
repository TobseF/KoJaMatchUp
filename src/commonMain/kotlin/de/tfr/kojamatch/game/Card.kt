package de.tfr.kojamatch.game

import com.soywiz.klock.seconds
import com.soywiz.korge.tween.duration
import com.soywiz.korge.tween.easing
import com.soywiz.korge.tween.get
import com.soywiz.korge.tween.tween
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korma.interpolation.Easing

/**
 * Memory card on the [CardsField]
 */
class Card(private val cardSize: Int, type: Int, posX: Int, posY: Int, resources: Resources) : Container() {

    private val borderRect: SolidRect
    private val border = 4.0
    private val image: Image
    private val background: Image
    private val scaledSize: Double

    private val rotateDuration = 1.seconds
    private var rotated = false
    private var rotating = false

    init {
        position(posX, posY)
        borderRect = solidRect(cardSize + (border * 2), cardSize + (border * 2)) {
            color = Colors.RED
            position(-border, -border)
        }
        solidRect(cardSize, cardSize, color = Colors.LIGHTYELLOW)
        image = image(resources.getCard(type)) {
            setSize(cardSize.toDouble(), cardSize.toDouble())
        }
        background = image(resources.cardBackGround) {
            setSize(cardSize.toDouble(), cardSize.toDouble())
        }
        scaledSize = image.scale
    }

    suspend fun rotate() {
        if (!rotating) {
            if (rotated) {
                takeOff()
            } else {
                takeUp()
            }
            rotated = !rotated
        }
    }

    suspend fun takeOff() {
        rotateCard(background, image)
    }

    suspend fun takeUp() {
        rotateCard(image, background)
    }

    suspend fun rotateCard(foreground: Image, background: Image) {
        rotating = true
        background.x = 0.0
        background.scaleX = scaledSize
        foreground.x = cardSize / 2.0
        foreground.scaleX = 0.0
        background.tween(
            background::scaleX[0].duration(rotateDuration).easing(Easing.EASE_IN_QUAD),
            background::x[(cardSize / 2)].duration(rotateDuration).easing(Easing.EASE_IN_QUAD),
        )
        foreground.tween(
            foreground::x[0].duration(rotateDuration).easing(Easing.EASE_OUT_QUAD),
            foreground::scaleX[scaledSize].duration(rotateDuration).easing(Easing.EASE_OUT_QUAD),
        )
        rotating = false
    }

    fun setHighlight(visible: Boolean) {
        borderRect.visible = visible
    }

}