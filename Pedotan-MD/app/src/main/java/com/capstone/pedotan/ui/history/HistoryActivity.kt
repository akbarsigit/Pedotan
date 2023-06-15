package com.capstone.pedotan.ui.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.pedotan.databinding.ActivityHistoryBinding
import com.capstone.pedotan.ui.ViewModelFactory

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var viewModel: HistoryViewModel
    private val adapter: ListHistoryAdapter by lazy { ListHistoryAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[HistoryViewModel::class.java]

        binding.apply {
            rvFieldHistory.layoutManager = LinearLayoutManager(this@HistoryActivity)
            rvFieldHistory.setHasFixedSize(true)
            rvFieldHistory.adapter = adapter
        }

        val id = intent.getIntExtra(EXTRA_FIELD_ID, 0)
        viewModel.setListHistoriesById(id)
        adapter.setList(viewModel.listHistory)
    }

    private fun setupAction() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val EXTRA_FIELD_ID = "field_id"
    }
}