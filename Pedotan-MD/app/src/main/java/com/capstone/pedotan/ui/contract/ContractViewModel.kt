package com.capstone.pedotan.ui.contract

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.capstone.pedotan.data.SettingsRepository
import com.capstone.pedotan.databinding.ItemFieldBinding
import com.capstone.pedotan.databinding.ItemRowContractBinding
import com.capstone.pedotan.model.Contract
import com.capstone.pedotan.ui.camera.CameraActivity

class ContractViewModel(private val repository: SettingsRepository) : ViewModel() {
    val listContract: List<Contract> =
        repository.getContracts()
}

class ListContractAdapter(private val context: Context): RecyclerView.Adapter<ListContractAdapter.ListViewHolder>() {
    private val listContract = mutableListOf<Contract>()

    fun setList(contract: List<Contract>) {
        val oldSize = listContract.size
        listContract.clear()
        listContract.addAll(contract)
        val newSize = listContract.size
        notifyItemRangeRemoved(0, oldSize)
        notifyItemRangeInserted(0, newSize)
    }

    inner class ListViewHolder(private var binding: ItemRowContractBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contract: Contract) {
            binding.apply {
                tvInvestorName.text = contract.investor
                tvCompany.text = contract.company
                tvLoan.text = "Rp ${contract.loan}"

                Glide.with(context)
                    .load(contract.imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivProfile)

                binding.rightArrow.setOnClickListener {
                    openWhatsApp("6281228352023")
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding =
            ItemRowContractBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listContract[position])
    }

    override fun getItemCount(): Int = listContract.size

    private fun openWhatsApp(num: String) {
        val isAppInstalled = appInstalledOrNot("com.whatsapp")
        if (isAppInstalled) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=$num"))
            context.startActivity(intent)
        } else {
            // WhatsApp not installed show toast or dialog
            showToast("install aplikasi whatsapp terlebih dahulu")
        }
    }

    private fun appInstalledOrNot(uri: String): Boolean {
        val pm = context.packageManager
        return try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun showToast(message: String) {
        val toast: Toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.show()
    }
}

