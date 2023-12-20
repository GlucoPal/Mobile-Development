package com.dicoding.glucopal.ui.history

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.glucopal.R
import com.dicoding.glucopal.data.response.HistoryItem
import com.dicoding.glucopal.databinding.FragmentHistoryBinding
import com.dicoding.glucopal.ui.ViewModelFactory

class HistoryFragment : Fragment() {
    private lateinit var searchView: SearchView
    private lateinit var adapter: HistoryAdapter
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val historyViewModel: HistoryViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }
    private var historyItemToDelete: HistoryItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvHistory.removeItemDecoration(itemDecoration)

        adapter = HistoryAdapter { historyItem ->
            deleteHistoryItem(historyItem)
        }
        binding.rvHistory.adapter = adapter

        historyViewModel.deletedItemPosition.observe(viewLifecycleOwner) { position ->
            adapter.notifyItemChanged(position)
        }

        historyViewModel.getSession().observe(viewLifecycleOwner) { loginResult ->
            val userId = loginResult?.userId

            if (userId != null) {
                historyViewModel.getHistory(userId)
            } else {
                Log.d("Anin - CategoryActivity", "User ID is null")
            }
        }

        historyViewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        historyViewModel.historyResponse.observe(viewLifecycleOwner) { historyResponse ->
            if (historyResponse != null) {
                if (historyResponse.success == 1) {
                    Log.d("Anin - CategoryActivity", "Success")
                    setData(historyResponse.data)
                } else {
                    Log.d("Anin - CategoryActivity", "Error Data")
                }

                val isEmpty = historyResponse.data.isNullOrEmpty()
                binding.tvEmptyHistory.visibility = if (isEmpty) View.VISIBLE else View.GONE

            } else {
                Log.d("Anin - CategoryActivity", "Response Null")
            }
        }

        searchView = binding.searchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val historyItem = adapter.currentList[position]

                historyItemToDelete = historyItem

                showCustomDialog(position)
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.rvHistory)
    }

    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = historyViewModel.historyResponse.value?.data
                ?.filter { it!!.foodName!!.contains(query, ignoreCase = true) }
            adapter.submitList(filteredList.orEmpty())
        }
    }

    private fun setData(historyItems: List<HistoryItem?>?) {
        adapter.submitList(historyItems)
    }

    private fun deleteHistoryItem(historyItem: HistoryItem) {
        val resultId = historyItem.id
        historyViewModel.deleteHistoryItem(resultId!!)
    }

    private fun showCustomDialog(position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.warning_dialog, null)

        val okButton = dialogView.findViewById<Button>(R.id.ok_btn_id)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancel_btn_id)
        val messageTextView = dialogView.findViewById<TextView>(R.id.message)

        messageTextView.text = getString(R.string.delete_message)

        builder.setView(dialogView)
        val dialog = builder.create()

        dialog.setCanceledOnTouchOutside(true)

        dialog.setOnCancelListener {
            adapter.notifyItemChanged(position)
            dialog.dismiss()
        }

        okButton.setOnClickListener {
            deleteHistoryItem(historyItemToDelete!!)
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            adapter.notifyItemChanged(position)
            dialog.dismiss()
        }

        dialog.show()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}