package com.abhishek.vaccineslotchecker

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhishek.vaccineslotchecker.databinding.ActivityMainBinding
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private lateinit var binding : ActivityMainBinding
    private val myAdapter = RecyclerViewAdapter()
    private var districtsList : MutableList<String> = mutableListOf()
    private var districts : MutableMap<String, Int> = mutableMapOf()
    private val loadingDialog = LoadingDialog(this)
    private val noSessionDialog = NoSessionDialog(this)


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Inflate the layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Set Date in Chips
        setDate()

        //Set RecyclerView Layout and Adapter
        binding.recyclerView.adapter = myAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        //Navigate to WebPage of Cowin portal
        binding.bookBtn.setOnClickListener {

            //Navigate to WebViewActivity using Intent
            startActivity(Intent(this, WebViewActivity::class.java))
        }

        //Check Sessions By Pin
        binding.btnPin.setOnClickListener {
            val pin = getEnteredPinCode()
            val date = getSelectedDate()
            findByPin(pin,date)
            hideKeyboard(it)
            }

        //Check Sessions By District
        binding.btnDistrict.setOnClickListener {
            val key = districts.get(binding.currentDistrict.text.toString())
            if (key != null){
                val distId = key.toString()
                val date = getSelectedDate()
                findByDistrict(distId, date)
            }else{
                Toast.makeText(this, "Select state first", Toast.LENGTH_SHORT).show()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDate() {
        val date = LocalDateTime.now()
        val myFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        //Set Dates in the chip
        binding.chip1.text = date.format(myFormat).toString()
        binding.chip2.text = (date.plusDays(1)).format(myFormat).toString()
        binding.chip3.text = (date.plusDays(2)).format(myFormat).toString()
        binding.chip4.text = (date.plusDays(3)).format(myFormat).toString()
        binding.chip5.text = (date.plusDays(4)).format(myFormat).toString()
        binding.chip6.text = (date.plusDays(5)).format(myFormat).toString()
        binding.chip7.text = (date.plusDays(6)).format(myFormat).toString()
    }

    //Set RecyclerView Data to show and change on when needed
    private fun setRecyclerViewData(newSessionsList: MutableList<Sessions>){
        myAdapter.setData(newSessionsList)
    }

    private fun getSelectedDate(): String {
        var date = binding.chip1.text.toString()
        when {
            binding.chip2.isChecked -> {
                date = binding.chip2.text.toString()
            }
            binding.chip3.isChecked -> {
                date = binding.chip3.text.toString()
            }
            binding.chip4.isChecked -> {
                date = binding.chip4.text.toString()
            }
            binding.chip5.isChecked -> {
                date = binding.chip5.text.toString()
            }
            binding.chip6.isChecked -> {
                date = binding.chip6.text.toString()
            }
            binding.chip7.isChecked -> {
                date = binding.chip7.text.toString()
            }
        }
        return date
    }

    private fun getEnteredPinCode(): String {
        var pinCode = binding.pin.text.toString()
        binding.pin.doOnTextChanged { text, _, _, _ ->
            if (text?.length == 6) {
                pinCode = text.toString()
            }
        }
        return pinCode
    }

    private fun sendApiRequest(url: String) {
        //A list to Store all the available sessions data in the list
        val sessionsList : MutableList<Sessions> = mutableListOf()

        //Start the loading Dialog
        loadingDialog.startLoadingDialog()

        // Request a JSON response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                val sessionResponse = response.getJSONArray("sessions")

                //Show No sessions Available dialog if there are no sessions available in the response array
                if (sessionResponse.length() == 0) {
                    noSessionDialog.startDialog()

                } else {

                    for (i in 0 until sessionResponse.length()) {

                        val eachSession = sessionResponse.getJSONObject(i)

                        val centerId = eachSession.getInt("center_id")
                        val name = eachSession.getString("name")
                        val address = eachSession.getString("address")
                        val stateName = eachSession.getString("state_name")
                        val districtName = eachSession.getString("district_name")
                        val blockName = eachSession.getString("block_name")
                        val pincode = eachSession.getInt("pincode")
                        val feeType = eachSession.getString("fee_type")
                        val fee = eachSession.getInt("fee")
                        val vaccine = eachSession.getString("vaccine")
                        val slotsAvailable = eachSession.getInt("available_capacity")
                        val dose1 = eachSession.getInt("available_capacity_dose1")
                        val dose2 = eachSession.getInt("available_capacity_dose2")
                        val sessionDetails = Sessions(centerId, name, pincode, stateName, districtName, address, blockName, fee, vaccine, slotsAvailable, feeType, dose1, dose2)
                        sessionsList.add(sessionDetails)
                    }
                }
                //To feed data to the recycler View to show to the user
                setRecyclerViewData(sessionsList)
                //Dismiss the loading dialog
                loadingDialog.dismissDialog()
            },
            { _ ->
                //Dismiss the loading dialog
                loadingDialog.dismissDialog()
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        )
        // Add the request to the RequestQueue.
        MySingletonClass.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }


    private fun findByPin(pinCode: String, date: String) {
        if (pinCode.length == 6){
            val url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode=$pinCode&date=$date"
            sendApiRequest(url)
        }else{
            Toast.makeText(this, "Invalid PinCode", Toast.LENGTH_LONG).show()
        }

    }

    private fun findByDistrict(districtId : String, date: String) {
        val url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByDistrict?district_id=$districtId&date=$date"
        sendApiRequest(url)
    }

    override fun onResume() {
        //Set the States from the resources to the state dropdown
        val states = resources.getStringArray(R.array.States)
        val statesArrayAdapter = ArrayAdapter(this, R.layout.dropdown_states, states)

        with(binding.currentState) {
            setAdapter(statesArrayAdapter)
            onItemClickListener = this@MainActivity
        }

        val districtsArrayAdapter = ArrayAdapter(this, R.layout.dropdown_districts, districtsList)
        binding.currentDistrict.setAdapter(districtsArrayAdapter)
        super.onResume()
    }

    //Show a exit confirmation dialog wheter the user wants to exit the app or not
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to exit ?")
            .setCancelable(true)
            .setPositiveButton("Yes") { _, _ ->
                this.finish()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
        val alert = builder.create()
        alert.show()
    }

    //Handle State option click so that we can show the district list to select
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        districtsList.clear()
        districts.clear()
        getDistricts((position + 1).toString())
    }

    //Get the list of the all districts of the selected state
    private fun getDistricts(stateId: String) {
        loadingDialog.startLoadingDialog()
        val url = "https://cdn-api.co-vin.in/api/v2/admin/location/districts/$stateId"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                val districtsResponse = response.getJSONArray("districts")

                for (i in 0 until districtsResponse.length()) {

                    val eachDistrict = districtsResponse.getJSONObject(i)
                    val districtId = eachDistrict.getInt("district_id")
                    val districtName = eachDistrict.getString("district_name")
                    districtsList.add(districtName)
                    districts[districtName] = districtId
                }
                loadingDialog.dismissDialog()
            }, { _ ->
                Toast.makeText(this, "Check internet connection and try again ", Toast.LENGTH_LONG).show()
                loadingDialog.dismissDialog()
            }
        )
        MySingletonClass.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    //Hide keyboard
    private fun Context.hideKeyboard(view: View) {
        val inputManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}