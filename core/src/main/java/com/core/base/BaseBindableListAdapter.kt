package com.core.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.core.utils.onClick
import java.lang.reflect.ParameterizedType

abstract class BaseBindableListAdapter<D : Any, B : ViewBinding>(diffCallback: DiffUtil.ItemCallback<D> = DiffUtilsItemCallBack())
    : ListAdapter<D, BaseBindableViewHolderWithoutViewHolder<B>>(diffCallback) {

    private var itemClickBody: D.(Int) -> Unit = {}

    abstract fun onBind(binding: B, item: D, position: Int)

    private fun createBinding(inflater: LayoutInflater, parent: ViewGroup): B {
        val method = createBinding().getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
        return method.invoke(null, inflater, parent, false) as B
    }

    open fun createBinding(): Class<B> {
        return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<B>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindableViewHolderWithoutViewHolder<B> {
        val binding = createBinding(LayoutInflater.from(parent.context), parent)
        val viewHolder = BaseBindableViewHolderWithoutViewHolder(binding)
        binding.root.onClick {
            val adapterPosition = viewHolder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onItemClick(currentList[adapterPosition], adapterPosition)
                itemClickBody(currentList[adapterPosition], adapterPosition)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: BaseBindableViewHolderWithoutViewHolder<B>, position: Int) {
        if (!currentList.isNullOrEmpty())
            onBind(holder.binding, currentList[position], position)
    }

    open fun onItemClick(item: D, position: Int) {

    }

    fun onItemClick(body: D.(Int) -> Unit) {
        itemClickBody = body
    }

}

class BaseBindableViewHolderWithoutViewHolder<B : ViewBinding>(val binding: B) : RecyclerView.ViewHolder(binding.root)