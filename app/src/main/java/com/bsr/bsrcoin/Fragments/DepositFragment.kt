package com.bsr.bsrcoin.Fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bsr.bsrcoin.PayforDeposit
import com.google.android.material.snackbar.Snackbar
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.SharedPrefmanager
import com.bsr.bsrcoin.network.Deposit
import com.bsr.bsrcoin.network.RetrofitBondAndDepositInstance
import com.bsr.bsrcoin.network.retrofitDepositService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DepositFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var etdepositAmount: EditText
    lateinit var dropdown_menu_deposittype: Spinner
    lateinit var dropdown_menu_depositduration: Spinner
    lateinit var Rate_of_Interest:TextView
    private lateinit var btnApply: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_deposit, container, false)

        etdepositAmount = view.findViewById(R.id.etdepositAmount)
        dropdown_menu_deposittype = view.findViewById(R.id.dropdown_menu_deposittype)
        dropdown_menu_depositduration = view.findViewById(R.id.dropdown_menu_depositduration)
        btnApply = view.findViewById(R.id.btndepositApply)
        Rate_of_Interest=view.findViewById(R.id.deposit_rate_of_interest)

        val deposittype = arrayOf("Select Deposit Type","Fixed", "Recurring", "CL-Deposit", "LF-Deposit","Hf-Deposit")
        var selectedDepoitType: String = deposittype[0]
        var durationavailable= arrayOf("Select Duration")
        dropdown_menu_deposittype.adapter = ArrayAdapter<String>(
            activity as Context,
            R.layout.support_simple_spinner_dropdown_item,
            deposittype
        )
        var amountlimit=0
        var interest=0
        dropdown_menu_deposittype.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedDepoitType=deposittype[p2]
                    when(selectedDepoitType){
                        deposittype[1]->{
                            durationavailable= arrayOf("Select Duration","1 year","2 year","3 year","4 year","5 year")
                            interest=7
                            amountlimit=25000
                            }
                        deposittype[2]->{
                            durationavailable= arrayOf("Select Duration","6 month","12 month","18 month","24 month")
                            interest=8
                            amountlimit=3000
                            etdepositAmount.hint="Enter Amount( Minimum Rs.3000 every month)"
                        }
                        deposittype[3]->{durationavailable= arrayOf("Select Duration","10 year","11 year","12 year","13 year","14 year","15year")
                            interest=12
                            amountlimit=200000
                             }
                        deposittype[4]->{
                            durationavailable= arrayOf("Select Duration","15 year","20 year","25 year")
                            interest=13
                            amountlimit=500000
                        }
                        deposittype[5]-> {
                            durationavailable= arrayOf("Select Duration","Life Time")
                            interest=14
                            amountlimit=1500000
                            }
                    }
                    dropdown_menu_depositduration.adapter = ArrayAdapter<String>(
                        activity as Context,
                        R.layout.support_simple_spinner_dropdown_item,
                        durationavailable
                    )
                    if(selectedDepoitType!=deposittype[0]){
                        Rate_of_Interest.text="Rate of Interest is ${interest}%"
                        if (selectedDepoitType!=deposittype[2]){
                            etdepositAmount.hint="Enter Amount(Above ${amountlimit})"
                        }
                    }

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        var selectedduration=durationavailable[0]

        dropdown_menu_depositduration.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedduration=durationavailable[p2]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }

        val user_id= SharedPrefmanager.getInstance(requireContext().applicationContext).keyId.toInt()

        btnApply.setOnClickListener {
            val amt = etdepositAmount.text.toString().trim { it <= ' ' }
            when {
                (selectedDepoitType==deposittype[0])->{
                    displayErrorSnackbar("Select Deposit Type")
                }
                (selectedduration==durationavailable[0])->{
                    displayErrorSnackbar("Select Duration")
                }
                TextUtils.isEmpty(amt) -> {
                    displayErrorSnackbar("Please Enter Amount")
                }
                amt.toInt()<amountlimit-> {
                    displayErrorSnackbar("Minimum Range of Amount is $amountlimit")
                }
                else->{
                    val intent = Intent(context,PayforDeposit::class.java)
                    intent.putExtra("caller","create")
                    intent.putExtra("type",selectedDepoitType)
                    intent.putExtra("amount",amt)
                    intent.putExtra("roi",interest.toString())
                    intent.putExtra("userId",SharedPrefmanager.getInstance(context).keyId)
                    intent.putExtra("duration",selectedduration)
                    startActivity(intent)


                }
            }
        }

        // Inflate the layout for this fragment
        return view
    }



    fun displayErrorSnackbar(text: String) {
        val snackbar = Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            text,
            Snackbar.LENGTH_LONG
        )
        val view = snackbar.view
        view.setBackgroundColor(ContextCompat.getColor(activity as Context, R.color.error))
        snackbar.show()

    }
}