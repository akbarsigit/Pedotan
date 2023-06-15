package com.capstone.pedotan.ui.loan

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.capstone.pedotan.data.SettingsRepository
import com.capstone.pedotan.databinding.ItemRowLoanBinding
import com.capstone.pedotan.model.Loan
import com.capstone.pedotan.ui.loan.ListLoanAdapter

class LoanViewModel(private val repository: SettingsRepository) : ViewModel() {
    val listLoan: List<Loan> =
        repository.getLoans()
}

class ListLoanAdapter(private val context: Context): RecyclerView.Adapter<ListLoanAdapter.ListViewHolder>() {
    private val listLoan = mutableListOf<Loan>()

    fun setList(loan: List<Loan>) {
        val oldSize = listLoan.size
        listLoan.clear()
        listLoan.addAll(loan)
        val newSize = listLoan.size
        notifyItemRangeRemoved(0, oldSize)
        notifyItemRangeInserted(0, newSize)
    }

    inner class ListViewHolder(private var binding: ItemRowLoanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(loan: Loan) {
            binding.apply {
                tvLoanAmount.text = loan.loan.toString()
                tvCompany.text = loan.company
                tvName.text = loan.investor
                tvDueDate.text = loan.due

                binding.rightArrow.setOnClickListener {
                    showToast("membuka detail pinjaman")
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding =
            ItemRowLoanBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listLoan[position])
    }

    override fun getItemCount(): Int = listLoan.size

    fun showToast(message: String) {
        val toast: Toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.show()
    }
}
