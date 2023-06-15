package com.capstone.pedotan.ui.dashboard

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.capstone.pedotan.api.ApiConfig
import com.capstone.pedotan.data.SettingsRepository
import com.capstone.pedotan.databinding.ItemFieldBinding
import com.capstone.pedotan.model.response.FieldResponse
import com.capstone.pedotan.model.response.UserResponse
import com.capstone.pedotan.ui.camera.CameraActivity
import com.capstone.pedotan.ui.checkfield.CheckFieldActivity
import com.capstone.pedotan.ui.history.HistoryActivity
import com.google.android.play.integrity.internal.t
import com.senpro.ulamsae.model.Settings
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel(private val repository: SettingsRepository) : ViewModel() {
    private val _listFields = MutableLiveData<ArrayList<FieldResponse>>()
    val listFields: LiveData<ArrayList<FieldResponse>> get() = _listFields
    private val _user = MutableLiveData<UserResponse?>(null)
    val user : LiveData<UserResponse?> get() = _user

    fun getSettings() {
        val settings = repository.getSettings()
        setFieldsData(settings)
        setUserDetail(settings)
    }

    private fun setFieldsData(settings: Settings) {
        val client = ApiConfig().getApiService().getFieldsData("Bearer ${settings.token}", settings.email)
        client.enqueue(object : Callback<ArrayList<FieldResponse>> {
            override fun onResponse(
                call: Call<ArrayList<FieldResponse>>,
                response: Response<ArrayList<FieldResponse>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val arrayList = ArrayList<FieldResponse>()
                    responseBody?.let { list ->
                        val modifiedList = list.map { fieldResponse ->
                            fieldResponse.copy(id = 1) // Create a copy of each FieldResponse item with the id set to 1
                        }
                        arrayList.addAll(modifiedList)
                    }
                    _listFields.postValue(arrayList)
                } else {
                    Log.e("error", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<FieldResponse>>, t: Throwable) {
//                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setUserDetail(settings: Settings) {
        val client = ApiConfig().getApiService().getUserDetail("Bearer ${settings.token}", settings.email)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    _user.value = response.body()
                } else {
                    Log.e("error", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
//                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }
}

class ListFieldAdapter(private val context: Context) : RecyclerView.Adapter<ListFieldAdapter.ListViewHolder>() {
    private val listField = mutableListOf<FieldResponse>()

    fun setList(field: ArrayList<FieldResponse>) {
        val oldSize = listField.size
        listField.clear()
        listField.addAll(field)
        val newSize = listField.size
        notifyItemRangeRemoved(0, oldSize)
        notifyItemRangeInserted(0, newSize)
    }

    inner class ListViewHolder(private var binding: ItemFieldBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(field: FieldResponse) {
            binding.apply {
                val position = adapterPosition

                fieldName.text = "Kebun ${field.commodity} |"
                fieldLuas.text = "${field.area} Ha"
                fieldLocation.text = field.location
                fieldStatus.text = field.status
                if (field.status == "buruk") {
                    fieldStatus.setTextColor(Color.parseColor("#D80000"))
                } else if (field.status == "kurang baik")  {
                    fieldStatus.setTextColor(Color.parseColor("#F6BE00"))
                } else {
                    fieldStatus.setTextColor(Color.parseColor("#778D45"))
                }

                btnCekPenyakit.setOnClickListener {
                    val intent = Intent(context, CameraActivity::class.java)
                    // Add any extra data to the intent if needed
                    intent.putExtra("task", "check penyakit")
                    intent.putExtra(CameraActivity.EXTRA_FIELD_ID, listField[position].farm_id)
                    context.startActivity(intent)
                }

                btnCekKondisi.setOnClickListener {
                    val intent = Intent(context, CheckFieldActivity::class.java)
                    // Add any extra data to the intent if needed
                    intent.putExtra(CheckFieldActivity.EXTRA_FIELD_ID, listField[position].farm_id)
                    context.startActivity(intent)
                }

                btnCekRiwayat.setOnClickListener {
                    val intent = Intent(context, HistoryActivity::class.java)
                    // Add any extra data to the intent if needed
                    intent.putExtra(HistoryActivity.EXTRA_FIELD_ID, listField[position].id ?: 0)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding =
            ItemFieldBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listField[position])
    }

    override fun getItemCount(): Int = listField.size
}