package com.capstone.pedotan.ui.dashboard

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.capstone.pedotan.databinding.FragmentDashboardBinding
import com.capstone.pedotan.ui.ViewModelFactory
import com.capstone.pedotan.ui.addfield.AddFieldActivity


class DashboardFragment : Fragment() {

    private lateinit var viewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val adapter: ListFieldAdapter by lazy { ListFieldAdapter(requireActivity()) }

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
        binding.apply {
            rvField.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvField.setHasFixedSize(true)
            rvField.adapter = adapter

            btnAddField.setOnClickListener {
                startActivity(Intent(requireActivity(), AddFieldActivity::class.java))
            }
        }

        Glide.with(this)
            .load("https://storage.googleapis.com/pedotan-image-resource/Group%2012(1).png")
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageTatacara)
        Glide.with(this)
            .load("https://storage.googleapis.com/pedotan-image-resource/Rectangle%203(1).png")
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.dashboardImage)

        viewModel.getSettings()
        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.profile.text = "Halo, ${user?.name ?: ""} " + String(Character.toChars(0x1F44B))
        }
        viewModel.listFields.observe(viewLifecycleOwner) {
            adapter.setList(it)
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