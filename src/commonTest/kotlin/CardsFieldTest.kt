import de.tfr.kojamatch.game.CardsField
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test for [CardsField]
 */
class CardsFieldTest {

    @Test
    fun `test list pairs`() {
        val cards = CardsField.listPairs(2, 3)
        assertEquals(listOf(0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5), cards)
    }

    @Test
    fun `test list pairs uneven`() {
        val cards = CardsField.listPairs(1, 3)
        assertEquals(listOf(0, 0, 1, 1), cards)
    }
}