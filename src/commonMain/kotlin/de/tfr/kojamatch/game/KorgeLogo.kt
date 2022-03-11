package de.tfr.kojamatch.game

import com.soywiz.klock.seconds
import com.soywiz.korge.bus.GlobalBus
import com.soywiz.korge.tween.get
import com.soywiz.korge.tween.tween
import com.soywiz.korge.view.*
import com.soywiz.korma.geom.degrees
import de.tfr.kojamatch.game.network.ConnectedEvent

class KorgeLogo(val bus: GlobalBus, resources: Resources) : BaseImage(resources.logo) {

    suspend fun init() {
        bus.register<ConnectedEvent> { visible = false }
        rotation = 0.degrees
        scale(.18)
            .alignRightToRightOf(parent!!, -20).alignTopToTopOf(parent!!, 55)
        anchor(0.5, 0.5)
        while (true) {
            tween(::rotation[30.degrees], time = 1.seconds)
            tween(::rotation[(-30).degrees], time = 1.seconds)
        }
    }

}