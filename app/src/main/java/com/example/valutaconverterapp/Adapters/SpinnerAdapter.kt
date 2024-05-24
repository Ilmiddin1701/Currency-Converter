package com.example.valutaconverterapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.valutaconverterapp.Models.Spinner
import com.example.valutaconverterapp.R

class SpinnerAdapter(private var list: ArrayList<Spinner>) : BaseAdapter() {
    override fun getCount(): Int = list.size

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemView: View
        if (convertView == null) {
            itemView = LayoutInflater.from(parent?.context).inflate(R.layout.spinner_item, parent, false)
        }else{
            itemView = convertView
        }

        itemView.findViewById<TextView>(R.id.text1).text = list[position].Ccy

        return itemView
    }
}