package com.capstone.pedotan.ui.loan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.pedotan.databinding.FragmentLoanBinding
import com.capstone.pedotan.ui.ViewModelFactory
import com.capstone.pedotan.ui.contract.ListContractAdapter


class LoanFragment : Fragment() {

    private lateinit var viewModel: LoanViewModel
    private var _binding: FragmentLoanBinding? = null
    private val binding get() = _binding!!
    private val adapter: ListLoanAdapter by lazy { ListLoanAdapter(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoanBinding.inflate(inflater, container, false)
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
        viewModel = ViewModelProvider(this, ViewModelFactory(requireActivity()))[LoanViewModel::class.java]
    }

    private fun setupAction() {
        binding.apply {
            rvLoan.layoutManager = LinearLayoutManager(requireActivity())
            rvLoan.setHasFixedSize(true)
            rvLoan.adapter = adapter
        }

        adapter.setList(viewModel.listLoan)
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }
}