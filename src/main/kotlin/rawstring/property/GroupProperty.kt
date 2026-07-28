package fr.geming400.gddotkt.rawstring.property

import fr.geming400.gddotkt.rawstring.Id
import java.util.SequencedSet

class GroupProperty(
    id: Id,
    val groupParentsProperty: SequencedSetProperty<UInt>,
    collectionCtor: CollectionCtor<SequencedSet<UInt>> = { linkedSetOf() },
    defaultValue: SequencedSet<UInt>? = collectionCtor(),
    currentValue: SequencedSet<UInt>? = null
) : SequencedSetProperty<UInt>(id, defaultValue =  defaultValue, currentValue = currentValue) {
    override var value: SequencedSet<UInt>?
        get() {
            val linkedSet = linkedSetOf<UInt>()

            val originalValue = super.value
            if (originalValue != null)
                linkedSet.addAll(originalValue)

            val groupParents = this.groupParentsProperty.value
            if (groupParents != null)
                linkedSet.addAll(groupParents)

            return linkedSet
        }
        set(value) {
            super.value = value
        }
}