package de.tfr.kojamatch.game

import com.soywiz.korge.bus.GlobalBus
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors

class Player(resources: Resources, val bus: GlobalBus) : Container() {
    private val hitPoint: Circle

    init {
        image(resources.playerImage).center()
        hitPoint = circle(radius = 8.0, fill = Colors.TRANSPARENT_WHITE).centerOnStage()
    }

    fun touches(view: View) = hitPoint.collidesWith(view)
}