package com.core.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.core.clean.HistoryItemHeader
import com.core.clean.ItemType
import com.core.clean.SectionItem

abstract class BaseSectionedBindAdapter<HEADER : Any, BODY : SectionItem, H : ViewBinding, B : ViewBinding> :
        ListAdapter<SectionItem, BaseSectionedBindAdapter.SectionHolder<ViewBinding>>(DiffUtilsItemCallBack()) {

    abstract fun createHeaderBinding(inflater: LayoutInflater, parent: ViewGroup): H

    abstract fun createBodyBinding(inflater: LayoutInflater, parent: ViewGroup): B

    abstract fun onBindHeader(binding: H, item: HEADER, position: Int)

    abstract fun onBindBody(binding: B, item: BODY, position: Int)

    open class SectionHolder<V : ViewBinding>(val binding: V) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionHolder<ViewBinding> {
        val binding = when (viewType) {
            0 -> createHeaderBinding(LayoutInflater.from(parent.context), parent)
            else -> createBodyBinding(LayoutInflater.from(parent.context), parent)
        }
        return SectionHolder(binding)
    }

    override fun onBindViewHolder(holder: SectionHolder<ViewBinding>, position: Int) {
        when (currentList[position].itemType()) {
            ItemType.HeaderType -> onBindHeader(holder.binding as H, (currentList[position] as HistoryItemHeader<HEADER>).data, position)
            ItemType.BodyType -> onBindBody(holder.binding as B, currentList[position] as BODY, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position].itemType()) {
            ItemType.HeaderType -> 0
            ItemType.BodyType -> 1
        }
    }


    open fun <T, K> submitData(list: List<T>, keySelector: (T) -> K) {
        val groupBy = list.groupBy(keySelector)
        val sectionedData = mutableListOf<SectionItem>()
        groupBy.forEach { (t, u) ->
            sectionedData.add(HistoryItemHeader(t))
            sectionedData.addAll(u as List<BODY>)
        }
        submitList(sectionedData)
    }
}