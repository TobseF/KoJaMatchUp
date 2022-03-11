import com.soywiz.klogger.Logger
import com.soywiz.korge.Korge
import com.soywiz.korge.bus.GlobalBus
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.alignBottomToBottomOf
import com.soywiz.korge.view.centerOnStage
import com.soywiz.korgw.GameWindow
import com.soywiz.korim.color.Colors
import com.soywiz.korma.geom.SizeInt
import de.tfr.kojamatch.game.*
import tfr.korge.jam.roguymaze.model.network.NetworkBridge

const val debug = false
const val cheatMode = false
const val multiplayer = true

/**
 * Virtual size which gets projected onto the [windowResolution]
 */
val virtualResolution = SizeInt(810, 1440)

/**
 * Actual window size
 */
val windowResolution = SizeInt(540, 960)

val backgroundColor = Colors["#2b2b2b"]


suspend fun main() = Korge(
    virtualHeight = virtualResolution.height,
    virtualWidth = virtualResolution.width,
    width = windowResolution.width,
    height = windowResolution.height,
    bgcolor = backgroundColor,
    debug = debug,
    title = "KoJa Match Up",
    quality = GameWindow.Quality.QUALITY,
) {
    Logger.defaultLevel = Logger.Level.DEBUG
    val bus = GlobalBus()

    val resources = Resources().init()

    val field = CardsField(4, 3, resources).addTo(this).alignBottomToBottomOf(this)
    val player = Player(resources, bus).addTo(this).centerOnStage()

    Mechanics(this, bus, field, player, resources).addTo(this)

    if (multiplayer) {
        NetworkBridge(bus, this).init()
    }

    KorgeLogo(bus, resources).addTo(this).init()
}

