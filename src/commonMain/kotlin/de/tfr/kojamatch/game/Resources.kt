package de.tfr.kojamatch.game

import com.soywiz.korim.atlas.Atlas
import com.soywiz.korim.atlas.readAtlas
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs

class Resources {

    lateinit var logo: Bitmap
    lateinit var playerImage: Bitmap
    lateinit var cardBackGround: Bitmap
    lateinit var cards: List<Bitmap>
    lateinit var playerSpriteSheet: Atlas
    lateinit var star: Bitmap


    suspend fun init(): Resources {
        logo = resourcesVfs["korge.png"].readBitmap()
        star = resourcesVfs["star.png"].readBitmap()
        cardBackGround = resourcesVfs["backside.png"].readBitmap()
        playerImage = resourcesVfs["player-single.png"].readBitmap()
        playerSpriteSheet = resourcesVfs["player.xml"].readAtlas()
        cards = (0..9).map { "animals-$it.png" }.map { resourcesVfs[it].readBitmap() }
        return this
    }

    fun getCard(index: Int) = cards[index.mod(cards.size)]
}