package com.dicoding.glucopal.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.glucopal.MainViewModel
import com.dicoding.glucopal.databinding.FragmentHomeBinding
import com.dicoding.glucopal.ui.ViewModelFactory
import com.dicoding.glucopal.ui.bloodsugar.BloodSugarActivity
import com.dicoding.glucopal.ui.glycemicindex.GycemicIndexActivity
import com.dicoding.glucopal.ui.glycemicload.GlycemicLoadActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewModel: MainViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = obtainViewModel(this@HomeFragment)

        val logoutButton = binding.logoutButton
        val giButton = binding.giButton
        val glButton = binding.glButton
        val bslButton = binding.bslButton

        logoutButton.setOnClickListener {
            viewModel.logout()
        }

        giButton.setOnClickListener {
            val intent = Intent(requireContext(), GycemicIndexActivity::class.java)
            startActivity(intent)
        }

        glButton.setOnClickListener {
            val intent = Intent(requireContext(), GlycemicLoadActivity::class.java)
            startActivity(intent)
        }

        bslButton.setOnClickListener {
            val intent = Intent(requireContext(), BloodSugarActivity::class.java)
            startActivity(intent)
        }
        return root
    }

    private fun obtainViewModel(fragment: Fragment): MainViewModel {
        val factory = ViewModelFactory.getInstance(fragment.requireActivity().application)
        return ViewModelProvider(fragment, factory).get(MainViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}