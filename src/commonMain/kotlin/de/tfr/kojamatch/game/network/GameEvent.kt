package de.tfr.kojamatch.game.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class GameEvent

@Serializable
@SerialName("Pack")
data class Packet(
  @SerialName("c")
  val clientEvent: GameEvent
)

fun GameEvent.packed() = Packet(this)

@Serializable
@SerialName("P")
data class PlayerPos(
  var x: Int, var y: Int,
  @SerialName("id")
  override val playerId: Int = -1
) : Named

interface Named {
  val playerId: Int
  fun hasPlayerID() = playerId >= 0
}

@Serializable
@SerialName("EMul")
data class MultiplayerEvent(
  @SerialName("pos")
  val positions: List<PlayerPos>
) : GameEvent()

@Serializable
@SerialName("Con")
data class ConnectedEvent(
  override val playerId: Int = -1,
  val score: Int = -1
) : GameEvent(), Named


fun PlayerPos.toMultiplayerEvent() = MultiplayerEvent(listOf(this))

@Serializable
@SerialName("EPlayerGone")
data class PlayerGoneEvent(
  override val playerId: Int = -1
) : GameEvent(), Named


@Serializable
@SerialName("EPos")
data class NewPositionEvent(
  val pos: PlayerPos
) : GameEvent()

@Serializable
@SerialName("ECard")
data class NewCardEvent(
  val pos: PlayerPos
) : GameEvent()

@Serializable
@SerialName("NScore")
data class NewScoreEvent(
  val score: Int,
  val pos: PlayerPos
) : GameEvent()