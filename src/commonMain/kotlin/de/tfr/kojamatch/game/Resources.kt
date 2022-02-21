package de.tfr.kojamatch.game

import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs

class Resources {

    lateinit var playerImage: Bitmap
    lateinit var cardBackGround: Bitmap
    lateinit var cards: List<Bitmap>

    suspend fun init(): Resources {
        cardBackGround = resourcesVfs["backside.png"].readBitmap()
        playerImage = resourcesVfs["player-single.png"].readBitmap()
        cards = (0..9).map { "animals-$it.png" }.map { resourcesVfs[it].readBitmap() }
        return this
    }

    fun getCard(index: Int) = cards[index.mod(cards.size)]
}