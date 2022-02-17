import com.soywiz.klock.seconds
import com.soywiz.korge.Korge
import com.soywiz.korge.tween.get
import com.soywiz.korge.tween.tween
import com.soywiz.korge.view.*
import com.soywiz.korgw.GameWindow
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.SizeInt
import com.soywiz.korma.geom.degrees
import com.soywiz.korma.interpolation.Easing

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
    val minDegrees = (-16).degrees
    val maxDegrees = (+16).degrees

    val playerImage = resourcesVfs["player-single.png"].readBitmap()
    Player(playerImage).addTo(this)

    val image = image(resourcesVfs["korge.png"].readBitmap()) {
        rotation = maxDegrees
        scale(.2)
    }.alignRightToRightOf(this, 20).alignTopToTopOf(this, 20)

    while (true) {
        image.tween(image::rotation[minDegrees], time = 1.seconds, easing = Easing.EASE_IN_OUT)
        image.tween(image::rotation[maxDegrees], time = 1.seconds, easing = Easing.EASE_IN_OUT)
    }
}

