package fr.geming400.gddotkt.objects.data

import fr.geming400.gddotkt.TestTags
import fr.geming400.gddotkt.editor.objects.data.Hsv
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.EDITOR)
private class HsvTests {
    private fun testHsvCtor(hsv: Hsv, expectedSatAdditive: Boolean, expectedBrightnessAdditive: Boolean) {
        Assertions.assertEquals(hsv.isSatChecked, expectedSatAdditive)
        Assertions.assertEquals(hsv.isBrightnessChecked, expectedBrightnessAdditive)
        Assertions.assertEquals(0, hsv.hue)
        Assertions.assertEquals(0f, hsv.sat)
        Assertions.assertEquals(0f, hsv.brightness)
    }

    @Test
    fun rawStringTest() {
        val hsv = Hsv.create(0, 0f, 0f)
        Assertions.assertEquals("0a0.0a0.0a0a0", hsv.asRawString())
    }

    @Test
    fun ctorTests() {
        testHsvCtor(Hsv.create(0, 0f, 0f), expectedSatAdditive = false, expectedBrightnessAdditive = false)
        testHsvCtor(Hsv.checkedSat(0, 0f, 0f), expectedSatAdditive = true, expectedBrightnessAdditive = false)
        testHsvCtor(Hsv.checkedBrightness(0, 0f, 0f), expectedSatAdditive = false, expectedBrightnessAdditive = true)
        testHsvCtor(Hsv.checkedSatBrightness(0, 0f, 0f), expectedSatAdditive = true, expectedBrightnessAdditive = true)
    }

    @Test
    fun offsetTest() {
        // The saturation and brightness are unchecked
        val hsv = Hsv.create(saturation = 1f, brightness = 1f)

        // They are already unchecked, nothing should change
        hsv.isSatChecked = false
        hsv.isBrightnessChecked = false
        Assertions.assertEquals(1f, hsv.sat)
        Assertions.assertEquals(1f, hsv.brightness)


        // When checking we add -1
        hsv.isSatChecked = true
        hsv.isBrightnessChecked = true
        Assertions.assertEquals(0f, hsv.sat)
        Assertions.assertEquals(0f, hsv.brightness)

        // They are already checked, nothing should change
        hsv.isSatChecked = true
        hsv.isBrightnessChecked = true
        Assertions.assertEquals(0f, hsv.sat)
        Assertions.assertEquals(0f, hsv.brightness)


        // When unchecking we add 1
        hsv.isSatChecked = false
        hsv.isBrightnessChecked = false
        Assertions.assertEquals(1f, hsv.sat)
        Assertions.assertEquals(1f, hsv.brightness)
    }

    @Test
    fun clampingTest() {
        val hsv = Hsv.create(
            200,        // should clamp to 180
            -0.4f, // should clamp to 0.0
            2.7f  // should clamp to 2.0
        )

        Assertions.assertEquals(180, hsv.hue)
        Assertions.assertEquals(0f, hsv.sat)
        Assertions.assertEquals(2f, hsv.brightness)

        hsv.isSatChecked = true
        hsv.isBrightnessChecked = true

        hsv.hue = -170
        hsv.sat = -1.5f
        Assertions.assertEquals(-170, hsv.hue)
        Assertions.assertEquals(-1f, hsv.sat)
        Assertions.assertEquals(1f, hsv.brightness) // automatically clamped with `hsv.isBrightnessChecked = True`

        hsv.brightness = -1.5f
        Assertions.assertEquals(-1f, hsv.brightness)

        hsv.isBrightnessChecked = false
        Assertions.assertEquals(0f, hsv.brightness)

        hsv.sat = 2.5f
        Assertions.assertEquals(1f, hsv.sat)

        hsv.isSatChecked = false
        Assertions.assertEquals(2f, hsv.sat)
        hsv.sat = 3.0f
        Assertions.assertEquals(2f, hsv.sat)

        hsv.sat = 1.0f
        hsv.brightness = 1.25f
        hsv.hue = 136

        Assertions.assertEquals(1f, hsv.sat)
        Assertions.assertEquals(1.25f, hsv.brightness)
        Assertions.assertEquals(136, hsv.hue)
        Assertions.assertFalse(hsv.isSatChecked)
        Assertions.assertFalse(hsv.isBrightnessChecked)
    }
}
