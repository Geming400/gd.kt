package editor.rawstring.property

import editor.rawstring.Id
import editor.rawstring.serializing.Serializer
import java.util.*

/**
 * @see MutableList
 */
open class ListProperty<T>(
    id: Id,
    collectionCtor: CollectionCtor<MutableList<T>> = { arrayListOf() },
    defaultValue: MutableList<T>? = collectionCtor(),
    currentValue: MutableList<T>? = null,
    elemSerializer: Serializer<T>
) : AbstractCollectionProperty<T, MutableList<T>>(id, defaultValue, currentValue, elemSerializer) {
    override fun createEmptyCollection(): MutableList<T> = arrayListOf()

    /**
     * Returns the element at the specified index in the list
     * @throws IndexOutOfBoundsException if [index] is less than zero or greater than or equal to [Collection.size] of this list
     * @see List.get
     */
    operator fun get(index: Int): T = this.getOrCreateCollection()[index]

    /**
     * Replaces the element at the specified position in this list with the specified element.
     * @return the element previously at the specified position.
     * @throws IndexOutOfBoundsException if [index] is less than zero or greater than or equal to [Collection.size] of this list.
     * @see MutableList.set
     */
    operator fun set(index: Int, element: T) {
        this.getOrCreateCollection()[index] = element
    }

    override fun asRawString(): String =
        this.toRawIterableStringHelper()
}

/**
 * @see MutableSet
 */
open class SetProperty<T>(
    id: Id,
    collectionCtor: CollectionCtor<MutableSet<T>> = { mutableSetOf() },
    defaultValue: MutableSet<T>? = collectionCtor(),
    currentValue: MutableSet<T>? = null,
    elemSerializer: Serializer<T>
) : AbstractCollectionProperty<T, MutableSet<T>>(id, defaultValue, currentValue, elemSerializer) {
    override fun asRawString(): String =
        this.toRawIterableStringHelper()

    override fun createEmptyCollection(): MutableSet<T> = mutableSetOf()
}

/**
 * @see SequencedSet
 */
open class SequencedSetProperty<T>(
    id: Id,
    collectionCtor: CollectionCtor<SequencedSet<T>> = { linkedSetOf() },
    defaultValue: SequencedSet<T>? = collectionCtor(),
    currentValue: SequencedSet<T>? = null,
    elemSerializer: Serializer<T>
) : AbstractCollectionProperty<T, SequencedSet<T>>(id, defaultValue, currentValue, elemSerializer) {
    override fun asRawString(): String =
        this.toRawIterableStringHelper()

    override fun createEmptyCollection(): SequencedSet<T> = linkedSetOf()
}