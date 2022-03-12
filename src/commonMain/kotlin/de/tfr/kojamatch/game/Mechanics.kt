package de.tfr.kojamatch.game

import com.soywiz.klock.milliseconds
import com.soywiz.korev.Key
import com.soywiz.korge.bus.GlobalBus
import com.soywiz.korge.input.keys
import com.soywiz.korge.input.onClick
import com.soywiz.korge.input.touch
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korio.async.launch
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korma.geom.Point
import de.tfr.kojamatch.game.network.*

class Mechanics(
  override val stage: Stage, private val bus: GlobalBus,
  private val field: CardsField, private val player: Player,
  private val resources: Resources,
) : Container() {

  private var cardA: Card? = null
  private var cardB: Card? = null
  private var selected: Card? = null
  private var pickingCard = false
  private var target: Point = player.pos.copy()
  private val playerSpeed = 6.0

  private val clickPos: Circle = circle(radius = 4.0).centered.visible(false)
  private val multiplayer = mutableMapOf<Int, Player>()
  private val multiplayerTargets = mutableMapOf<Int, PlayerPos>()

  /**
   * Online Player ID
   */
  private var playerID = -1

  init {
    bus.register<MultiplayerEvent> {
      updateMultiplayer(it)
    }
    bus.register<ConnectedEvent> {
      playerID = it.playerId
      bus.send(NewPositionEvent(player.pos.toPlayerPos(playerID)))
      stage.circle(radius = 8.0, fill = Colors.GREEN) {
      }.alignTopToTopOf(stage, 20).alignRightToRightOf(stage, 20)
    }
    bus.register<PlayerGoneEvent> {
      removePlayer(it.playerId)
    }
    keys {
      down {
        if (it.key == Key.SPACE) {
          pickUpCard()
        }
      }
    }

    addUpdater {
      highlightCardOnHover()
    }

    addFixedUpdater(25.milliseconds) {
      movePlayer(player, target)
      multiplayerTargets.forEach { (id, targetPos) ->
        multiplayer[id]?.let { player ->
          movePlayer(player, targetPos.toPoint())
        }
      }
    }

    stage.onClick {
      clickOn(it.currentPosStage)
    }
    touch {
      this.infos.forEach {
        stage.launchImmediately {
          clickOn(it.local)
        }
      }
    }
  }

  private fun removePlayer(playerId: Int) {
    multiplayerTargets.remove(playerId)
    multiplayer[playerId]?.removeFromParent()
    multiplayer.remove(playerId)
  }

  private fun movePlayer(player: Player, target: Point) {
    if (target.distanceTo(player.pos) > playerSpeed) {
      player.walkTo(target, playerSpeed)
    } else {
      player.stopWalking()
    }
  }

  private fun updateMultiplayer(event: MultiplayerEvent) {
    event.positions.forEach { playerPos ->
      val player = multiplayer[playerPos.playerId]
      if (player == null) {
        val newPlayer = Player(resources, bus, playerPos.playerId, 100.milliseconds).apply {
          position(playerPos.x, playerPos.y)
          name = "Player ${playerPos.playerId}"
          scale = 0.6
        }.addTo(stage)
        multiplayer[playerPos.playerId] = newPlayer
      } else {
        multiplayerTargets[playerPos.playerId] = playerPos
      }
    }
  }

  private suspend fun Mechanics.pickUpCard() {
    bus.send(Events.PicUpEvent())
    field.getSelectedCard(player)
      ?.takeIf { card -> canBePicked(card) }
      ?.let { card -> pickCard(card) }
  }

  private suspend fun clickOn(point: Point) {
    clickPos.position(point)
    val found = field.getSelectedCard(clickPos as View)
    if (selected != null && selected == found) {
      pickUpCard()
    } else {
      target.copyFrom(point)
      bus.send(NewPositionEvent(point.toPlayerPos(playerID)))
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
    stage.launchImmediately {
      cardA.takeOff()
    }
    stage.launchImmediately {
      cardB.takeOff()
    }
    card.takeUp()
  }

  private suspend fun collectCards(card: Card, cardA: Card) {
    bus.send(NewCardEvent(player.pos.toPlayerPos(player.id)))
    this.cardA = null
    this.cardB = null
    card.collected = true
    cardA.collected = true
    card.takeUp()
    stage.launch {
      card.collect()
    }
    stage.launch {
      cardA.collect()
    }
  }

  private fun deselectAllCards() = field.cards.forEach {
    it.setHighlight(false)
    selected = null
  }

  private fun Point.toPlayerPos(id: Int) = PlayerPos(this.x.toInt(), this.y.toInt(), id)
  private fun PlayerPos.toPoint() = Point(this.x, this.y)
}