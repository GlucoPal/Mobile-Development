package com.dicoding.glucopal.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.glucopal.databinding.FragmentHomeBinding
import com.dicoding.glucopal.ui.bloodsugar.BloodSugarActivity
import com.dicoding.glucopal.ui.glycemicindex.GycemicIndexActivity
import com.dicoding.glucopal.ui.glycemicload.GlycemicLoadActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

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

        val giButton = binding.giButton
        val glButton = binding.glButton
        val bslButton = binding.bslButton

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}