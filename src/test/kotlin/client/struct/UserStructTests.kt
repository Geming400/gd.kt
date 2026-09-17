package client.struct

import client.GDClientApi
import exceptions.GdDotKtException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.CLIENT)
@OptIn(GDClientApi::class)
private class UserStructTests {
    @Test
    fun classicLevelsBreakdownTest() {
        val classicBreakdown = ClassicLevelsBreakdown("1,2,3,4,5,6,7,8")
        Assertions.assertEquals(1, classicBreakdown.auto)
        Assertions.assertEquals(2, classicBreakdown.easy)
        Assertions.assertEquals(3, classicBreakdown.normal)
        Assertions.assertEquals(4, classicBreakdown.hard)
        Assertions.assertEquals(5, classicBreakdown.harder)
        Assertions.assertEquals(6, classicBreakdown.insane)

        Assertions.assertEquals(7, classicBreakdown.daily)
        Assertions.assertEquals(8, classicBreakdown.gauntlet)

        Assertions.assertThrows(GdDotKtException::class.java) { ClassicLevelsBreakdown("1,2,3,4,5,6,7") }
        Assertions.assertThrows(GdDotKtException::class.java) { ClassicLevelsBreakdown("1,2,3,4,5,6,7,8,9") }
    }

    @Test
    fun platformerLevelsBreakdownTest() {
        val classicBreakdown = PlatformerLevelsBreakdown("1,2,3,4,5,6,7")
        Assertions.assertEquals(1, classicBreakdown.auto)
        Assertions.assertEquals(2, classicBreakdown.easy)
        Assertions.assertEquals(3, classicBreakdown.normal)
        Assertions.assertEquals(4, classicBreakdown.hard)
        Assertions.assertEquals(5, classicBreakdown.harder)
        Assertions.assertEquals(6, classicBreakdown.insane)

        Assertions.assertEquals(7, classicBreakdown.theMap)


        Assertions.assertThrows(GdDotKtException::class.java) { PlatformerLevelsBreakdown("1,2,3,4,5,6") }
        Assertions.assertThrows(GdDotKtException::class.java) { PlatformerLevelsBreakdown("1,2,3,4,5,6,7,8") }
    }

    @Test
    fun demonLevelsBreakdownTest() {
        val classicBreakdown = DemonLevelsBreakdown("1,2,3,4,5,6,7,8,9,10,11,12")
        Assertions.assertEquals(1, classicBreakdown.easyDemon)
        Assertions.assertEquals(2, classicBreakdown.mediumDemon)
        Assertions.assertEquals(3, classicBreakdown.hardDemon)
        Assertions.assertEquals(4, classicBreakdown.insaneDemon)
        Assertions.assertEquals(5, classicBreakdown.extremeDemon)

        Assertions.assertEquals(6, classicBreakdown.easyDemonPlatformer)
        Assertions.assertEquals(7, classicBreakdown.mediumDemonPlatformer)
        Assertions.assertEquals(8, classicBreakdown.hardDemonPlatformer)
        Assertions.assertEquals(9, classicBreakdown.insaneDemonPlatformer)
        Assertions.assertEquals(10, classicBreakdown.extremeDemonPlatformer)

        Assertions.assertEquals(11, classicBreakdown.weekly)
        Assertions.assertEquals(12, classicBreakdown.gauntlet)


        Assertions.assertThrows(GdDotKtException::class.java) { DemonLevelsBreakdown("1,2,3,4,5,6,7,8,9,10,11") }
        Assertions.assertThrows(GdDotKtException::class.java) { DemonLevelsBreakdown("1,2,3,4,5,6,7,8,9,10,11,12,13") }
    }
}