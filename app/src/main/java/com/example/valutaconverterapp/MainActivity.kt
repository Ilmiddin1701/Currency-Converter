package com.example.valutaconverterapp

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.valutaconverterapp.Adapters.RvAdapter
import com.example.valutaconverterapp.Adapters.SpinnerAdapter
import com.example.valutaconverterapp.Broadcast.MyReceiver
import com.example.valutaconverterapp.Models.Courses
import com.example.valutaconverterapp.Models.Spinner
import com.example.valutaconverterapp.Utils.MyData
import com.example.valutaconverterapp.Utils.Status
import com.example.valutaconverterapp.ViewModel.MyViewModel
import com.example.valutaconverterapp.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var rvAdapter: RvAdapter
    private lateinit var myViewModel: MyViewModel
    private var ccy = ""
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]

        val myReceiver = MyReceiver()

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        this.registerReceiver(myReceiver, filter)

        onResume()
        MyData.internetLiveData.observe(this) { isConnected ->
            if (!isConnected) {
                binding.tvNoInternet.visibility = View.VISIBLE
            } else {
                binding.tvNoInternet.visibility = View.GONE
            }
        }
    }

    private fun showKeyboard(view: View) {
        view.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onResume() {
        super.onResume()
        myViewModel.getCourses(this).observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    binding.shimmerLayout.visibility = View.VISIBLE
                    MyData.internetLiveData.observe(this) { isConnected ->
                        if (!isConnected) {
                            binding.tvNoInternet.visibility = View.VISIBLE
                        } else {
                            binding.tvNoInternet.visibility = View.GONE
                        }
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    binding.shimmerLayout.visibility = View.GONE
                    binding.rv.visibility = View.VISIBLE
                    rvAdapter = RvAdapter(it.data!! as ArrayList<Courses>)
                    binding.rv.adapter = rvAdapter

                    val list = it.data

                    val spinnerList = ArrayList<Spinner>()
                    var from = 0f
                    var to = 0f
                    list!!.forEach { spinners ->
                        spinnerList.add(Spinner(spinners.Ccy))
                    }
                    binding.apply {
                        spinnerFrom.adapter = SpinnerAdapter(spinnerList)
                        spinnerFrom.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                @SuppressLint("SetTextI18n")
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    tvDate.text = list[position].Date
                                    tvRN.text = "1 ${list[position].Ccy} = ${list[position].Rate} so'm"
                                    from = list[position].Rate.toFloat()
                                    tvName.text = "${list[position].CcyNm_UZ} (${list[position].Ccy})"
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                            }
                        spinnerTo.adapter = SpinnerAdapter(spinnerList)
                        spinnerTo.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                @SuppressLint("SetTextI18n")
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    tvDate1.text = list[position].Date
                                    tvRN1.text = "1 ${list[position].Ccy} = ${list[position].Rate} so'm"
                                    to = list[position].Rate.toFloat()
                                    ccy = list[position].Ccy
                                    tvName1.text = "${list[position].CcyNm_UZ} (${list[position].Ccy})"
                                }
                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                            }
                        btnConvert.setOnClickListener {
                            try {
                                if (edtAmount.text.isNotEmpty()) {
                                    val c = from / to
                                    val result = (c * edtAmount.text.toString().toFloat())
                                    tvResult.text = "%.3f".format(result)
                                    tvResult.append("  $ccy")
                                } else {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Enter the amount!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e:Exception) {
                                Toast.makeText(this@MainActivity, "Nimadir xato ketdi!", Toast.LENGTH_SHORT).show()
                            }
                        }
                        edtAmount.addTextChangedListener { tvResult.text = "" }
                        show.setOnClickListener {
                            showKeyboard(edtAmount)
                        }
                    }
                }
            }
        }
    }
}