package com.capstone.pedotan.ui.contract

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.pedotan.R
import com.capstone.pedotan.databinding.FragmentContractBinding
import com.capstone.pedotan.databinding.FragmentLoanBinding
import com.capstone.pedotan.ui.ViewModelFactory
import com.capstone.pedotan.ui.loan.LoanViewModel
import com.capstone.pedotan.ui.market.ListMarketAdapter

class ContractFragment : Fragment() {

    private lateinit var viewModel: ContractViewModel
    private var _binding: FragmentContractBinding? = null
    private val binding get() = _binding!!
    private val adapter: ListContractAdapter by lazy { ListContractAdapter(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContractBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        setupViewModel()
        setupAction()
        onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(requireActivity()))[ContractViewModel::class.java]
    }

    private fun setupAction() {
        binding.apply {
            rvContract.layoutManager = LinearLayoutManager(requireActivity())
            rvContract.setHasFixedSize(true)
            rvContract.adapter = adapter
        }

        adapter.setList(viewModel.listContract)
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }

}