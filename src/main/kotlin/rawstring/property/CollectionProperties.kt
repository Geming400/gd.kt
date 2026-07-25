package fr.geming400.gddotkt.rawstring.property

open class ListProperty<T>(id: UInt, defaultValue: MutableList<T>? = arrayListOf(), currentValue: MutableList<T>? = defaultValue) : AbstractCollectionProperty<T, MutableList<T>>(id, defaultValue, currentValue) {
    /**
     * Returns the element at the specified index in the list
     * @throws NullPointerException if the collection [value] is null
     * @throws IndexOutOfBoundsException if [index] is less than zero or greater than or equal to [Collection.size] of this list
     * @see List.get
     */
    operator fun get(index: Int): T = this.getOrThrow()[index]

    /**
     * Replaces the element at the specified position in this list with the specified element.
     * @return the element previously at the specified position.
     * @throws NullPointerException if the collection [value] is null
     * @throws IndexOutOfBoundsException if [index] is less than zero or greater than or equal to [Collection.size] of this list.
     * @see MutableList.set
     */
    operator fun set(index: Int, element: T) {
        this.getOrThrow()[index] = element
    }

    override fun toRawString(): String =
        this.toRawIterableStringHelper {
            it.toString()
        }
}

open class SetProperty<T>(id: UInt, defaultValue: MutableSet<T>? = mutableSetOf(), currentValue: MutableSet<T>? = defaultValue) : AbstractCollectionProperty<T, MutableSet<T>>(id, defaultValue, currentValue) {
    override fun toRawString(): String =
        this.toRawIterableStringHelper {
            it.toString()
        }
}