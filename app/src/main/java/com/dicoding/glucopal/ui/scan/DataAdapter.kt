package com.dicoding.glucopal.ui.scan

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.glucopal.data.response.DataItem
import com.dicoding.glucopal.databinding.ItemCategoryBinding
import java.util.Locale

class DataAdapter(private val userId: String) : ListAdapter<DataItem, DataAdapter.MyViewHolder>(DIFF_CALLBACK){
    private var filteredList: List<DataItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, userId)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        val itemList = getItem(position)
        viewHolder.bind(itemList)
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(viewHolder.itemView.context, UploadActivity::class.java)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("CATEGORY_ID", itemList.id)
            intent.putExtra("FOOD_NAME", itemList.food)
            intent.putExtra("IMAGE", itemList.photo)
            intent.putExtra("GI", itemList.gI)
            viewHolder.itemView.context.startActivity(intent)
        }
    }

    class MyViewHolder(private val binding : ItemCategoryBinding, private val userId: String) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: DataItem){
            Glide.with(binding.root.context)
                .load(review.photo.toString())
                .into(binding.photoFood)
            binding.tvFood.text = "${review.food}"
        }
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val queryString = constraint?.toString()?.lowercase(Locale.getDefault()) ?: ""
                filteredList = if (queryString.isEmpty()) {
                    currentList
                } else {
                    currentList.filter { it.food!!.lowercase(Locale.getDefault()).contains(queryString) }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as? List<DataItem> ?: emptyList()
                notifyDataSetChanged()
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