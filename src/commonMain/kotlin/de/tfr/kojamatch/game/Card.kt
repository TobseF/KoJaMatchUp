package de.tfr.kojamatch.game

import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors

/**
 * Memory card on the [CardsField]
 */
class Card(size: Int, type: Int, x: Int, y: Int) : Container() {
    val borderRect: SolidRect

    private val border = 4.0

    init {
        position(x, y)
        borderRect = solidRect(size + (border * 2), size + (border * 2)) {
            color = Colors.RED
            position(-border, -border)
        }
        solidRect(size, size, color = Colors.LIGHTYELLOW)
        text("" + type, textSize = 42.0, color = Colors.BLACK).centerOn(this)
    }

    fun setHighlight(visible: Boolean) {
        borderRect.visible = visible
    }

}