package de.tfr.kojamatch.game

import com.soywiz.korev.Key
import com.soywiz.korge.input.keys
import com.soywiz.korge.view.BaseImage
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korui.layout.MathEx
import virtualResolution

class Player(bitmap: Bitmap) : BaseImage(bitmap) {
    private val step = 20

    init {
        initMoves()
    }

    private fun initMoves() {
        keys {
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
                    if (it.key == Key.SPACE) {

                    }
                }
            }
        }
    }

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