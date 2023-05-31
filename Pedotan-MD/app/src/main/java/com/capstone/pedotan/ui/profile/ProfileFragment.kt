package com.capstone.pedotan.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.capstone.pedotan.databinding.FragmentProfileBinding
import com.capstone.pedotan.ui.ViewModelFactory

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
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
        viewModel = ViewModelProvider(this, ViewModelFactory(requireActivity()))[ProfileViewModel::class.java]
    }

    private fun setupAction() {
        viewModel.getSettings()

        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.apply {
                tvUsername.text = user?.username
                tvEmail.text = user?.fullname
                Glide.with(requireActivity())
                    .load("https://picsum.photos/512")
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(profileImage)
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