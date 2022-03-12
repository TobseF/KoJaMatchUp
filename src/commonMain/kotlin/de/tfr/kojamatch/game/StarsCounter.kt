package de.tfr.kojamatch.game

import com.soywiz.klock.milliseconds
import com.soywiz.korge.bus.GlobalBus
import com.soywiz.korge.tween.get
import com.soywiz.korge.tween.tween
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korio.async.delay
import com.soywiz.korio.async.launch
import com.soywiz.korma.geom.degrees
import com.soywiz.korma.interpolation.Easing
import de.tfr.kojamatch.game.network.ConnectedEvent
import de.tfr.kojamatch.game.network.NewScoreEvent

class StarsCounter(bus: GlobalBus, stage: Stage, resources: Resources) : Container() {

  init {
    image(resources.star) {
      position(10, 10)
    }
    val yellow = Colors.get("FCBD0F")
    val text = text("0", textSize = 80.0, color = yellow) {
      position(110, 10)
    }

    bus.register<ConnectedEvent> {
      text.setText((it.score).toString())
    }
    bus.register<NewScoreEvent> {
      val scoreStar = image(resources.star) {
        anchor(0.5, 0.5)
        position(it.pos.x, it.pos.y)
        rotation = 120.degrees
        scale = 0.1
      }
      val flyTime = 2800.milliseconds

      stage.launch {
        scoreStar.tween(scoreStar::x[50], time = flyTime, easing = Easing.EASE_OUT_QUAD)
      }
      stage.launch {
        scoreStar.tween(scoreStar::y[50], time = flyTime, easing = Easing.EASE_OUT_QUAD)
      }
      stage.launch {
        scoreStar.tween(scoreStar::scale[0.8], time = 600.milliseconds, easing = Easing.EASE_OUT_QUAD)
      }
      stage.launch {
        scoreStar.tween(scoreStar::rotation[0.degrees], time = flyTime, easing = Easing.EASE_OUT_QUAD)
      }
      stage.launch {
        delay(flyTime - 400.milliseconds)
        scoreStar.tween(scoreStar::alpha[0.0], time = flyTime, easing = Easing.EASE_OUT)
      }
      stage.launch {
        delay(flyTime - 200.milliseconds)
        text.setText((it.score).toString())
        scoreStar.removeFromParent()
      }
    }
  }
}