package de.tfr.kojamatch.game

import com.soywiz.korev.Key
import com.soywiz.korge.view.Stage
import com.soywiz.korma.geom.Point
import com.soywiz.korui.layout.MathEx
import virtualResolution

class KeyBoardControls(private val stage: Stage, private val player: Player, private val target: Point) {

  private val playerStep = 20

  fun handeKeyboardControls() {
    if (anyMoveKeyIsPressed()) {
      resetDestination()
      if (Key.UP.isPressed() || Key.W.isPressed()) {
        moveUp()
      }
      if (Key.DOWN.isPressed() || Key.S.isPressed()) {
        moveDown()
      }
      if (Key.LEFT.isPressed() || Key.A.isPressed()) {
        moveLeft()
      }
      if (Key.RIGHT.isPressed() || Key.D.isPressed()) {
        moveRight()
      }
    }
  }

  private fun anyMoveKeyIsPressed() = (arrowKeys() + wasdKeys()).any { it.isPressed() }

  private fun arrowKeys() = listOf(Key.UP, Key.DOWN, Key.LEFT, Key.RIGHT)
  private fun wasdKeys() = listOf(Key.W, Key.A, Key.S, Key.S)

  private fun Key.isPressed() = stage.input.keys[this]

  private fun resetDestination() {
    target.copyFrom(player.pos)
  }

  private fun moveLeft() {
    target.x = MathEx.max(target.x - playerStep, 0.0)
  }

  private fun moveRight() {
    target.x = MathEx.min(target.x + playerStep, virtualResolution.width.toDouble())
  }

  private fun moveDown() {
    target.y = MathEx.min(target.y + playerStep, virtualResolution.height.toDouble())
  }

  private fun moveUp() {
    target.y = MathEx.max(target.y - playerStep, 0.0)
  }

}