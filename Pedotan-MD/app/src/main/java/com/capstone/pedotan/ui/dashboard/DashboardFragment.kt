package com.capstone.pedotan.ui.dashboard

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.capstone.pedotan.databinding.FragmentDashboardBinding
import com.capstone.pedotan.ui.ViewModelFactory
import com.capstone.pedotan.ui.camera.CameraActivity
import com.capstone.pedotan.ui.livecamera.LiveCameraActivity

class DashboardFragment : Fragment() {

    private lateinit var viewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
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
        viewModel = ViewModelProvider(this, ViewModelFactory(requireActivity()))[DashboardViewModel::class.java]
    }

    private fun setupAction() {
        binding.btnQuickScan.setOnClickListener {
            startActivity(Intent(requireActivity(), LiveCameraActivity::class.java))
        }

        binding.fabCamera.setOnClickListener {
            startActivity(Intent(requireActivity(), CameraActivity::class.java))
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