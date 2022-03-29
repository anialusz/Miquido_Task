package com.example.miquido.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.miquido.R
import com.example.miquido.model.ToDoItem

class ToDoListAdapter(
    private val dataSet: List<ToDoItem>,
    private val onItemClicked: (ToDoItem) -> Unit,
    private val onLongItemClicked: (ToDoItem) -> Boolean
) :
    RecyclerView.Adapter<ToDoListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)

        return ItemViewHolder(view, { onItemClicked(dataSet[it]) },
            { onLongItemClicked(dataSet[it]) })
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.setToDoItem(dataSet[position])
        holder.setImageBackground(dataSet[position].icon)
    }

    override fun getItemCount(): Int = dataSet.size

    class ItemViewHolder(
        itemView: View,
        onItemClicked: (Int) -> Unit,
        onLongItemClicked: (Int) -> Boolean
    ) :
        RecyclerView.ViewHolder(itemView) {
        private val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvDes: TextView = itemView.findViewById(R.id.tvDescription)
        private val tvTimeStamp: TextView = itemView.findViewById(R.id.tvTimeStamp)

        init {
            itemView.setOnClickListener {
                onItemClicked(bindingAdapterPosition)
            }
            itemView.setOnLongClickListener {
                onLongItemClicked(bindingAdapterPosition)
            }
        }

        fun setToDoItem(toDoItem: ToDoItem) {
            tvTitle.text = toDoItem.title
            tvDes.text = toDoItem.description
            tvTimeStamp.text = toDoItem.timestamp
        }

        fun setImageBackground(url: String) {
            Glide.with(itemView.context).load(url).placeholder(R.drawable.ic_image_placeholder)
                .into(ivIcon)
        }
    }
}