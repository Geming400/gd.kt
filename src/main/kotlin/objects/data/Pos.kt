package fr.geming400.gddotkt.objects.data

sealed interface Position {
    companion object {
        const val GRID_UNIT: Int = 30
        const val GRID_OFFSET: Int = 15
    }

    /**
     * The actual x position of this position object.
     *
     * This is useful for [GridPos] because its position fields ([x][GridPos.x] / [y][GridPos.y])
     * don't actually represent the true geometry dash position
     */
    val actualX: Float
    val actualY: Float
}

/**
 * The position for a geometry dash object.
 * 1 `grid unit` = `30`
 */
data class Pos(
    val x: Float,
    val y: Float
) : Position {
    companion object {
        /**
         * Create a [Pos] object on the intersections of geometry dash's grid
         * @return the [Pos] object with the grid coordinates
         * @see gridCentered
         */
        fun gridUncentered(x: Float, y: Float): Pos =
            Pos(x.grid, y.grid)

        /**
         * Create a [Pos] object on geometry dash's grid
         * @return the [Pos] object with the grid coordinates
         * @see gridUncentered
         */
        fun gridCentered(x: Float, y: Float): Pos =
            Pos(x.offsetGrid, y.offsetGrid)

        fun ofPos(pos: Position): Pos =
            Pos(pos.actualX, pos.actualY)
    }

    override val actualX = this.x
    override val actualY = this.y

    val gridPos: GridPos
        get() = GridPos.ofPos(this)
}

/**
 * The position for a geometry dash object based on the gd editor's grid
 * 1 `grid unit` = `30`
 */
data class GridPos(
    val x: Float,
    val y: Float
) : Position {
    companion object {
        /**
         * Create a [GridPos] object on the intersections of geometry dash's grid
         * @return the [GridPos] object with the grid coordinates
         * @see gridCentered
         */
        fun gridUncentered(x: Float, y: Float): GridPos =
            GridPos(x, y)

        /**
         * Create a [GridPos] object on geometry dash's grid
         * @return the [GridPos] object with the grid coordinates
         * @see gridUncentered
         */
        fun gridCentered(x: Float, y: Float): GridPos =
            GridPos(x + Position.GRID_OFFSET / Position.GRID_UNIT, y + Position.GRID_OFFSET / Position.GRID_UNIT)

        fun ofPos(pos: Position): GridPos =
            GridPos(pos.actualX / Position.GRID_UNIT, pos.actualY / Position.GRID_UNIT)
    }

    override val actualX = this.x * Position.GRID_UNIT
    override val actualY = this.y * Position.GRID_UNIT

    val gdPos: Pos
        get() = Pos.ofPos(this)
}

/**
 * Get the grid value relative to geometry dash's position units where
 * 1 `grid unit` = `30`
 *
 * This is a quick way to multiply this value by `30`
 * @see Int.offsetGrid
 * @see Pos.gridCentered
 */
inline val Int.grid: Int
    get() = this * Position.GRID_UNIT

/**
 * Get the grid value relative to geometry dash's position units where
 * 1 `grid unit` = `30` and center it on the grid
 *
 * This is equal to `val * 30 + 15`
 * @see Int.grid
 * @see Pos.gridCentered
 */
inline val Int.offsetGrid: Int
    get() = this.grid + Position.GRID_OFFSET


/**
 * Get the grid value relative to geometry dash's position units where
 * 1 `grid unit` = `30`
 *
 * This is a quick way to multiply this value by `30`
 * @see Float.offsetGrid
 * @see Pos.gridCentered
 */
inline val Float.grid: Float
    get() = this * Position.GRID_UNIT

/**
 * Get the grid value relative to geometry dash's position units where
 * 1 `grid unit` = `30` and center it on the grid
 *
 * This is equal to `val * 30 + 15`
 * @see Float.grid
 * @see Pos.gridCentered
 */
inline val Float.offsetGrid: Float
    get() = this.grid + Position.GRID_OFFSET
