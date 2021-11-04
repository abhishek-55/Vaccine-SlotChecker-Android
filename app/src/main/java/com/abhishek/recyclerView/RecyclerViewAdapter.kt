package com.abhishek.vaccineslotchecker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abhishek.vaccineslotchecker.databinding.RecyclerviewLayoutBinding

class RecyclerViewAdapter: RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    private var sessionsList = mutableListOf<Sessions>()

    class MyViewHolder(val  binding: RecyclerviewLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecyclerviewLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.binding.name.text = sessionsList[position].name + ", " + sessionsList[position].pin
        val address = sessionsList[position].address + ", "+ sessionsList[position].stateName + ", "+ sessionsList[position].districtName
        holder.binding.address.text = "Address : $address"
        val blockName = sessionsList[position].blockName
        holder.binding.blockName.text = "Block Name : $blockName"
        val fee = sessionsList[position].fee
        holder.binding.fee.text = "Fee : $fee"
        val vaccineName = sessionsList[position].vaccineName
        holder.binding.vaccineName.text = "Vaccine Name : $vaccineName"
        val slotsAvailable  = sessionsList[position].SlotsAvailable
        holder.binding.SlotsAvailable.text = "Slots Available : $slotsAvailable"
        holder.binding.feeType.text = sessionsList[position].feeType
        val dose1 = sessionsList[position].Dose1
        holder.binding.Dose1.text = "Dose-1 : $dose1"
        val dose2 = sessionsList[position].Dose2
        holder.binding.Dose2.text = "Dose-2 : $dose2"

    }

    override fun getItemCount(): Int {
        return sessionsList.size
    }

    fun setData(newSessionsList: MutableList<Sessions>){
        val diffUtil = MyDiffUtil(sessionsList, newSessionsList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        sessionsList = newSessionsList
        diffResult.dispatchUpdatesTo(this)
    }

}