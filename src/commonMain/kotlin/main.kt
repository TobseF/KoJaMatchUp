import com.soywiz.korge.Korge
import com.soywiz.korge.bus.GlobalBus
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.alignBottomToBottomOf
import com.soywiz.korge.view.centerOnStage
import com.soywiz.korgw.GameWindow
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.SizeInt
import de.tfr.kojamatch.game.*

const val debug = false

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
    val bus = GlobalBus()

    val resources = Resources().init()

    val field = CardsField(4, 3, resources).addTo(this).alignBottomToBottomOf(this)
    val player = Player(resources, bus).addTo(this).centerOnStage()

    Mechanics(bus, field, player).addTo(this)

    val logo = resourcesVfs["korge.png"].readBitmap()
    KorgeLogo(logo).addTo(this).init()
}

