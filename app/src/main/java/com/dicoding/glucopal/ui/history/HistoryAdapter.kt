package com.dicoding.glucopal.ui.history

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.glucopal.R
import com.dicoding.glucopal.data.response.HistoryItem
import com.dicoding.glucopal.databinding.ItemResultBinding

class HistoryAdapter : ListAdapter<HistoryItem, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        val historyItem = getItem(position)
        viewHolder.bind(historyItem)
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(viewHolder.itemView.context, DetailActivity::class.java)
            intent.putExtra("HISTORY_ID", historyItem.id)
            intent.putExtra("PHOTO", historyItem.photo)
            viewHolder.itemView.context.startActivity(intent)
        }
    }

    class MyViewHolder(private val binding: ItemResultBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(historyItem: HistoryItem) {
            Glide.with(binding.root.context)
                .load(historyItem.photo.toString())
                .into(binding.photoFood)

            binding.tvMerk.text = historyItem.foodName
            binding.tvResultGI.text = historyItem.gI.toString()

            val glValue: Float = historyItem.gL ?: 0.0F
            binding.tvResultGL.text = glValue.toString()

            when {
                glValue < 10 -> setClassificationColors("Low", R.color.class_low)
                glValue in 10.0..20.0 -> setClassificationColors("Medium", R.color.class_medium)
                glValue > 20 -> setClassificationColors("High", R.color.class_high)
                else -> binding.tvResultClassification.text = "Error"
            }
        }

        private fun setClassificationColors(classification: String, colorResId: Int) {
            binding.tvResultClassification.text = classification
            val color = ContextCompat.getColor(binding.root.context, colorResId)
            binding.imageView3.backgroundTintList = ColorStateList.valueOf(color)
            binding.constraintLayout.backgroundTintList = ColorStateList.valueOf(color)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryItem>() {
            override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}