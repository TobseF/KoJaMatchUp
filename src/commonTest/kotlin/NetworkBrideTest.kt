import de.tfr.kojamatch.game.network.MultiplayerEvent
import de.tfr.kojamatch.game.network.NewPositionEvent
import de.tfr.kojamatch.game.network.Packet
import de.tfr.kojamatch.game.network.PlayerPos
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class NetworkBrideTest {

  private val json = Json

  @Test
  fun testGameEventToJson() {
    val event = NewPositionEvent(PlayerPos(2, 2))
    val json = json.encodeToString(event)
    assertEquals("""{"pos":{"x":2,"y":2}}""", json)
  }

  @Test
  fun testJsonToGameEvent() {
    val event = NewPositionEvent(PlayerPos(2, 2))
    val decoded = json.decodeFromString<NewPositionEvent>("""{"pos":{"x":2,"y":2}}""")
    assertEquals(event, decoded)
  }

  @Test
  fun testJsonToPacket() {
    val event = NewPositionEvent(PlayerPos(2, 2))
    val packet = Packet(event)
    val decoded = json.decodeFromString<Packet>("""{"c":{"type":"EPos","pos":{"x":2,"y":2}}}""")
    assertEquals(packet, decoded)
  }

  @Test
  fun testPacketToJson() {
    val event = NewPositionEvent(PlayerPos(2, 2))
    val packet = Packet(event)
    val json = json.encodeToString(packet)
    assertEquals("""{"c":{"type":"EPos","pos":{"x":2,"y":2}}}""", json)
  }

  @Test
  fun testMultiplayerEventToJson() {
    val event = MultiplayerEvent(
      listOf(
        PlayerPos(1, 1, 1),
        PlayerPos(2, 2, 1)
      )
    )
    val packet = Packet(event)
    val json = json.encodeToString(packet)
    assertEquals("""{"c":{"type":"EMul","pos":[{"x":1,"y":1,"id":1},{"x":2,"y":2,"id":1}]}}""", json)
  }
}