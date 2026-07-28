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
data class Id private constructor(val numericalID: UInt?, val stringID: String?): Comparable<Id> {
    companion object {
        fun ofNumerical(numericalID: UInt) = Id(numericalID.coerceAtLeast(1u), null)

        fun ofString(stringID: String) = Id(null, stringID)

        /**
         * Creates a numerical or string [Id] object.
         * It tries converting your [id] to an int, and depending on if it fails
         * or not it will return an id with the correct underlying [type]
         * @throws IllegalArgumentException if the given [id] is below `0` (exclusive, so `< 0`)
         */
        fun ofUnknown(id: String): Id {
            val asUInt = id.toUIntOrNull()
            return if (asUInt == null) {
                val asInt = id.toIntOrNull()
                if (asInt == null) {
                    ofString(id)
                } else {
                    throw IllegalArgumentException("Id argument (= $id) isn't a valid UInt", NumberFormatException("Invalid number format: '$id'"))
                }
            } else {
                ofNumerical(asUInt)
            }
        }
    }

    val type: Type
        get() =
            if (this.numericalID == null)
                Type.STRING
            else
                Type.NUMERICAL

    /**
     * Gets the [numerical id][numericalID] of this ID.
     * However, if it's `null` an exception gets thrown
     * @return the [numerical id][numericalID] of this ID
     * @throws NullPointerException if the [numerical id][numericalID] is `null`
     */
    fun getNumericalIdStrict(): UInt {
        if (this.numericalID == null)
            throw NullPointerException("Tried getting numerical id $this but failed because this id object is a string id and not a numerical id.")

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
            throw NullPointerException("Tried getting string id $this but failed because this id object is a numerical id and not a string id.")

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

    override fun compareTo(other: Id): Int {
        if (this.type == Type.STRING) {
            return if (other.type == Type.STRING) {
                // this: STRING ; other: STRING
                this.getStringIdStrict().compareTo(other.getStringIdStrict())
            } else {
                // this: STRING ; other: NUMERICAL
                this.getStringIdStrict().compareTo(other.getID())
            }
        } else {
            return if (other.type == Type.STRING) {
                // this: NUMERICAL ; other: STRING
                this.getID().compareTo(other.getStringIdStrict())
            } else {
                // this: NUMERICAL ; other: NUMERICAL
                this.getNumericalIdStrict().compareTo(other.getNumericalIdStrict())
            }
        }
    }

    /**
     * Returns a string representation of the object.
     * If you are looking to get the **string representation** of the id, use [getID]
     */
    override fun toString(): String = this.getID()

    enum class Type {
        NUMERICAL,
        STRING
    }
}

/**
 * Creates a numerical ID for this integer
 * @see Id.ofNumerical
 */
inline val Int.id: Id
    get() = Id.ofNumerical(this.coerceAtLeast(1).toUInt())


/**
 * Creates a numerical ID for this unsigned integer
 * @see Id.ofNumerical
 */
inline val UInt.id: Id
    get() = Id.ofNumerical(this)

/**
 * Creates a string ID for this string
 * @see Id.ofString
 */
inline val String.id: Id
    get() = Id.ofString(this)
