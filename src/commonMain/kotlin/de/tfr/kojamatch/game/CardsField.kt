package de.tfr.kojamatch.game

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.position
import com.soywiz.korge.view.solidRect
import com.soywiz.korim.color.Colors
import com.soywiz.korui.layout.MathEx.min
import virtualResolution


class CardsField(private val columns: Int, private val rows: Int) : Container() {

    private val minimalGap = 10
    private val fieldHeight = (virtualResolution.height * 0.35).toInt()
    private val fieldWith = virtualResolution.width

    init {
        val cardWidth = (fieldWith / columns) - minimalGap
        val cardHeight = (fieldHeight / rows) - minimalGap
        val cardSize = min(cardWidth, cardHeight)

        solidRect(width = fieldWith, height = fieldHeight, color = Colors.GREEN)
        val gapColumn = (fieldWith - (columns * cardSize)) / (columns + 1)
        val gapRow = (fieldHeight - (rows * cardSize)) / (rows + 1)
        for (column in (0 until columns)) {
            for (row in (0 until rows)) {
                solidRect(width = cardSize, height = cardSize, color = Colors.RED) {
                    position(
                        (column * (cardSize + gapColumn)) + gapColumn,
                        (row * (cardSize + gapRow)) + gapRow
                    )
                }
            }
        }


    }
}