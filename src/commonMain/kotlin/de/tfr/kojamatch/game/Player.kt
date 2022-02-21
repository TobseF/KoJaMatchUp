package de.tfr.kojamatch.game

import com.soywiz.korev.Key
import com.soywiz.korge.bus.GlobalBus
import com.soywiz.korge.input.keys
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korui.layout.MathEx
import virtualResolution

class Player(resources: Resources, val bus: GlobalBus) : Container() {
    private val step = 20
    private val hitPoint: Circle

    init {
        initMoves()
        image(resources.playerImage)
        hitPoint = circle(radius = 4.0, fill = Colors.RED).centerOnStage()
    }

    private fun initMoves() {
        keys {
            down {
                if (it.key == Key.LEFT) {
                    moveLeft()
                }
                if (it.key == Key.RIGHT) {
                    moveRight()
                }
                if (it.key == Key.UP) {
                    moveUp()
                }
                if (it.key == Key.DOWN) {
                    moveDown()
                }
            }
        }
    }

    fun touches(view: View) = hitPoint.collidesWith(view)

    private fun moveLeft() {
        x = MathEx.max(x - step, 0.0)
    }

    private fun moveRight() {
        x = MathEx.min(x + step, virtualResolution.width.toDouble() - width)
    }

    private fun moveDown() {
        y = MathEx.min(y + step, virtualResolution.height - height)
    }

    private fun moveUp() {
        y = MathEx.max(y - step, 0.0)
    }
}