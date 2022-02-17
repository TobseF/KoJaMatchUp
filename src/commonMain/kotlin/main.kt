import com.soywiz.klock.seconds
import com.soywiz.korge.Korge
import com.soywiz.korge.tween.get
import com.soywiz.korge.tween.tween
import com.soywiz.korge.view.anchor
import com.soywiz.korge.view.image
import com.soywiz.korge.view.position
import com.soywiz.korge.view.scale
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.degrees
import com.soywiz.korma.interpolation.Easing

suspend fun main() = Korge(width = 512, height = 512, bgcolor = Colors["#2b2b2b"]) {
    val minDegrees = (-16).degrees
    val maxDegrees = (+16).degrees

    val image = image(resourcesVfs["korge.png"].readBitmap()) {
        rotation = maxDegrees
        anchor(.5, .5)
        scale(.8)
        position(256, 256)
    }

    while (true) {
        image.tween(image::rotation[minDegrees], time = 1.seconds, easing = Easing.EASE_IN_OUT)
        image.tween(image::rotation[maxDegrees], time = 1.seconds, easing = Easing.EASE_IN_OUT)
    }
}