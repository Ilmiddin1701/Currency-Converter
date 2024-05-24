package com.example.valutaconverterapp.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.valutaconverterapp.Models.Courses
import com.example.valutaconverterapp.databinding.ItemRvBinding

class RvAdapter(var list: ArrayList<Courses>): Adapter<RvAdapter.Vh>() {

    inner class Vh(var itemRvBinding: ItemRvBinding): ViewHolder(itemRvBinding.root){
        @SuppressLint("SetTextI18n")
        fun onBind(courses: Courses, position: Int){
            itemRvBinding.currencyName.text = courses.CcyNm_UZ
            itemRvBinding.currencyAmount1.text = "${courses.Nominal} ${courses.Ccy}"
            itemRvBinding.currencyDate.text = courses.Date
            itemRvBinding.currencyAmount2.text = "${courses.Rate} so'm"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }
}