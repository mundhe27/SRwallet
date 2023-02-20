package com.bsr.bsrcoin

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.trimmedLength
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bsr.bsrcoin.Noitification.FcmNotificationsSender
import com.bsr.bsrcoin.MysqlConst.Constants
import com.bsr.bsrcoin.databinding.ActivityPayforDepositBinding
import org.json.JSONObject

class PayforDeposit : AppCompatActivity() {
    private lateinit var binding : ActivityPayforDepositBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Renew Deposit"
        binding = ActivityPayforDepositBinding.inflate(layoutInflater,null,false)
        setContentView(binding.root)
        binding.depositsummary.depositIAmount.text = "₹ "+intent.getStringExtra("amount")
        binding.depositsummary.depositIDuration.text = intent.getStringExtra("duration")
        binding.depositsummary.depositIRoi.text = intent.getStringExtra("roi")+" %"
        binding.depositsummary.depositIUserId.text = intent.getStringExtra("userId")
        binding.depositsummary.depositIType.text = intent.getStringExtra("type")
        binding.depositsummary.rowExpiry.visibility = View.GONE

        binding.btnpay.setOnClickListener {
            val focusedView = this.currentFocus
            if(focusedView!=null){
                val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                im.hideSoftInputFromWindow(focusedView.windowToken,0)
            }
            val builder = AlertDialog.Builder(this@PayforDeposit)
            val view = layoutInflater.inflate(R.layout.view_verify_pwd,null,false)
            val password = view.findViewById<EditText>(R.id.verify_pwd).text
            builder.setView(view)
            builder.setTitle("Confirmation")
            builder.setMessage("The amount will be deducted from the NWallet. Enter your password below to confirm this transaction")
            builder.setPositiveButton("Pay",DialogInterface.OnClickListener{_,_->
                Log.e("password", "onCreate: ${view.findViewById<EditText>(R.id.verify_pwd).text}")
                if(password.trimmedLength()==0){
                    Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
                }
                else{
                    val progressDialog = ProgressDialog(this)
                    progressDialog.setMessage("Please Wait while we create your transaction")
                    progressDialog.setTitle("Processing...")
                    progressDialog.setCancelable(false)
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                    progressDialog.show()

                    val caller = intent.getStringExtra("caller")
                    Log.e("outside", "onCreate: outside the if  " )
                    if(caller=="create"){
                        Log.e("TAG", "onCreate: insde the if condition", )

                        val queue = Volley.newRequestQueue(this)
                        val request =
                            object : StringRequest(Request.Method.POST, Constants.url_create_deposit, { response ->
                                if(JSONObject(response).getString("message").equals("Deposit was created.")){
                                    FcmNotificationsSender(SharedPrefmanager.getInstance(this).keyToken,"Deposit Alert",
                                    "Your application for the ${intent.getStringExtra("type")} deposit of amount ₹${intent.getStringExtra("amount")} for " +
                                            "${intent.getStringExtra("duration")} has been successfully created. Tap to view the description",this).SendNotifications()
                                    }
                                progressDialog.dismiss()
                                val builder = AlertDialog.Builder(this)
                                builder.setMessage(JSONObject(response).getString("message"))
                                builder.setTitle("Alert")
                                builder.setPositiveButton("Okay",DialogInterface.OnClickListener { dialogInterface, i ->
                                    val intent  = Intent(this,MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finish()
                                })
                                builder.setNegativeButton("cancel",DialogInterface.OnClickListener { dialogInterface, i ->
                                    startActivity(Intent(this,MainActivity::class.java))
                                    finish()

                                })
                                builder.show()
                                Log.e("TAG", "onCreate: $response", )
                            }, { error -> Log.e("TAG", "case1: ${error.message}")
                                }) {
                                override fun getParams(): MutableMap<String, String> {
                                    Log.e("TAG", "getParams: Inside the get Params", )
                                    val sharedpref = SharedPrefmanager.getInstance(this@PayforDeposit)
                                    val hashMap = HashMap<String,String>()
                                    hashMap.put("email",sharedpref.keyEmail)
                                    hashMap.put("pwd",password.toString().trim())
                                    hashMap.put("userId",sharedpref.keyId)
                                    hashMap.put("deposit_amount",intent.getStringExtra("amount")!!)
                                    hashMap.put("deposit_type",intent.getStringExtra("type")!!)
                                    hashMap.put("rate_of_Interest",intent.getStringExtra("roi")!!)
                                    hashMap.put("duration",intent.getStringExtra("duration")!!)
                                    Log.e("TAG", "getParams: hello from the getparams", )
                                    Log.e("TAG", "getParams: $hashMap", )
                                    return hashMap
                                }
                            }
                        queue.add(request)
                    }
                    else{
                        Log.e("TAG", "onCreate: insde the if condition", )

                        val queue = Volley.newRequestQueue(this)
                        val request =
                            object : StringRequest(Request.Method.POST, Constants.url_renew_deposit, { response ->
                                if(JSONObject(response).getString("message").equals("Deposit was created.")){
                                    FcmNotificationsSender(SharedPrefmanager.getInstance(this).keyToken,"Deposit Alert",
                                        "Your application for the renewal of ${intent.getStringExtra("type")} deposit of amount ₹${intent.getStringExtra("amount")} for " +
                                                "${intent.getStringExtra("duration")}. Tap to view the description",this).SendNotifications()
                                }

                                progressDialog.dismiss()
                                val builder = AlertDialog.Builder(this)
                                builder.setMessage(JSONObject(response).getString("message"))
                                builder.setTitle("Alert")
                                builder.setPositiveButton("Okay",DialogInterface.OnClickListener { dialogInterface, i ->
                                    val intent  = Intent(this,MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finish()
                                })
                                builder.setNegativeButton("cancel",DialogInterface.OnClickListener { dialogInterface, i ->
                                    startActivity(Intent(this,MainActivity::class.java))
                                    finish()

                                })
                                builder.show()
                                Log.e("TAG", "onCreate: $response", )
                            }, { error -> Log.e("TAG", "case1: ${error.message}")
                            }) {
                                override fun getParams(): MutableMap<String, String> {
                                    Log.e("TAG", "getParams: Inside the get Params", )
                                    val sharedpref = SharedPrefmanager.getInstance(this@PayforDeposit)
                                    val hashMap = HashMap<String,String>()
                                    Log.e("TAG", "getParams: ${intent.getStringExtra("id").toString()}")
                                    hashMap.put("email",sharedpref.keyEmail)
                                    hashMap.put("pwd",password.toString().trim())
                                    hashMap.put("userId",sharedpref.keyId)
                                    hashMap.put("id",intent.getStringExtra("id").toString())
                                    hashMap.put("deposit_amount",intent.getStringExtra("amount")!!)
                                    hashMap.put("expiry",intent.getStringExtra("expiry")!!)
                                    hashMap.put("rate_of_Interest",intent.getStringExtra("roi")!!)
                                    hashMap.put("duration",intent.getStringExtra("duration")!!)
                                    Log.e("TAG", "getParams: hello from the getparams")

                                    return hashMap
                                }
                            }
                        queue.add(request)
                    }
                }
            })
            builder.setNegativeButton("Cancel",DialogInterface.OnClickListener{_,_->

            })
            builder.show()
        }
    }
}