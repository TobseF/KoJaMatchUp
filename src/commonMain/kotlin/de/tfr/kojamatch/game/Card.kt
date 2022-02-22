package de.tfr.kojamatch.game

import cheatMode
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
class Card(
    private val cardSize: Int,
    val type: Int,
    posX: Int,
    posY: Int,
    val column: Int,
    val row: Int,
    resources: Resources
) : Container() {

    private val borderRect: RoundRect
    private val border = 6.0
    private val cornerRadius = 12.0
    private val image: Image
    private val background: Image
    private val scaledSize: Double

    private val rotateDuration = 1.seconds
    private val collectDuration = 1.2.seconds
    var rotating = false
    var collected = false

    init {
        position(posX, posY)
        borderRect = roundRect(
            width = (cardSize + (border * 2)),
            height = (cardSize + (border * 2)),
            rx = cornerRadius,
            stroke = Colors.YELLOW,
            fill = Colors.TRANSPARENT_BLACK,
            strokeThickness = border
        ) {
            position(-border, -border)
        }

        image = image(resources.getCard(type)) {
            setSize(cardSize.toDouble(), cardSize.toDouble())
        }
        background = image(resources.cardBackGround) {
            setSize(cardSize.toDouble(), cardSize.toDouble())
        }
        scaledSize = image.scale
        if (cheatMode) {
            text("" + type, textSize = 60.0, color = Colors.BLACK).centerOn(this)
        }
    }

    suspend fun takeOff() {
        if (!rotating) {
            rotateCard(background, image)
        }
    }

    suspend fun takeUp() {
        if (!rotating) {
            rotateCard(image, background)
        }
    }

    suspend fun collect() {
        collected = true
        takeUp()
        rotating = true
        background.visible = false
        image.tween(
            image::scale[1.4].duration(collectDuration).easing(Easing.EASE_IN_QUAD),
            image::alpha[0].duration(collectDuration).easing(Easing.EASE_IN_QUAD),
        )
        visible = false
        removeFromParent()
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

    override fun toString(): String {
        return "Card(type=$type [$column:$row])"
    }


}