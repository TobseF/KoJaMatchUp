package de.tfr.kojamatch.game

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.solidRect
import com.soywiz.korim.color.Colors
import com.soywiz.korui.layout.MathEx.min
import virtualResolution
import kotlin.math.round
import kotlin.random.Random

/**
 * Field with [Card]s
 */
class CardsField(private val columns: Int, private val rows: Int, resources: Resources) : Container() {

    private val minimalGap = 20
    private val fieldHeight = (virtualResolution.height * 0.35).toInt()
    private val fieldWith = virtualResolution.width

    val cards = mutableListOf<Card>()

    private fun shuffleCards(): List<Int> {
        return listPairs(columns, rows).shuffled(Random.Default)
    }

    companion object {
        fun listPairs(columns: Int, rows: Int): List<Int> {
            val cardsCount = round((columns * rows) / 2.0).toInt()
            return (0 until cardsCount).flatMap { listOf(it, it) }
        }
    }

    fun getSelectedCard(player: Player): Card? {
        return cards.firstOrNull { player.touches(it) }
    }

    init {
        val cardWidth = (fieldWith / columns) - minimalGap
        val cardHeight = (fieldHeight / rows) - minimalGap
        val cardSize = min(cardWidth, cardHeight)

        solidRect(width = fieldWith, height = fieldHeight, color = Colors.GREEN)
        val gapColumn = (fieldWith - (columns * cardSize)) / (columns + 1)
        val gapRow = (fieldHeight - (rows * cardSize)) / (rows + 1)
        var card = 0
        val cardTypes = shuffleCards()
        for (column in (0 until columns)) {
            for (row in (0 until rows)) {
                val cardType = cardTypes.getOrNull(card)
                if (cardType != null) {
                    val cardX = (column * (cardSize + gapColumn)) + gapColumn
                    val cardY = (row * (cardSize + gapRow)) + gapRow
                    cards += Card(cardSize, cardType, cardX, cardY, column, row, resources).addTo(this)
                }
                card++
            }
        }

    }
}