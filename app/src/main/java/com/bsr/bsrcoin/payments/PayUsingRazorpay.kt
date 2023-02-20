package com.bsr.bsrcoin.payments

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bsr.bsrcoin.MainActivity
import com.bsr.bsrcoin.MysqlConst.Constants
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.SharedPrefmanager
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.lang.Exception
import java.lang.reflect.Method

class PayUsingRazorpay : AppCompatActivity(), PaymentResultListener {
    private var name: String = ""
    private var amt : String = ""
    private var type : String = ""
    private var order_id : String =""
    private var wallet : String =""
    private var curr : String =""
    private var coin : String =""
    var filePath: String = ""
    lateinit var view: View

    val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if(result.isSuccessful) {
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(this)
            view.findViewById<ImageView>(R.id.pay_ss).setImageDrawable(Drawable.createFromStream(uriContent?.let {
                this.contentResolver.openInputStream(
                    it
                )
            }, null))
            if (uriFilePath != null) {
                filePath = uriFilePath
                Toast.makeText(this,filePath,Toast.LENGTH_LONG).show()
            }
            //Toast.makeText(requireContext(),"uri, ${uriContent?.path.toString()} \n filepath $filePath", Toast.LENGTH_LONG).show()

        } else {
            val exception = result.error
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_using_razorpay)

        AndroidNetworking.initialize(this.applicationContext)

        view = LayoutInflater.from(this@PayUsingRazorpay).inflate(R.layout.pay_dialog, this.findViewById(R.id.pay_layout))

        name = intent.getStringExtra("name").toString() // Specify name of the entity to be purchased
        amt = intent.getStringExtra("amount").toString() // Specify the amount of the entity
        type = intent.getStringExtra("type").toString()

        val wid = intent.getStringExtra("wid").toString()
        Log.d("wid",wid)
        val wallet_a = arrayOf("","wallet1","wallet2","wallet3","wallet4","wallet5")
        wallet = wallet_a[wid.toInt()]
        curr = intent.getStringExtra("curr").toString()
        coin = intent.getStringExtra("coin").toString()

        Checkout.preload(applicationContext)

        val pay = findViewById<Button>(R.id.paybtn)
        findViewById<Button>(R.id.paybtc).setOnClickListener { PayBTC() }
        findViewById<Button>(R.id.paywall).setOnClickListener { paySkrill() }

        pay.setOnClickListener{
            val progressDialog = ProgressDialog(this)
            progressDialog.setCancelable(false)
            progressDialog.setMessage("Generating Order")
            progressDialog.show()

            val amount = amt
            val str_url = Constants.url_razorpay + amount
            val method = Request.Method.GET
            val response = Response.Listener<String> { s ->
                try {
                    progressDialog.dismiss()
                    val res = JSONObject(s)
                    PayNow(res.getString("order_id"), res.getString("amount"), res.getString("currency"), res.getString("keyId"))
                } catch (e: Exception) { e.printStackTrace() }
            }
            val error = Response.ErrorListener { error ->
                progressDialog.dismiss()
                error.stackTrace
            }

            Volley.newRequestQueue(this).add(
                object  : StringRequest(
                    method,
                    str_url,
                    response,
                    error
                ){
                    override fun getParams(): MutableMap<String, String> {
                        val params : MutableMap<String, String> = HashMap()
                        params["amount"] = amount
                        return params
                    }
                }
            )
        }
    }

    private fun PayNow(id: String, amt: String, curr: String, key: String) {
        val checkout = Checkout()
        checkout.setKeyID(key)
        checkout.setImage(R.mipmap.ic_launcher)
        order_id = id

        try {
            val options = JSONObject()
            options.put("name", "E Wallet")
            options.put("description", name)
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("order_id", id) //from response of step 3.
            options.put("theme.color", "#3399cc")
            options.put("currency", curr)
            options.put("amount", amt) //pass amount in currency subunits
            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)

            checkout.open(this, options)
        } catch (e: Exception) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e)
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        AlertDialog.Builder(this).setMessage("Order_ID : $order_id")
            .setTitle("Successful")
            .setCancelable(true)
            .setPositiveButton("Ok") { _, _ ->
                if(type == "add")
                {


                    val progressDialog = ProgressDialog(this)
                    progressDialog.setCancelable(false)
                    progressDialog.setMessage("Adding Money To Wallet")
                    progressDialog.show()

                    val response = Response.Listener<String> { s->
                        progressDialog.dismiss()
                        try {
                            val r = JSONObject(s)
                            Toast.makeText(this, "${r.getString("tmessage")} \n ${r.getString("message")}", Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {e.stackTrace}
                    }
                    val error = Response.ErrorListener { e ->
                        progressDialog.dismiss()
                        e.stackTrace}

                    if (curr == "INR" || curr == "inr") {
                        Volley.newRequestQueue(this).add(
                            object : StringRequest(
                                Method.POST,
                                Constants.url_update_wallet_coin_val,
                                response,
                                error
                            ){
                                override fun getParams(): MutableMap<String, String> {
                                    val param : MutableMap<String,String> = HashMap()
                                    param["userId"] = SharedPrefmanager.getInstance(this@PayUsingRazorpay.applicationContext).keyId
                                    param["walletName"] = wallet
                                    param["coinval"] = amt
                                    return param
                                }
                            }
                        )
                    } else {
                        Volley.newRequestQueue(this).add(
                            object : StringRequest(
                                Method.POST,
                                Constants.url_add_wallet_coin,
                                response,
                                error
                            ){
                                override fun getParams(): MutableMap<String, String> {
                                    val param : MutableMap<String,String> = HashMap()
                                    param["userId"] = SharedPrefmanager.getInstance(this@PayUsingRazorpay.applicationContext).keyId
                                    param["walletName"] = wallet
                                    param["coin"] = coin
                                    return param
                                }
                            }
                        )
                    }
                }
                startActivity(Intent(this@PayUsingRazorpay, MainActivity::class.java))}
            .create()
            .show()

    }

    override fun onPaymentError(p0: Int, p1: String) {
       try {
           val err = JSONObject(p1)
           val me = err.getJSONObject("error")
           Toast.makeText(this,"Reason : ${me.getString("reason")}", Toast.LENGTH_LONG).show()

       } catch (e: JSONException) {e.stackTrace}
    }

    fun PayBTC() {

        val addr = view.findViewById<TextView>(R.id.wallet_address)
        val cpy = view.findViewById<ImageButton>(R.id.copy_btn)
        val cancel = view.findViewById<Button>(R.id.cancel_btn)
        val proceed = view.findViewById<Button>(R.id.proceed_btn)
        val ss = view.findViewById<ImageView>(R.id.pay_ss)
        val btc_addr = "1BvHskAuQ3Yq1PKxjXUN6VWAJKLp9qKn9e"
        val dialog = AlertDialog.Builder(this).setView(view).create()

        cancel.setOnClickListener { dialog.dismiss() }
        addr.text = btc_addr
        cpy.setOnClickListener {
            ss.visibility = View.VISIBLE
            val clipboard : ClipboardManager  = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("btc_wallet", btc_addr)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Address Copied to Clipboard", Toast.LENGTH_SHORT).show()
        }

        ss.setOnClickListener { startCrop() }
        proceed.setOnClickListener { dialog.dismiss()
            ProceedPayment("BTC") }

        dialog.show()

    }

    private fun ProceedPayment(opt : String) {
        if(type == "add")
        {
/*
            val wallet_a = arrayOf("","wallet1","wallet2","wallet3","wallet4","wallet5")
            val wallet = wallet_a[intent.getStringExtra("wid").toString().toInt()]
            val curr = intent.getStringExtra("curr").toString()
            val coin = intent.getStringExtra("coin").toString()*/

            val progressDialog = ProgressDialog(this)
            progressDialog.setCancelable(false)
            progressDialog.setMessage("Adding Money To Wallet")
            progressDialog.max = 100
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            progressDialog.show()



            AndroidNetworking.upload(Constants.url_buy_coin)
                .addMultipartFile("image", File(filePath))
                .addMultipartParameter(
                    "userId",
                    SharedPrefmanager.getInstance(this.applicationContext).keyId
                )
                .addMultipartParameter("amt", amt)
                .addMultipartParameter("wallet", wallet)
                .addMultipartParameter("currency", curr)
                .addMultipartParameter("pay",opt)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener { bytesUploaded, totalBytes ->
                    val progress: Float = (bytesUploaded / totalBytes * 100).toFloat()
                    progressDialog.progress = progress.toInt()
                }
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        progressDialog.dismiss()
                        if (response != null) {
                            Toast.makeText(this@PayUsingRazorpay,
                                response.getString("message"), Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@PayUsingRazorpay, MainActivity::class.java))
                            filePath = ""
                            view.findViewById<ImageView>(R.id.pay_ss).setImageResource(R.drawable.upl_img)
                        }

                    }

                    override fun onError(anError: ANError?) {
                        progressDialog.dismiss()
                        anError!!.stackTrace
                        view.findViewById<ImageView>(R.id.pay_ss).setImageResource(R.drawable.upl_img)
                    }

                })
            }



    }

    fun paySkrill() {
        val skrill_addr = "bsrdigicoin@gmail.com"
        val addr = view.findViewById<TextView>(R.id.wallet_address)
        val cpy = view.findViewById<ImageButton>(R.id.copy_btn)
        val cancel = view.findViewById<Button>(R.id.cancel_btn)
        val ss = view.findViewById<ImageView>(R.id.pay_ss)
        val proceed = view.findViewById<Button>(R.id.proceed_btn)
        val dialog = AlertDialog.Builder(this).setView(view).create()
        cancel.setOnClickListener { dialog.dismiss() }
        addr.text = skrill_addr
        cpy.setOnClickListener {
            ss.visibility = View.VISIBLE
            val clipboard : ClipboardManager  = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("btc_wallet", skrill_addr)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Address Copied to Clipboard", Toast.LENGTH_SHORT).show()
        }
        ss.setOnClickListener { startCrop() }
        proceed.setOnClickListener { dialog.dismiss()
            ProceedPayment("SKRILL") }
        dialog.show()
    }

    fun startCrop(){
        cropImage.launch(
            options {
                setGuidelines(CropImageView.Guidelines.ON)
            }
        )
    }
}