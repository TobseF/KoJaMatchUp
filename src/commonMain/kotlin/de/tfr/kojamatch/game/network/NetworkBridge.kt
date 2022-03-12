package tfr.korge.jam.roguymaze.model.network

import com.soywiz.klogger.Logger
import com.soywiz.korge.bus.GlobalBus
import com.soywiz.korinject.AsyncDependency
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korio.async.launch
import com.soywiz.korio.net.ws.WebSocketClient
import de.tfr.kojamatch.game.network.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class NetworkBridge(
  private val bus: GlobalBus,
  private val scope: CoroutineScope
) : AsyncDependency {
  private val json = Json

  private val dummyHost = "ws://echo.websocket.org"
  private val localHost = "ws://localhost:8080"
  private val liveHost = "wss://match-up-server.app.tobse.eu"
  private var host = liveHost

  private var socket: WebSocketClient? = null
  private val log = Logger<NetworkBridge>()
  private var connected = false
  private var playerId = -1

  init {
    bus.register<NewPositionEvent> {
      broadcast(it)
    }
    bus.register<NewCardEvent> {
      broadcast(it)
    }
  }

  private suspend fun broadcast(packet: Packet) {
    log.debug { "broadcast: $packet" }
    val parsed = json.encodeToString(packet)
    if (socket == null) {
      log.warn { "Ignoring network message, because not ready: $parsed" }
    } else {
      socket?.send(parsed)
    }
  }

  suspend fun broadcast(gameEvent: GameEvent) {
    broadcast(gameEvent.packed())
  }

  suspend fun handleData(data: String) {
    log.debug { "Received data: $data" }
    val parsed = json.decodeFromStringOrNull<Packet>(data)
    if (parsed == null) {
      log.warn { "Failed to parse network data: $data" }
    } else {
      when (val event = parsed.clientEvent) {
        is MultiplayerEvent -> {
          bus.send(event)
        }
        is ConnectedEvent -> {
          playerId = event.playerId
          bus.send(event)
          connected = true
          log.info { "Connected as player: $playerId" }
        }
        is PlayerGoneEvent -> {
          bus.send(event)
          log.info { "Player quit: $playerId" }
        }
        is NewScoreEvent -> {
          bus.send(event)
        }

        else -> {}
      }
    }
  }

  companion object {
    suspend operator fun invoke(injector: AsyncInjector): NetworkBridge {
      injector.mapSingleton {
        NetworkBridge(get(), get())
      }
      return injector.get()
    }
  }

  private suspend fun openSocket(): WebSocketClient {
    val url = "$host/game"
    log.info { "Creating WebSocketClient: $url" }
    val socket = WebSocketClient(url, debug = false)
    configureSocket(socket)
    return socket
  }

  private suspend fun configureSocket(socket: WebSocketClient) {
    socket.onStringMessage.add {
      scope.launch {
        handleData(it)
      }
    }
    socket.onError.add {
      log.error { "Error on socket: $it" }
      this.socket = null
    }

    socket.onOpen.add {
    }
  }

  override suspend fun init() {
    log.debug { "Starting network bridge" }
    scope.launch {
      try {
        socket = openSocket()
      } catch (e: Exception) {
        log.error { "Failed opening web Socket: $e" }
      }
    }
  }


}