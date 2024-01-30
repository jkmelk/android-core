package com.core.base

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.core.utils.inflate
import com.core.utils.onClick


abstract class BaseListAdapter<D : Any>(diffCallback: DiffUtil.ItemCallback<D> = DiffUtilsItemCallBack())
    : ListAdapter<D, BaseViewHolderWithoutViewHolder>(diffCallback) {

    abstract val layoutResId: Int

    abstract fun onBind(itemView: View, item: D, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderWithoutViewHolder {
        val itemView = parent.inflate(layoutResId)
        val viewHolder = BaseViewHolderWithoutViewHolder(itemView)
        itemView.onClick {
            val adapterPosition = viewHolder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onItemClick(currentList[adapterPosition], adapterPosition)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolderWithoutViewHolder, position: Int) {
        onBind(holder.itemView, currentList[position], position)
    }

    open fun onItemClick(item: D, position: Int) {

    }
}

class BaseViewHolderWithoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)