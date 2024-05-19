package com.example.assignment2___

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(
    private val items: List<String>,
    private val onEdit: (String) -> Unit,
    private val onDelete: (String) -> Unit
) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textView: TextView = view.findViewById(R.id.text_view)
        var editButton: TextView = view.findViewById(R.id.edit_button)
        var deleteButton: TextView = view.findViewById(R.id.delete_button)

        init {
            editButton.setOnClickListener { onEdit(items[adapterPosition]) }
            deleteButton.setOnClickListener { onDelete(items[adapterPosition]) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = items[position]
    }

    override fun getItemCount() = items.size
}
