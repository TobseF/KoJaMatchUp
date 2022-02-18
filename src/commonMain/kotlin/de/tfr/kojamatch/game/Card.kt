package de.tfr.kojamatch.game

import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors

/**
 * Memory card on the [CardsField]
 */
class Card(size: Int, type: Int, x: Int, y: Int) : Container() {

    init {
        position(x, y)
        solidRect(size, size, color = Colors.RED)
        text("" + type, textSize = 42.0).centerOn(this)
    }
}