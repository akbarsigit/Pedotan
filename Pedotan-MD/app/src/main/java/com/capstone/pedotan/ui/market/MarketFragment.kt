package com.capstone.pedotan.ui.market

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.pedotan.databinding.FragmentMarketBinding
import com.capstone.pedotan.ui.ViewModelFactory

class MarketFragment : Fragment() {

    private lateinit var viewModel: MarketViewModel
    private var _binding: FragmentMarketBinding? = null
    private val binding get() = _binding!!
    private val adapter: ListMarketAdapter by lazy { ListMarketAdapter(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMarketBinding.inflate(inflater, container, false)
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
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(requireActivity())
        )[MarketViewModel::class.java]
    }

    private fun setupAction() {
        binding.apply {
            rvMarket.layoutManager = LinearLayoutManager(requireActivity())
            rvMarket.setHasFixedSize(true)
            rvMarket.adapter = adapter
        }

        adapter.setList(viewModel.listMarket)
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
    }
}