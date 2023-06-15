package com.capstone.pedotan.ui.market

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.pedotan.R
import com.capstone.pedotan.databinding.ActivityHistoryBinding
import com.capstone.pedotan.databinding.ActivityShopBinding
import com.capstone.pedotan.ui.history.HistoryViewModel
import com.capstone.pedotan.ui.history.ListHistoryAdapter

class ShopActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShopBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }
}