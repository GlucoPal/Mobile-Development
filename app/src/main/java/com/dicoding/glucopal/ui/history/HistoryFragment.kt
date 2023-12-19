package com.dicoding.glucopal.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.glucopal.data.response.HistoryItem
import com.dicoding.glucopal.databinding.FragmentHistoryBinding
import com.dicoding.glucopal.ui.ViewModelFactory

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val historyViewModel: HistoryViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

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

        historyViewModel.getSession().observe(viewLifecycleOwner) { loginResult ->
            val userId = loginResult?.userId

            if (userId != null) {
                historyViewModel.getHistory(userId)
            } else {
                Log.d("Anin - CategoryActivity", "User ID is null")
            }
        }

        historyViewModel.historyResponse.observe(viewLifecycleOwner) { historyResponse ->
            if (historyResponse != null) {
                if (historyResponse.success == 1) {
                    Log.d("Anin - CategoryActivity", "Success")
                    setData(historyResponse.data)
                } else {
                    Log.d("Anin - CategoryActivity", "Error Data")
                }
            } else {
                Log.d("Anin - CategoryActivity", "Response Null")
            }
        }
    }

    private fun setData(historyItems: List<HistoryItem?>?) {
        val adapter = HistoryAdapter()
        adapter.submitList(historyItems)
        binding.rvHistory.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}