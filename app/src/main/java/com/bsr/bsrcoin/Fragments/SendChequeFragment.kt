package com.bsr.bsrcoin.Fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bsr.bsrcoin.MysqlConst.Constants
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.SharedPrefmanager
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Method
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.HashMap

class SendChequeFragment : Fragment() {
    private lateinit var datePickerDialog: DatePickerDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_cheque, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val date_text = view.findViewById<TextView>(R.id.cheque_date)
        val account_text = view.findViewById<EditText>(R.id.cheque_account)
        val currency_spin = view.findViewById<Spinner>(R.id.cheque_currency)
        val amount_text = view.findViewById<EditText>(R.id.cheque_amount)
        val s_wallet_spin = view.findViewById<Spinner>(R.id.sender_wallet)
        val r_wallet_spin = view.findViewById<Spinner>(R.id.reciever_wallet)
        val send_cheque = view.findViewById<AppCompatButton>(R.id.send_Cheque)

        //Currency and Wallet Spinners data
        val currency_a = arrayOf("INR","BSR")
        val wallet_a = arrayOf("Wallets","Nwallet","Mwallet")
        val wallet = arrayOf("","wallet1","wallet5")

        currency_spin.adapter = ArrayAdapter((activity as Context), android.R.layout.simple_spinner_dropdown_item, currency_a)
        s_wallet_spin.adapter = ArrayAdapter((activity as Context), android.R.layout.simple_spinner_dropdown_item, wallet_a)
        r_wallet_spin.adapter = ArrayAdapter((activity as Context), android.R.layout.simple_spinner_dropdown_item, wallet_a)

        //Date Spinner
        initDatePicker(date_text)
        date_text.setOnClickListener { datePickerDialog.show() }

        send_cheque.setOnClickListener {
            val account = account_text.text.toString()
            val amount = amount_text.text.toString()
            val currency = currency_a[currency_spin.selectedItemPosition]
            val wallet_send = wallet[s_wallet_spin.selectedItemPosition]
            val wallet_recieve = wallet[r_wallet_spin.selectedItemPosition]
            val date = date_text.text.toString()
            val currDate = getCurrentDate()
            val sdf = SimpleDateFormat("yyy-mm-dd")

            if(account == "")
                account_text.error = "Please Enter Account Number"
            else if (amount == "")
                amount_text.error = "Please Enter Amount"
            else if(amount.toInt() <= 0)
                amount_text.error = "Please Enter Valid Amount"
            else if(s_wallet_spin.selectedItemPosition == 0)
                Toast.makeText(requireContext(),"Select Sender Wallet", Toast.LENGTH_LONG).show()
            else if(r_wallet_spin.selectedItemPosition == 0)
                Toast.makeText(requireContext(),"Select Receiver Wallet", Toast.LENGTH_LONG).show()
            else if(date_text.text == "YYYY-MM-DD")
                Toast.makeText(requireContext(),"Enter Valid Date", Toast.LENGTH_LONG).show()
            else{
                val d : Date = sdf.parse(date)
                val d1 = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                val d3 : Date = sdf.parse(currDate)
                val d2 = d3.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                Log.d("days", ChronoUnit.DAYS.between(d1,d2).toString())
                if(d1.isBefore(d2))
                    Toast.makeText(requireContext(),"Enter Valid Date", Toast.LENGTH_LONG).show()
                /*else if(ChronoUnit.DAYS.between(d1,d2) <= 2)
                    Toast.makeText(requireContext(),"Enter date at least 3 Days after from now ", Toast.LENGTH_LONG).show()
                */else {
                    val progressDialog = ProgressDialog(requireContext())
                    progressDialog.setCancelable(false)
                    progressDialog.setMessage("Validating Account Number")
                    progressDialog.show()

                    Volley.newRequestQueue(requireContext()).add(
                        object : StringRequest(
                            Method.POST,
                            Constants.url_getid,
                            { s ->
                                try {
                                    val obj = JSONObject(s).getString("userId").toInt()
                                    if(obj > 0) {
                                        progressDialog.setMessage("Sending Cheque")
                                        Volley.newRequestQueue(requireContext()).add(
                                            object : StringRequest(
                                                Method.POST,
                                                Constants.url_send_cheque,
                                                {response->
                                                    progressDialog.dismiss()
                                                    try{
                                                        val obj1 = JSONObject(response)
                                                        if(obj1.getString("message") == "Cheque Sent") {
                                                            account_text.setText("")
                                                            amount_text.setText("")
                                                            s_wallet_spin.setSelection(0)
                                                            r_wallet_spin.setSelection(0)
                                                            currency_spin.setSelection(0)
                                                            date_text.setText(R.string.yyyy_mm_dd)
                                                        }
                                                        Toast.makeText(requireContext(),obj1.getString("message"), Toast.LENGTH_LONG).show()
                                                    } catch (e: JSONException){ e.stackTrace }
                                                },
                                                {e ->
                                                    e.stackTrace
                                                    progressDialog.dismiss()
                                                }
                                            ) {
                                                override fun getParams(): MutableMap<String, String> {
                                                    val params = HashMap<String, String>()
                                                    params["from_userId"] = SharedPrefmanager.getInstance(requireContext().applicationContext).keyId
                                                    params["to_userId"] = obj.toString()
                                                    params["amount"] = amount
                                                    params["currency"] = currency
                                                    params["from_wallet"] = wallet_send
                                                    params["to_wallet"] = wallet_recieve
                                                    params["date"] = date
                                                    return params
                                                }
                                            }
                                        )
                                    } else {
                                        account_text.error = " Invalid User Id "
                                        progressDialog.dismiss()
                                    }

                                } catch (e: JSONException) {
                                    e.stackTrace
                                }
                            },
                            { e ->
                                progressDialog.dismiss()
                            }
                        ) {
                            override fun getParams(): MutableMap<String, String> {
                                val params = HashMap<String, String>()
                                params["account"] = account
                                return params
                            }
                        }
                    )
                }
            }
        }

    }

    fun initDatePicker(dateText : TextView) {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val Date = "$year-${month+1}-$day"
            dateText.text = Date
        }
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val style = AlertDialog.THEME_HOLO_LIGHT
        datePickerDialog = DatePickerDialog(requireContext(),style,dateSetListener,year,month,day)
    }

    fun getCurrentDate() : String {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)

        return "$year-$month-$day"
    }
}