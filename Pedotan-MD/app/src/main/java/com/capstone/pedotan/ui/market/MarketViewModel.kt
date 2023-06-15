package com.capstone.pedotan.ui.market

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
import com.capstone.pedotan.data.SettingsRepository
import com.capstone.pedotan.databinding.ItemFieldBinding
import com.capstone.pedotan.databinding.ItemRowMarketBinding
import com.capstone.pedotan.model.Market
import com.capstone.pedotan.ui.camera.CameraActivity

class MarketViewModel(private val repository: SettingsRepository) : ViewModel() {
    val listMarket: List<Market> =
        repository.getMarkets()
}

class ListMarketAdapter(private val context: Context): RecyclerView.Adapter<ListMarketAdapter.ListViewHolder>() {
    private val listMarket = mutableListOf<Market>()

    fun setList(market: List<Market>) {
        val oldSize = listMarket.size
        listMarket.clear()
        listMarket.addAll(market)
        val newSize = listMarket.size
        notifyItemRangeRemoved(0, oldSize)
        notifyItemRangeInserted(0, newSize)
    }

    inner class ListViewHolder(private var binding: ItemRowMarketBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(market: Market) {
            binding.apply {
                tvCommodity.text = market.komoditas
                tvMarketPrice.text = market.harga.toString()
                tvName.text = market.petani
                tvPlace.text = market.lokasi

                binding.rightArrow.setOnClickListener {
                    showToast("add to cart")
                }
            }

            binding.rightArrow.setOnClickListener {
                showToast("menambahkan ke keranjang")
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding =
            ItemRowMarketBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listMarket[position])
    }

    override fun getItemCount(): Int = listMarket.size

    fun showToast(message: String) {
        val toast: Toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.show()
    }
}

