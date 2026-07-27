package fr.geming400.gddotkt.rawstring

/**
 * An id is a way to store an identifier in a numerical or string type.
 * Numerical types are [unsigned integers][UInt] because in geometry dash, property ids don't go below `1`.
 *
 * You can create an [Id] instance, either by:
 * - Using [ofNumerical] and [ofString]
 * - Or use the 3 extensions [Int.id], [UInt.id] and [String.id]
 */
@ConsistentCopyVisibility
data class Id private constructor(val numericalID: UInt?, val stringID: String?) {
    companion object {
        fun ofNumerical(numericalID: UInt) = Id(numericalID.coerceAtLeast(1u), null)
        fun ofString(stringID: String) = Id(null, stringID)
    }

    /**
     * Gets the [numerical id][numericalID] of this ID.
     * However, if it's `null` an exception gets thrown
     * @return the [numerical id][numericalID] of this ID
     * @throws NullPointerException if the [numerical id][numericalID] is `null`
     */
    fun getNumericalIdStrict(): UInt {
        if (this.numericalID == null)
            throw NullPointerException("Tried getting numerical id of $this but failed because it's null.")

        return this.numericalID
    }

    /**
     * Gets the [string id][stringID] of this ID.
     * However, if it's `null` an exception gets thrown
     * @return the [string id][stringID] of this ID
     * @throws NullPointerException if the [string id][stringID] is `null`
     */
    fun getStringIdStrict(): String {
        if (this.stringID == null)
            throw NullPointerException("Tried getting string id of $this but failed because it's null.")

        return this.stringID
    }

    /**
     * Gets the string representation of this id.
     * It chooses between the numerical id and the string one
     */
    fun getID(): String =
        if (this.numericalID == null)
            this.stringID!!
        else
            this.numericalID.toString()

    fun getType(): Type =
        if (this.numericalID == null)
            Type.NUMERICAL
        else
            Type.STRING

    override fun toString(): String {
        return "${this::class.simpleName}{id = ${this.getID()}}"
    }

    enum class Type {
        NUMERICAL,
        STRING
    }
}

/**
 * Creates a numerical ID for this integer
 * @see Id.ofNumerical
 */
val Int.id: Id
    get() = Id.ofNumerical(this.coerceAtLeast(1).toUInt())


/**
 * Creates a numerical ID for this unsigned integer
 * @see Id.ofNumerical
 */
val UInt.id: Id
    get() = Id.ofNumerical(this)

/**
 * Creates a string ID for this string
 * @see Id.ofString
 */
val String.id: Id
    get() = Id.ofString(this)
