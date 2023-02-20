package com.bsr.bsrcoin.Adapter

import android.app.AlertDialog
import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat.*
import androidx.core.text.trimmedLength
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bsr.bsrcoin.MainActivity
import com.bsr.bsrcoin.MysqlConst.Constants
import com.bsr.bsrcoin.PayforDeposit
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.SharedPrefmanager
import com.bsr.bsrcoin.network.Deposit
import org.json.JSONObject
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

//same adapter is used by the ViewDepositFragment and the fund_share fragment as most of the functionalities were same

//if caller = 0 then the adapter is called by ViewDepositFragment or else from the fund_deposit fragment
class ViewDepositAdapter(private val alldeposit:ArrayList<Deposit>, val context:Context, val caller:Int): RecyclerView.Adapter<ViewDepositAdapter.DepositViewHolder>() {

    class DepositViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val depositId=view.findViewById<TextView>(R.id.deposit_Id)
        val type=view.findViewById<TextView>(R.id.deposit_type)
        val type2 = view.findViewById<TextView>(R.id.deposit_i_type)
        val relativelayout = view.findViewById<RelativeLayout>(R.id.reltypedeposit)
        val amount=view.findViewById<TextView>(R.id.deposit_i_amount)
        val roi=view.findViewById<TextView>(R.id.deposit_i_roi)
        val duration=view.findViewById<TextView>(R.id.deposit_i_duration)
        val expiry = view.findViewById<TextView>(R.id.deposit_expirydate)
        var deposit : Deposit? = null
        val renewRow = view.findViewById<TableRow>(R.id.renewDate)
        val renewdate = view.findViewById<TextView>(R.id.deposit_renewdate)
        val typerow = view.findViewById<TableRow>(R.id.deposit_row_type)
        val button = view.findViewById<TextView>(R.id.deposit_renew)
        val info = view.findViewById<TextView>(R.id.deposit_info)
        var context:Context? = null

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepositViewHolder {
        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.fund_view_deposit,parent,false)
        return DepositViewHolder(view)
    }


    override fun getItemCount(): Int {
        return alldeposit.size
    }

    override fun onBindViewHolder(holder: DepositViewHolder, position: Int) {
        if (alldeposit.isEmpty()) {
            Toast.makeText(context, "No Deposit Till Now", Toast.LENGTH_SHORT).show()
        }else {

                val amount = alldeposit[position].deposit_amount.toString()
                val depositId =  alldeposit[position].depositId.toString()
                holder.depositId.text = depositId
                holder.type.text = alldeposit[position].deposit_type
                holder.amount.text = amount
                holder.duration.text = alldeposit[position].duration
                holder.type2.text = alldeposit[position].deposit_type
                holder.roi.text = alldeposit[position].rate_of_Interest.toString()
                holder.expiry.text = alldeposit[position].expiry
                holder.deposit = alldeposit[position]
                if (alldeposit[position].renewedOn.length < 5) {
                    holder.renewRow.visibility = View.GONE
                } else {
                    holder.renewRow.visibility = View.VISIBLE
                    holder.renewdate.text = alldeposit[position].renewedOn
                }
                if (caller == 0) {
                    holder.button.text = "pay dues"
                    holder.button.setOnClickListener {
                        val builder = AlertDialog.Builder(context)
                        val view = LayoutInflater.from(context).inflate(R.layout.view_verify_pwd,null,false)
                        val password = view.findViewById<EditText>(R.id.verify_pwd).text
                        builder.setView(view)
                        builder.setTitle("Confirmation")
                        builder.setMessage("The amount will be deducted from the NWallet. Enter your password below to confirm this transaction")
                        builder.setPositiveButton("Pay",DialogInterface.OnClickListener{_,_->


                            if(password.trimmedLength()==0){
                                Toast.makeText(context, "Password is required", Toast.LENGTH_SHORT).show()
                            }
                            else{
                                val progress = ProgressDialog(context)
                                progress.setTitle("Processing...")
                                progress.setMessage("Please wait while we are processing your transaction")
                                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                                progress.show()

                                Log.e("password", "onCreate: ${view.findViewById<EditText>(R.id.verify_pwd).text}")
                                val queue = Volley.newRequestQueue(context)
                                val request = object:StringRequest(Request.Method.POST,Constants.url_pay_deposit_due,
                                    {response->
                                        progress.dismiss()
                                        val obj = JSONObject(response)
                                        Log.e("TAG", "onBindViewHolder: $obj", )
                                        val builder1 = AlertDialog.Builder(context)
                                        builder1.setTitle("Alert")
                                        builder1.setMessage(obj.getString("message"))
                                        builder1.setPositiveButton("Okay",DialogInterface.OnClickListener{_,_->
                                         

                                        })
                                        builder1.show()
                                    },{error->
                                        Toast.makeText(context, "Sry for the inconvineance ${error.message}", Toast.LENGTH_SHORT).show()
                                    }){
                                    override fun getParams(): MutableMap<String, String>? {

                                        val hash=HashMap<String,String>();
                                        val sharedPref = SharedPrefmanager.getInstance(context)
                                        hash.put("email",sharedPref.keyEmail)
                                        hash.put("userId",sharedPref.keyId)
                                        hash.put("pwd",password.toString())
                                        hash.put("deposit_amount",amount)
                                        hash.put("deposit_id",depositId)

                                        Log.e("tag","$hash")


                                        return hash
                                    }
                                }

                                queue.add(request)

                            }
                    })
                        builder.show()


                }
                }else {
                    holder.relativelayout.visibility = View.VISIBLE
                    holder.typerow.visibility = View.GONE
                    val deposittype = arrayOf(
                            "Select Deposit Type",
                    "Fixed",
                    "Recurring",
                    "CL-Deposit",
                    "LF-Deposit",
                    "Hf-Deposit"
                    )
                    var interest = 0
                    var amountlimit = 0
                    var durationavailable = arrayOf("Select Duration")
                    when (alldeposit[position].deposit_type) {
                        deposittype[1] -> {
                            durationavailable = arrayOf(
                                "Select Duration",
                                "1 year",
                                "2 year",
                                "3 year",
                                "4 year",
                                "5 year"
                            )
                            interest = 7
                            amountlimit = 25000
                        }
                        deposittype[2] -> {
                            durationavailable = arrayOf(
                                "Select Duration",
                                "6 month",
                                "12 month",
                                "18 month",
                                "24 month"
                            )
                            interest = 8
                            amountlimit = 3000
                        }
                        deposittype[3] -> {
                            durationavailable = arrayOf(
                                "Select Duration",
                                "10 year",
                                "11 year",
                                "12 year",
                                "13 year",
                                "14 year",
                                "15year"
                            )
                            interest = 12
                            amountlimit = 200000
                        }
                        deposittype[4] -> {
                            durationavailable =
                                arrayOf("Select Duration", "15 year", "20 year", "25 year")
                            interest = 13
                            amountlimit = 500000
                        }
                        deposittype[5] -> {
                            durationavailable = arrayOf("Select Duration", "Life Time")
                            interest = 14
                            amountlimit = 1500000
                        }
                    }


                    holder.button.setOnClickListener {

                        val builder = AlertDialog.Builder(context)
                        builder.setTitle("Renew Deposit")
                        val view = LayoutInflater.from(context)
                            .inflate(R.layout.view_renew_deposit, null, false)
                        view.findViewById<TextView>(R.id.deposit_type).text =
                            alldeposit[position].deposit_type
                        view.findViewById<TextView>(R.id.deposit_amount).text =
                            alldeposit[position].deposit_amount.toString()
                        view.findViewById<TextView>(R.id.deposit_roi).text = interest.toString()
                        view.findViewById<TextView>(R.id.deposit_user_id).text =
                            alldeposit[position].depositId.toString()
                        view.findViewById<Spinner>(R.id.deposit_duration).adapter = ArrayAdapter(
                            context,
                            R.layout.support_simple_spinner_dropdown_item,
                            durationavailable
                        )
                        var selectedduration = ""
                        view.findViewById<Spinner>(R.id.deposit_duration).onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    p0: AdapterView<*>?,
                                    p1: View?,
                                    p2: Int,
                                    p3: Long
                                ) {
                                    selectedduration = durationavailable[p2]
                                }

                                override fun onNothingSelected(p0: AdapterView<*>?) {

                                }

                            }

                        builder.setView(view)
                        builder.create()
                        builder.setPositiveButton("Pay", DialogInterface.OnClickListener { _, _ ->
                            if (selectedduration.equals("Select Duration")) {
                                Toast.makeText(context, "Please select a duration", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                val intent = Intent(context, PayforDeposit::class.java)
                                intent.putExtra("caller", "update")
                                intent.putExtra("type", alldeposit[position].deposit_type)
                                intent.putExtra(
                                    "amount",
                                    alldeposit[position].deposit_amount.toString()
                                )
                                intent.putExtra("roi", interest.toString())
                                intent.putExtra("userId", alldeposit[position].userId.toString())
                                intent.putExtra("duration", selectedduration)
                                intent.putExtra("expiry", alldeposit[position].expiry)
                                Log.e(
                                    "checkcheckcheck",
                                    "onBindViewHolder: ${alldeposit[position].depositId}",
                                )
                                intent.putExtra("id", alldeposit[position].depositId)
                                startActivity(context, intent, null)
                            }
                        })
                        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->

                        })
                        builder.show()

                    }
                }




            }



    }

}