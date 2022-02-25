package de.tfr.kojamatch.game

import com.soywiz.klock.milliseconds
import com.soywiz.korev.Key
import com.soywiz.korge.bus.GlobalBus
import com.soywiz.korge.input.keys
import com.soywiz.korge.input.onClick
import com.soywiz.korge.input.touch
import com.soywiz.korge.view.*
import com.soywiz.korio.async.launch
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korma.geom.Point
import com.soywiz.korui.layout.MathEx
import virtualResolution

class Mechanics(val bus: GlobalBus, val field: CardsField, val player: Player) : Container() {

    private var cardA: Card? = null
    private var cardB: Card? = null
    private var selected: Card? = null
    private var pickingCard = false
    private var destination: Point = player.pos.copy()
    private val playerSpeed = 6.0
    private val playerStep = 20
    private val clickPos: Circle = circle(radius = 4.0).centered.visible(false)

    init {
        keys {
            down {
                if (it.key == Key.SPACE) {
                    pickUpCard()
                }
            }
        }
        touch {
            this.start.once {
                destination = it.local
            }
            this.end.once {
                destination = it.local
            }
        }

        addUpdater {
            highlightCardOnHover()
            handeKeyboardControls()
        }

        addFixedUpdater(25.milliseconds) {
            if (destination.distanceTo(player.pos) > playerSpeed) {
                moveToDestination()
            }
        }
    }

    private fun handeKeyboardControls() {
        if (anyMoveKeyIsPressed()) {
            resetDestination()
        }
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

    private fun anyMoveKeyIsPressed() = (arrowKeys() + wasdKeys()).any { it.isPressed() }

    private fun arrowKeys() = listOf(Key.UP, Key.DOWN, Key.LEFT, Key.RIGHT)
    private fun wasdKeys() = listOf(Key.W, Key.A, Key.S, Key.S)

    private fun Key.isPressed() = stage?.input?.keys?.get(this) ?: false

    private fun resetDestination() {
        destination.copyFrom(player.pos)
    }

    private fun moveLeft() {
        destination.x = MathEx.max(destination.x - playerStep, 0.0)
    }

    private fun moveRight() {
        destination.x = MathEx.min(destination.x + playerStep, virtualResolution.width.toDouble())
    }

    private fun moveDown() {
        destination.y = MathEx.min(destination.y + playerStep, virtualResolution.height.toDouble())
    }

    private fun moveUp() {
        destination.y = MathEx.max(destination.y - playerStep, 0.0)
    }

    private suspend fun Mechanics.pickUpCard() {
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
            clickPos.position(it.currentPosStage)
            val found = field.getSelectedCard(clickPos as View)
            if (selected != null && selected == found) {
                pickUpCard()
            } else {
                destination.copyFrom(it.currentPosStage)
            }
        }
    }

    private fun highlightCardOnHover() {
        deselectAllCards()
        field.getSelectedCard(player)?.let {
            if (canBePicked(it)) {
                it.setHighlight(true)
                selected = it
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
        selected = null
    }
}