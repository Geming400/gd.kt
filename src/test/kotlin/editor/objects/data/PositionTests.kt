package fr.geming400.gddotkt.editor.objects.data

import fr.geming400.gddotkt.TestTags
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

const val X = 5f
const val Y = 7f

@Tag(TestTags.EDITOR)
private class PositionTests {
    @Test
    @DisplayName("Pos class 'actual poses' test")
    fun posActualPosTest() {
        val pos = Pos(X, Y)
        Assertions.assertEquals(X, pos.actualX)
        Assertions.assertEquals(Y, pos.actualY)
    }

    @Test
    @DisplayName("Pos constructor test")
    fun posCtorTest() {
        Assertions.assertEquals(Pos.gridCentered(0f, 1f), Pos(15f, 45f))
        Assertions.assertEquals(Pos.gridUncentered(1f, 2f), Pos(30f, 60f))

        Assertions.assertEquals(Pos.gridCentered(0f, 0f), Pos(0f.gridCentered, 0f.gridCentered))
        Assertions.assertEquals(Pos.gridUncentered(0f, 0f), Pos(0f.gridUncentered, 0f.gridUncentered))
    }

    @Test
    @DisplayName("Pos -> GridPos test")
    fun posToGridPosTest() {
        val pos = Pos.gridUncentered(X, Y)
        Assertions.assertEquals(GridPos.gridUncentered(X, Y), pos.gridPos)

        val pos2 = Pos.gridCentered(X, Y)
        Assertions.assertEquals(GridPos.gridCentered(X, Y), pos2.gridPos)
    }


    @Test
    @DisplayName("GridPos class 'actual poses' test")
    fun gridPosActualPosTest() {
        val pos = GridPos(X, Y)
        Assertions.assertEquals(X * Position.GRID_UNIT, pos.actualX)
        Assertions.assertEquals(Y * Position.GRID_UNIT, pos.actualY)
    }

    @Suppress("KotlinMisorderedAssertEqualsArguments")
    @Test
    @DisplayName("GridPos constructor test")
    fun gridPosCtorTest() {
        Assertions.assertEquals(Pos(45f, 75f), Pos.gridCentered(1f, 2f))
        Assertions.assertEquals(Pos(30f, 60f), Pos.gridUncentered(1f, 2f))
    }

    @Test
    @DisplayName("GridPos -> Pos test")
    fun gridPosToGridPosTest() {
        val pos = GridPos.gridUncentered(X, Y)
        Assertions.assertEquals(Pos.gridUncentered(X, Y), pos.gdPos)

        val pos2 = GridPos.gridCentered(X, Y)
        Assertions.assertEquals(Pos.gridCentered(X, Y), pos2.gdPos)
    }
}