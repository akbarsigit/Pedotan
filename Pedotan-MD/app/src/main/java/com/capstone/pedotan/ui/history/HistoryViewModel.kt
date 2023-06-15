package com.capstone.pedotan.ui.history

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
import com.capstone.pedotan.databinding.ItemRowHistoryBinding
import com.capstone.pedotan.model.History
import com.capstone.pedotan.ui.camera.CameraActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HistoryViewModel(private val repository: SettingsRepository) : ViewModel() {
    private val _listHistory: MutableList<History> = mutableListOf()

    val listHistory: List<History> = _listHistory

    fun setListHistoriesById(id: Int) {
        _listHistory.clear()
        _listHistory.addAll(repository.getHistories(id))
    }
}

class ListHistoryAdapter(private val context: Context): RecyclerView.Adapter<ListHistoryAdapter.ListViewHolder>() {
    private val listHistory = mutableListOf<History>()

    fun setList(history: List<History>) {
        val oldSize = listHistory.size
        listHistory.clear()
        listHistory.addAll(history)
        val newSize = listHistory.size
        notifyItemRangeRemoved(0, oldSize)
        notifyItemRangeInserted(0, newSize)
    }

    inner class ListViewHolder(private var binding: ItemRowHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History) {
            binding.apply {
                tvTimestamp.text = history.timestamp
                tvNitrogen.text = history.nitrogen
                tvPhosphorous.text = history.phosphorous
                tvPotassium.text = history.potassium
                tvCurahHujan.text = history.curahhujan
                tvPh.text = history.ph
                tvKelembaban.text = history.kelembaban
                tvSuhu.text = history.suhu
                tvPenyakit.text = history.penyakit
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding =
            ItemRowHistoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listHistory[position])
    }

    override fun getItemCount(): Int = listHistory.size

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

