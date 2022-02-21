package de.tfr.kojamatch.game

import com.soywiz.korev.Key
import com.soywiz.korge.bus.GlobalBus
import com.soywiz.korge.input.keys
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addUpdater

class Mechanics(bus: GlobalBus, val field: CardsField, player: Player) : Container() {

    init {
        keys {
            down {
                if (it.key == Key.SPACE) {
                    bus.send(Events.PicUpEvent())
                    field.getSelectedCard(player)?.rotate()
                }
            }
        }

        addUpdater {
            deselectAllCards()
            field.getSelectedCard(player)?.let {
                deselectAllCards()
                it.setHighlight(true)
            }
        }

    }

    private fun deselectAllCards() {
        field.cards.forEach {
            it.setHighlight(false)
        }
    }
}

