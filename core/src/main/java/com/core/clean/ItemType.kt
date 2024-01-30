package com.core.clean

sealed class ItemType {
    abstract val type: Int

    object HeaderType : ItemType() {
        override val type: Int = 0
    }

    object BodyType : ItemType() {
        override val type: Int = 1
    }
}
