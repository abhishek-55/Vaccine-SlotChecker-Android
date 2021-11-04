package com.abhishek.vaccineslotchecker

data class Sessions (
    val centreId : Int,
    val name : String,
    val pin : Int,
    val stateName : String,
    val districtName : String,
    val address : String,
    val blockName : String,
    val fee : Int,
    val vaccineName : String,
    val SlotsAvailable : Int,
    val feeType : String,
    val Dose1 : Int,
    val Dose2 : Int
)