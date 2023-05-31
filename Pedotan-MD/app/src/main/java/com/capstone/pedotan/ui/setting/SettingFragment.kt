package com.capstone.pedotan.ui.setting

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.capstone.pedotan.R
import com.capstone.pedotan.databinding.FragmentSettingBinding
import com.capstone.pedotan.ui.ViewModelFactory
import com.capstone.pedotan.ui.login.LoginActivity

class SettingFragment : Fragment() {

    private lateinit var viewModel: SettingViewModel
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupAction()
        onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(requireActivity()))[SettingViewModel::class.java]
    }

    private fun setupAction() {
        val settings = viewModel.getCurrentSettings()
        when (settings.isDarkMode) {
            0 -> {
                binding.tvTheme.text = "On"
            }
            1 -> {
                binding.tvTheme.text = "Off"
            }
            2 -> {
                binding.tvTheme.text = "Default"
            }
        }

        binding.linearLayoutTheme.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.change_theme))
            val styles = arrayOf("On","Off","System default")
            val checkedItem = settings.isDarkMode

            builder.setSingleChoiceItems(styles, checkedItem) { dialog, which ->
                when (which) {
                    0 -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        viewModel.setDarkMode(0)
                        dialog.dismiss()
                    }
                    1 -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        viewModel.setDarkMode(1)
                        dialog.dismiss()
                    }
                    2 -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        viewModel.setDarkMode(2)
                        dialog.dismiss()
                    }
                }
            }
            val dialog = builder.create()
            dialog.show()
        }

        binding.linearLayoutLogout.setOnClickListener {
            AlertDialog.Builder(requireActivity()).apply {
                setTitle("Logout")
                setMessage("Apakah Anda Yakin Ingin Keluar?")
                setPositiveButton("Ya") { _, _ ->
                    viewModel.removeSession()
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    requireActivity().finish()
                }
                create()
                show()
            }
        }
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }
}