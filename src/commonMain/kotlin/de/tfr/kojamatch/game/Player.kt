package de.tfr.kojamatch.game

import com.soywiz.klock.milliseconds
import com.soywiz.korge.bus.GlobalBus
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korma.geom.Angle
import com.soywiz.korma.geom.degrees

class Player(val resources: Resources, val bus: GlobalBus) : Container() {
    private val hitPoint: Circle
    private val walking: Sprite

    private val walkRight = getAnimation("right")
    private val walkLeft = getAnimation("left")
    private val walkUp = getAnimation("up")
    private val walkDown = getAnimation("down")

    private fun getAnimation(name: String) = resources.playerSpriteSheet.getSpriteAnimation(name)

    init {
        hitPoint = circle(radius = 8.0, fill = Colors.TRANSPARENT_WHITE).centerOnStage()

        walking = sprite(walkDown) {
            center()
            rotation = (-90.0).degrees
            spriteDisplayTime = 200.milliseconds
        }
    }

    fun walk(angle: Angle) {
        stopWalking()
        fun Int.ninetyRange() = this - 45..this + 45
        when (angle.degrees.toInt()) {
            in 360 downTo (360 - 45) -> walking.playAnimationLooped(walkLeft)
            in 0..45 -> walking.playAnimationLooped(walkLeft)
            in 90.ninetyRange() -> walking.playAnimationLooped(walkUp)
            in 180.ninetyRange() -> walking.playAnimationLooped(walkRight)
            in 270.ninetyRange() -> walking.playAnimationLooped(walkDown)
        }
    }

    fun stopWalking() {
        walking.stopAnimation()
    }

    fun touches(view: View) = hitPoint.collidesWith(view)
}