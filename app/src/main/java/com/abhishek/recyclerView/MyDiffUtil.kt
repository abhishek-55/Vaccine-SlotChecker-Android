package com.abhishek.vaccineslotchecker

import androidx.recyclerview.widget.DiffUtil

class MyDiffUtil(
    private val oldList: List<Sessions>,
    private val newList: List<Sessions>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return  oldList[oldItemPosition].centreId == newList[newItemPosition].centreId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return  when{
            oldList[oldItemPosition].centreId != newList[newItemPosition].centreId -> {
                false
            }
            oldList[oldItemPosition].name != newList[newItemPosition].name -> {
                false
            }
            oldList[oldItemPosition].stateName != newList[newItemPosition].stateName -> {
                false
            }
            oldList[oldItemPosition].districtName != newList[newItemPosition].districtName -> {
                false
            }
            oldList[oldItemPosition].address != newList[newItemPosition].address -> {
                false
            }
            oldList[oldItemPosition].blockName != newList[newItemPosition].blockName -> {
                false
            }
            oldList[oldItemPosition].fee != newList[newItemPosition].fee -> {
                false
            }
            oldList[oldItemPosition].vaccineName != newList[newItemPosition].vaccineName -> {
                false
            }
            oldList[oldItemPosition].SlotsAvailable != newList[newItemPosition].SlotsAvailable -> {
                false
            }
            oldList[oldItemPosition].feeType != newList[newItemPosition].feeType -> {
                false
            }
            oldList[oldItemPosition].Dose1 != newList[newItemPosition].Dose1 -> {
                false
            }
            oldList[oldItemPosition].Dose2 != newList[newItemPosition].Dose2 -> {
                false
            }
            else -> true
        }
    }
}