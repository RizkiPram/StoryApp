package com.example.mystory2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystory2.databinding.ItemStoryBinding
import com.example.mystory2.response.ListStoryItem

class StoryAdapter:
    PagingDataAdapter<ListStoryItem, StoryAdapter.ListViewHolder>(DIFF_CALLBACK) {
    private var onItemClickCallback : OnItemClickCallback?=null
    inner class ListViewHolder(private var binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(storyItem: ListStoryItem) {

            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(storyItem)
            }
            with(binding){
                tvName.text=storyItem.name
                Glide.with(itemView)
                    .load(storyItem.photoUrl)
                    .into(storyImage)

            }

        }
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemStoryBinding =
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(itemStoryBinding)
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }
    interface OnItemClickCallback{
        fun onItemClicked(data:ListStoryItem )
    }
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }

    }

}