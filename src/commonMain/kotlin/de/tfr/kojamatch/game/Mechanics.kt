package de.tfr.kojamatch.game

import com.soywiz.korev.Key
import com.soywiz.korge.bus.GlobalBus
import com.soywiz.korge.input.keys
import com.soywiz.korge.view.*

class Mechanics(bus: GlobalBus, val field: CardsField, player: Player) : Container() {

    init {
        keys {
            down {
                if (it.key == Key.SPACE) {
                    bus.send(Events.PicUpEvent())
                }
            }
        }

        addUpdater {
            deselctAllCards()

            field.cards.forEach {
                if (player.touches(it)) {
                    deselctAllCards()
                    it.setHighlight(true)
                }
            }

        }

    }

    fun deselctAllCards() {
        field.cards.forEach {
            it.setHighlight(false)
        }
    }


}