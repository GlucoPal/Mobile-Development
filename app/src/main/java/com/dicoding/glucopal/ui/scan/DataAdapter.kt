package com.dicoding.glucopal.ui.scan

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.glucopal.data.response.DataItem
import com.dicoding.glucopal.databinding.ItemCategoryBinding

class DataAdapter : ListAdapter<DataItem, DataAdapter.MyViewHolder>(DIFF_CALLBACK), Filterable {

    var originalList: List<DataItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        val itemList = getItem(position)
        viewHolder.bind(itemList)
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(viewHolder.itemView.context, ScanResultActivity::class.java)
            intent.putExtra("CATEGORY_ID", itemList.id)
            viewHolder.itemView.context.startActivity(intent)
        }
    }

    class MyViewHolder(private val binding : ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: DataItem){
            Glide.with(binding.root.context)
                .load(review.photo.toString())
                .into(binding.photoFood)
            binding.tvFood.text = "${review.food}"
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = ArrayList<DataItem>()
                if (constraint.isNullOrBlank()) {
                    filteredList.addAll(originalList)
                } else {
                    val filterPattern = constraint.toString().trim()
                    for (item in originalList) {
                        if (item.food!!.contains(filterPattern, ignoreCase = true)) {
                            filteredList.add(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                submitList(results?.values as List<DataItem>)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}