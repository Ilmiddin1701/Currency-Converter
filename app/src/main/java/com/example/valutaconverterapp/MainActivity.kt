package com.example.valutaconverterapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.valutaconverterapp.Adapters.RvAdapter
import com.example.valutaconverterapp.Adapters.SpinnerAdapter
import com.example.valutaconverterapp.Models.Courses
import com.example.valutaconverterapp.Models.Spinner
import com.example.valutaconverterapp.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var rvAdapter: RvAdapter
    private lateinit var list: ArrayList<Courses>
    private lateinit var requestQueue: RequestQueue
    var p = 1
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        requestQueue = Volley.newRequestQueue(this)
        val url = "https://cbu.uz/uzc/arkhiv-kursov-valyut/json/"
        val jsonArrayRequest =
            JsonArrayRequest(Request.Method.GET, url, null, { response ->
                val str = response.toString()
                val type = object : TypeToken<ArrayList<Courses>>() {}.type
                list = Gson().fromJson(str, type)
                list.add(Courses(0, "Salom", "UZS", "UZS", "UZS", "UZS", "UZS", "1000", "1000", "1", list[1].Date))
                rvAdapter = RvAdapter(list)
                binding.rv.adapter = rvAdapter
                val spinnerList = ArrayList<Spinner>()
                var from = 0f
                var to = 0f
                list.forEach {
                    spinnerList.add(Spinner(it.Ccy))
                }
                binding.apply {
                    spinnerFrom.adapter = SpinnerAdapter(spinnerList)
                    spinnerFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        @SuppressLint("SetTextI18n")
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (list[position].id != 0){
                                tvDate.text = list[position].Date
                                tvRN.text = "1 ${list[position].Ccy} = ${list[position].Rate} so'm"
                                from = list[position].Rate.toFloat()
                                tvName.text = "${list[position].CcyNm_UZ} (${list[position].Ccy})"
                            } else {
                                p = list[position].id
                                tvName.text = "${list[position].CcyNm_UZ} (${list[position].Ccy})"
                            }
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                    spinnerTo.adapter = SpinnerAdapter(spinnerList)
                    spinnerTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        @SuppressLint("SetTextI18n")
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            tvDate1.text = list[position].Date
                            tvRN1.text = "1 ${list[position].Ccy} = ${list[position].Rate} so'm"
                            to = list[position].Rate.toFloat()
                            tvName1.text = "${list[position].CcyNm_UZ} (${list[position].Ccy})"
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                    btnConvert.setOnClickListener {
                        if (edtAmount.text.isNotEmpty()) {
                            if (p != 0){
                                val c = from / to
                                val result = (c * edtAmount.text.toString().toFloat())
                                tvResult.text = "%.3f".format(result)
                            }
                        } else {
                            Toast.makeText(this@MainActivity, "Enter the amount!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }) {
                Toast.makeText(this, "No Internet connection. Please try again!", Toast.LENGTH_SHORT).show()
            }
        requestQueue.add(jsonArrayRequest)
    }
}