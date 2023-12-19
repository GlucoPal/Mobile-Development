package com.dicoding.glucopal.ui.scan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.glucopal.MainActivity
import com.dicoding.glucopal.data.response.CategoryResponse
import com.dicoding.glucopal.databinding.ActivityCategoryBinding
import com.dicoding.glucopal.ui.ViewModelFactory

class CategoryActivity : AppCompatActivity() {
    private lateinit var categoryBinding: ActivityCategoryBinding
    private lateinit var viewModel: CategoryViewModel
    private lateinit var adapter: DataAdapter
    private lateinit var searchView: SearchView
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryBinding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(categoryBinding.root)

        supportActionBar?.hide()

        userId = intent.getStringExtra("USER_ID")
        Log.d("Anin - CategoryActivity", "Received USER_ID: $userId")

        viewModel = obtainViewModel(this@CategoryActivity)

        val layoutManager = GridLayoutManager(this, 3)
        categoryBinding.rvCategory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        categoryBinding.rvCategory.removeItemDecoration(itemDecoration)

        adapter = DataAdapter(userId!!)
        categoryBinding.rvCategory.adapter = adapter

        viewModel.getCategory()

        viewModel.categoryResponse.observe(this) { categoryResponse ->
            if (categoryResponse != null) {
                Log.d("Anin - CategoryActivity", "Response not null")
                if (categoryResponse.success == 1) {
                    Log.d("Anin - CategoryActivity", "Success")
                    setData(categoryResponse)
                } else {
                    Log.d("Anin - CategoryActivity", "Error Data")
                }
            } else {
                Log.d("Anin - CategoryActivity", "Response Null")
            }
        }

        searchView = categoryBinding.searchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

        categoryBinding.closeButton.setOnClickListener{
            navigateToHome()
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): CategoryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[CategoryViewModel::class.java]
    }

    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = viewModel.categoryResponse.value?.data
                ?.filter { it!!.food!!.contains(query, ignoreCase = true) }
            adapter.submitList(filteredList.orEmpty())
        }
    }

    private fun setData(users: CategoryResponse?) {
        val itemList = users?.data
        adapter.setUserId(userId!!)
        adapter.submitList(itemList.orEmpty().filterNotNull())
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}