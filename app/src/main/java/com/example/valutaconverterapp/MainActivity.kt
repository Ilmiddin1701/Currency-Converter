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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        requestQueue = Volley.newRequestQueue(this)
        val url = "https://cbu.uz/uzc/arkhiv-kursov-valyut/json/"
        val jsonArrayRequest =
            JsonArrayRequest(Request.Method.GET, url, null, object : Response.Listener<JSONArray> {
                override fun onResponse(response: JSONArray?) {
                    val str = response.toString()
                    val type = object : TypeToken<ArrayList<Courses>>() {}.type
                    list = Gson().fromJson(str, type)
                    rvAdapter = RvAdapter(list)
                    binding.rv.adapter = rvAdapter

                    val spinnerList = ArrayList<Spinner>()
                    list.forEach {
                        spinnerList.add(Spinner(it.Ccy))
                    }
                    binding.apply {
                        spinnerFrom.adapter = SpinnerAdapter(spinnerList)
                        spinnerFrom.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            @SuppressLint("SetTextI18n")
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                tvRN.text = "1 ${list[position].Ccy} = ${list[position].Rate} so'm"
                                tvDate.text = list[position].Date
                                tvName.text = "${list[position].CcyNm_UZ} (${list[position].Ccy})"
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        }
                    }
                }
            }, Response.ErrorListener {
                Toast.makeText(this, "Server xatoligi", Toast.LENGTH_SHORT).show()
            })
        requestQueue.add(jsonArrayRequest)
    }
}