package com.lavinou.startpoint.attribute

public class AttributeKey<T : Any>(public val name: String) {
    init {
        if (name.isEmpty()) {
            throw IllegalStateException("Name can't be blank")
        }
    }

    override fun toString(): String = "AttributeKey: $name"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as AttributeKey<*>

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}