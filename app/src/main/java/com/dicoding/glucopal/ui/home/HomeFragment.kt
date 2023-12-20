package com.dicoding.glucopal.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.glucopal.MainViewModel
import com.dicoding.glucopal.R
import com.dicoding.glucopal.databinding.FragmentHomeBinding
import com.dicoding.glucopal.ui.ViewModelFactory
import com.dicoding.glucopal.ui.bloodsugar.BloodSugarActivity
import com.dicoding.glucopal.ui.glucospike.GlucoSpikeActivity
import com.dicoding.glucopal.ui.glycemicindex.GycemicIndexActivity
import com.dicoding.glucopal.ui.glycemicload.GlycemicLoadActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewModel: MainViewModel

    private val binding get() = _binding!!

    @SuppressLint("StringFormatInvalid")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = obtainViewModel(this@HomeFragment)

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.token != null) {
                val username = user.username
                binding.userName.text = username
            }
        }

        val logoutButton = binding.logoutButton
        val giButton = binding.giButton
        val glButton = binding.glButton
        val bslButton = binding.bslButton
        val gsButton = binding.gsButton

        logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
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

        gsButton.setOnClickListener {
            val intent = Intent(requireContext(), GlucoSpikeActivity::class.java)
            startActivity(intent)
        }
        return root
    }

    private fun obtainViewModel(fragment: Fragment): MainViewModel {
        val factory = ViewModelFactory.getInstance(fragment.requireActivity().application)
        return ViewModelProvider(fragment, factory).get(MainViewModel::class.java)
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.warning_dialog, null)

        val okButton = dialogView.findViewById<Button>(R.id.ok_btn_id)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancel_btn_id)
        val messageTextView = dialogView.findViewById<TextView>(R.id.message)

        messageTextView.text = getString(R.string.logout_message)

        builder.setView(dialogView)
        val dialog = builder.create()

        dialog.setCanceledOnTouchOutside(true)

        okButton.setOnClickListener {
            viewModel.logout()
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
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