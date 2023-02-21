package com.bsr.bsrcoin

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bsr.bsrcoin.Database.UserDao
import com.bsr.bsrcoin.Database.UserDatabase
import com.bsr.bsrcoin.MysqlConst.Constants
import com.bsr.bsrcoin.ViewModels.UserViewModel
import com.bsr.bsrcoin.utils.ConnectionManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class Login : AppCompatActivity() {

    var db: UserDao? = null
    var dataBase: UserDatabase? = null
    lateinit var userViewModel: UserViewModel

    lateinit var context: Context

    private lateinit var readbtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        readbtn = findViewById(R.id.readbtn)
        readbtn.setOnClickListener {
            val intent = Intent(this, display_contacts::class.java)
            startActivity(intent)
        }



        checkIFLogin()
        val firebase = FirebaseDatabase.getInstance()
        val db = firebase.getReference("message")
        db.child("message1").setValue("hello")

        val login_btn = findViewById<Button>(R.id.login_btn)
        val signup_btn = findViewById<Button>(R.id.signup_btn)
        val forget_btn = findViewById<Button>(R.id.forget_pass)
        val admin_btn = findViewById<Button>(R.id.admin_btn)
        Log.e("time", "onCreate: ${System.currentTimeMillis()}" )

//        val root = FirebaseDatabase.getInstance()
//        val ref = root.getReference("message")
//        ref.setValue("hello")

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        context = this@Login

        val login_id = findViewById<EditText>(R.id.username_id);
        val password_id = findViewById<EditText>(R.id.password_id);

        signup_btn.setOnClickListener { //Your code here
            val intent = Intent(this@Login, Register::class.java)
            startActivity(intent)
        }
        admin_btn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if (ConnectionManager().checkConnectivity(this@Login)) {
                    val email: String = login_id.getText().toString().trim { it <= ' ' }
                    val password: String = password_id.getText().toString().trim { it <= ' ' }

                    val progressDialog = ProgressDialog(this@Login)
                    progressDialog.setMessage("Logging In To App .. ")
                    progressDialog.setCancelable(false)
                    progressDialog.show()

                    val stringRequest = object : StringRequest(
                        Method.POST,
                        Constants.url_login,
                        Response.Listener { s ->
                            progressDialog.dismiss()
                            Log.e("Response",s.toString())

                            try {
                                val response = JSONObject(s)
                                Toast.makeText(this@Login, response.getString("message"), Toast.LENGTH_SHORT).show()
                                if (response.getString("message") == "Login successfully.") {
                                    val det =
                                        response.getJSONObject("userRecords").getJSONArray("admin")
                                            .getJSONObject(0)

                                    SharedPrefmanager.getInstance(this@Login.applicationContext)
                                        .userLogin(
                                            det.getInt("userId"),
                                            det.getString("name"),
                                            det.getString("email"),
                                            det.getString("agent"),
                                            det.getString("account"),
                                            det.getString("admin")
                                        )

                                    SharedPrefmanager.getInstance(context).setLl(SharedPrefmanager.getInstance(this@Login).keyId,
                                        det.getString("lastLogin"))
                                    val intent = Intent(this@Login,MainActivity::class.java)
                                    startActivity(intent)
                                    finish()

                                    FirebaseMessaging.getInstance().token.addOnCompleteListener(
                                        OnCompleteListener { task ->
                                            if(!task.isSuccessful) {
                                                return@OnCompleteListener
                                            }
                                            val token = task.result
                                            SharedPrefmanager.getInstance(this@Login).setToken(token)
                                            Log.e("tokenreceived", "$token")
                                            Volley.newRequestQueue(this@Login).add(
                                                object : StringRequest(
                                                    Method.POST,
                                                    Constants.url_set_token,
                                                    { s->
                                                        try{
                                                            val obj = JSONObject(s)
                                                            Log.d("token message", obj.getString("message"))
                                                        } catch (e: Exception){ e.stackTrace }
                                                    } , {e->
                                                        e.stackTrace
                                                    }
                                                ){
                                                    override fun getParams(): MutableMap<String, String>? {
                                                        val map: MutableMap<String,String> = HashMap()
                                                        map["userId"] = SharedPrefmanager.getInstance(this@Login.applicationContext).keyId
                                                        map["token"] = token
                                                        return map
                                                    }
                                                }
                                            )

                                        })

                                    Log.e("token", SharedPrefmanager.getInstance(this@Login.applicationContext).keyToken)



                                }

                            } catch (e: Exception) {
                                Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
                                e.stackTrace
                                Log.e("Login Catch" , e.toString())
                            }
                        },
                        Response.ErrorListener { e ->
                            progressDialog.dismiss()
                        }) {
                        override fun getParams(): MutableMap<String, String> {
                            val params: MutableMap<String, String> = HashMap()
                            params["email"] = email
                            params["password"] = password
                            val date = Calendar.getInstance().time
                            val formatter =
                                SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
                            val formatedDate = formatter.format(date)
                            Log.e("check", "getParams: $formatedDate", )
                            params["lastLogin"] = formatedDate
                            return params
                        }
                    }

                    val requestQueue = Volley.newRequestQueue(this@Login)
                    requestQueue.add(stringRequest)

//                    var it = userViewModel.getUser(context, email, password, b)
//                    if (it != null) {
//                        val i = Intent(this@Login, MainActivity::class.java)
//                        startActivity(i)
//                        finish()

//                    } else {
//                        Toast.makeText(
//                            this@Login,
//                            "Unregistered user, or incorrect",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }


                }
                else
                {
                    val alertDialog=AlertDialog.Builder(this@Login)
                    alertDialog.setTitle("No Internet")
                    alertDialog.setMessage("No Active Internet Connection Found!!")
                    alertDialog.setPositiveButton("Open Settings"){text,listener->
                        startActivity(Intent(Settings.ACTION_SETTINGS))
                        finish()
                    }
                    alertDialog.setNegativeButton("Exit"){text,listener->
                        ActivityCompat.finishAffinity(this@Login)
                    }
                    alertDialog.show()
                }
            }
        })

        login_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (ConnectionManager().checkConnectivity(this@Login)) {
                    val email: String = login_id.getText().toString().trim { it <= ' ' }
                    val password: String = password_id.getText().toString().trim { it <= ' ' }

                    val progressDialog = ProgressDialog(this@Login)
                    progressDialog.setMessage("Logging In To App .. ")
                    progressDialog.setCancelable(false)
                    progressDialog.show()

                    val stringRequest = object : StringRequest(
                        Method.POST,
                        Constants.url_login,
                        Response.Listener { s ->
                            progressDialog.dismiss()
                            Log.e("Response",s.toString())

                            try {
                                val response = JSONObject(s)
                                Toast.makeText(this@Login, response.getString("message"), Toast.LENGTH_SHORT).show()
                                if (response.getString("message") == "Login successfully.") {
                                    val det =
                                        response.getJSONObject("userRecords").getJSONArray("user")
                                            .getJSONObject(0)

                                    SharedPrefmanager.getInstance(this@Login.applicationContext)
                                        .userLogin(
                                            det.getInt("userId"),
                                            det.getString("name"),
                                            det.getString("email"),
                                            det.getString("agent"),
                                            det.getString("account"),
                                            det.getString("admin")
                                        )

                                    SharedPrefmanager.getInstance(context).setLl(SharedPrefmanager.getInstance(this@Login).keyId,
                                            det.getString("lastLogin"))
                                    val intent = Intent(this@Login,MainActivity::class.java)
                                    startActivity(intent)
                                    finish()

                                    FirebaseMessaging.getInstance().token.addOnCompleteListener(
                                        OnCompleteListener { task ->
                                            if(!task.isSuccessful) {
                                                return@OnCompleteListener
                                            }
                                            val token = task.result
                                            SharedPrefmanager.getInstance(this@Login).setToken(token)
                                            Log.e("tokenreceived", "$token")
                                            Volley.newRequestQueue(this@Login).add(
                                                object : StringRequest(
                                                    Method.POST,
                                                    Constants.url_set_token,
                                                    { s->
                                                        try{
                                                            val obj = JSONObject(s)
                                                            Log.d("token message", obj.getString("message"))
                                                        } catch (e: Exception){ e.stackTrace }
                                                    } , {e->
                                                        e.stackTrace
                                                    }
                                                ){
                                                    override fun getParams(): MutableMap<String, String>? {
                                                        val map: MutableMap<String,String> = HashMap()
                                                        map["userId"] = SharedPrefmanager.getInstance(this@Login.applicationContext).keyId
                                                        map["token"] = token
                                                        return map
                                                    }
                                                }
                                            )

                                        })

                                    Log.e("token", SharedPrefmanager.getInstance(this@Login.applicationContext).keyToken)



                                }

                            } catch (e: Exception) {
                                Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
                                e.stackTrace
                                Log.e("Login Catch" , e.toString())
                            }
                        },
                        Response.ErrorListener { e ->
                            progressDialog.dismiss()
                        }) {
                        override fun getParams(): MutableMap<String, String> {
                            val params: MutableMap<String, String> = HashMap()
                            params["email"] = email
                            params["password"] = password
                            val date = Calendar.getInstance().time
                            val formatter =
                                SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
                            val formatedDate = formatter.format(date)
                            Log.e("check", "getParams: $formatedDate", )
                            params["lastLogin"] = formatedDate
                            return params
                        }
                    }

                    val requestQueue = Volley.newRequestQueue(this@Login)
                    requestQueue.add(stringRequest)

//                    var it = userViewModel.getUser(context, email, password, b)
//                    if (it != null) {
//                        val i = Intent(this@Login, MainActivity::class.java)
//                        startActivity(i)
//                        finish()

//                    } else {
//                        Toast.makeText(
//                            this@Login,
//                            "Unregistered user, or incorrect",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }


                }
                else
                {
                    val alertDialog=AlertDialog.Builder(this@Login)
                    alertDialog.setTitle("No Internet")
                    alertDialog.setMessage("No Active Internet Connection Found!!")
                    alertDialog.setPositiveButton("Open Settings"){text,listener->
                        startActivity(Intent(Settings.ACTION_SETTINGS))
                        finish()
                    }
                    alertDialog.setNegativeButton("Exit"){text,listener->
                        ActivityCompat.finishAffinity(this@Login)
                    }
                    alertDialog.show()
                }
            }

        })

        forget_btn.setOnClickListener { //Your code here
            do_forget_btn_actions()
        }

    }

    private fun checkIFLogin() {
        if(SharedPrefmanager.getInstance(this@Login.applicationContext).isLoggedIN) {
            Toast.makeText(this@Login,"Please wait", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@Login,MainActivity::class.java)
            startActivity(intent)
            finish()

        }
    }

    private fun do_forget_btn_actions() {
        Toast.makeText(applicationContext, "Forget Button Clicked!", Toast.LENGTH_LONG).show()
    }


}