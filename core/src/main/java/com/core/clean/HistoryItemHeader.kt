package com.core.clean

data class HistoryItemHeader<K>(val data: K) : SectionItem() {
    override fun itemType(): ItemType {
        return ItemType.HeaderType
    }
}
