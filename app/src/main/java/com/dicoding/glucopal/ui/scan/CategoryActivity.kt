package com.dicoding.glucopal.ui.scan

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.glucopal.data.response.CategoryResponse
import com.dicoding.glucopal.data.response.DataItem
import com.dicoding.glucopal.databinding.ActivityCategoryBinding
import com.dicoding.glucopal.ui.ViewModelFactory

class CategoryActivity : AppCompatActivity() {
    private lateinit var categoryBinding: ActivityCategoryBinding
    private lateinit var viewModel: CategoryViewModel
    private lateinit var adapter: DataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryBinding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(categoryBinding.root)

        viewModel = obtainViewModel(this@CategoryActivity)

        val layoutManager = GridLayoutManager(this, 2)
        categoryBinding.rvCategory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        categoryBinding.rvCategory.removeItemDecoration(itemDecoration)

        adapter = DataAdapter()
        categoryBinding.rvCategory.adapter = adapter

        viewModel.getCategory()

        viewModel.categoryResponse.observe(this) { categoryResponse ->
            if (categoryResponse != null) {
                Log.d("Anin - CategoryActivity", "Response not null")
                if (categoryResponse.success == 1) {
                    Log.d("Anin - CategoryActivity", "Success")
                    adapter.submitList(categoryResponse.data)
                    // adapter.originalList = categoryResponse.data as List<DataItem>
                } else {
                    Log.d("Anin - CategoryActivity", "Error Data")
                }
            } else {
                Log.d("Anin - CategoryActivity", "Response Null")
            }
        }

//        categoryBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                adapter.filter.filter(newText)
//                return true
//            }
//        })
    }

    private fun obtainViewModel(activity: AppCompatActivity): CategoryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[CategoryViewModel::class.java]
    }
}