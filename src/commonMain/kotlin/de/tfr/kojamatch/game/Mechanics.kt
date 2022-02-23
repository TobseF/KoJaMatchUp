package de.tfr.kojamatch.game

import com.soywiz.klock.milliseconds
import com.soywiz.korev.Key
import com.soywiz.korge.bus.GlobalBus
import com.soywiz.korge.input.keys
import com.soywiz.korge.input.onClick
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addFixedUpdater
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.position
import com.soywiz.korio.async.launch
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korma.geom.Point

class Mechanics(bus: GlobalBus, val field: CardsField, val player: Player) : Container() {

    private var cardA: Card? = null
    private var cardB: Card? = null
    private var pickingCard = false
    private var destination: Point = player.pos
    private val playerSpeed = 6.0

    init {
        keys {
            down {
                if (it.key == Key.SPACE) {
                    pickUpCard(bus)
                }
            }
        }

        addUpdater {
            highlightCardOnHover()
        }

        addFixedUpdater(25.milliseconds) {
            if (destination.distanceTo(player.pos) > playerSpeed) {
                moveToDestination()
            }
        }
    }

    private suspend fun Mechanics.pickUpCard(bus: GlobalBus) {
        bus.send(Events.PicUpEvent())
        field.getSelectedCard(player)
            ?.takeIf { card -> canBePicked(card) }
            ?.let { card -> pickCard(card) }
    }

    private fun moveToDestination() {
        val move = Point(destination.angleTo(player.pos), playerSpeed)
        player.position(player.pos - move)
    }

    fun init() = parent?.apply {
        onClick {
            destination = it.currentPosStage
        }
    }

    private fun highlightCardOnHover() {
        deselectAllCards()
        field.getSelectedCard(player)?.let {
            deselectAllCards()
            if (canBePicked(it)) {
                it.setHighlight(true)
            }
        }
    }

    private fun canBePicked(card: Card) =
        !card.rotating && card != cardA && card != cardB && !card.collected && !pickingCard

    private suspend fun pickCard(card: Card, cardA: Card? = this.cardA, cardB: Card? = this.cardB) {
        pickingCard = true
        if (cardA == null) {
            firstTurn(card)
        } else if (cardB == null) {
            secondTurn(card, cardA)
        } else { // 3rd Card
            thirdTurn(card, cardA, cardB)
        }
        pickingCard = false
    }

    private suspend fun firstTurn(card: Card) {
        this.cardA = card
        card.takeUp()
    }

    private suspend fun secondTurn(card: Card, cardA: Card) {
        if (card.type == cardA.type) {
            collectCards(card, cardA)
        } else {
            this.cardB = card
            card.takeUp()
        }
    }

    private suspend fun thirdTurn(card: Card, cardA: Card, cardB: Card) {
        this.cardA = card
        this.cardB = null
        stage?.launchImmediately {
            cardA.takeOff()
        }
        stage?.launchImmediately {
            cardB.takeOff()
        }
        card.takeUp()
    }

    private suspend fun collectCards(card: Card, cardA: Card) {
        this.cardA = null
        this.cardB = null
        card.collected = true
        cardA.collected = true
        card.takeUp()
        stage?.launch {
            card.collect()
        }
        stage?.launch {
            cardA.collect()
        }
    }

    private fun deselectAllCards() = field.cards.forEach {
        it.setHighlight(false)
    }
}