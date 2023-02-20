package com.bsr.bsrcoin


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color

import android.graphics.drawable.ColorDrawable
import android.provider.Settings
import android.text.InputType
import android.util.Log
import android.util.Patterns
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bsr.bsrcoin.MysqlConst.Constants
import com.bsr.bsrcoin.ViewModels.UserViewModel
import com.bsr.bsrcoin.utils.ConnectionManager
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class AgentRegister : AppCompatActivity() {
    lateinit var userViewModel: UserViewModel

    lateinit var context: Context

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        showdialog()
        val titleView = findViewById<TextView>(R.id.signupTextView)
        titleView.setText("Agent Sign Up")
        val fullName = findViewById<EditText>(R.id.fullname_id)
        val occupation = findViewById<EditText>(R.id.occupation_id)
        val phoneNum = findViewById<EditText>(R.id.phonenum_id)
        val email = findViewById<EditText>(R.id.emailinp_id)
        val password = findViewById<EditText>(R.id.passwordinp_id)
        val income = findViewById<EditText>(R.id.income_id)
        val conform_pass=findViewById<EditText>(R.id.conform_pass_id)
        val loginBtn = findViewById<Button>(R.id.login2_btn)
        val signupBtn = findViewById<Button>(R.id.signup2_btn)
        val account_num=findViewById<EditText>(R.id.account_number_id)
        val dob=findViewById<TextView>(R.id.dob)
        val home_number=findViewById<EditText>(R.id.home_number)
        val street=findViewById<EditText>(R.id.street_name)
        val city=findViewById<EditText>(R.id.city)
        val pincode=findViewById<EditText>(R.id.pincode)
        val refcode=findViewById<EditText>(R.id.reference_id)

        val switch = findViewById<Button>(R.id.toogle_btn)
        switch.visibility = View.GONE



        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        context = this@AgentRegister

        val calendar: Calendar = Calendar.getInstance()

        val year: Int = calendar.get(Calendar.YEAR)

        val month: Int = calendar.get(Calendar.MONTH)

        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

        val simpleDateFormat1 = SimpleDateFormat("dd/MM/yyyy")


        val dateSetListener1 :  DatePickerDialog.OnDateSetListener =
            OnDateSetListener { view, year, month, day ->
                // here month+1 is used so that
                // actual month number can be displayed
                // otherwise it starts from 0 and it shows
                // 1 number less for every month
                // example- for january month=0
                var month = month
                month = month + 1
                val date = "$day/$month/$year"
                dob.setText(date)
            }

        dob.setOnClickListener(View.OnClickListener { // date picker dialog is used
            // and its style and color are also passed
            val datePickerDialog = DatePickerDialog(
                this@AgentRegister,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateSetListener1,
                year,
                month,
                day
            )
            // to set background for datepicker
            datePickerDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            datePickerDialog.show()
        })


        loginBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //Your code here
                val intent = Intent(this@AgentRegister, Login::class.java)
                startActivity(intent)

            }
        })

        signupBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (ConnectionManager().checkConnectivity(this@AgentRegister)) {
                    val full_name = fullName.text.toString().trim()
                    val emaile_s = email.text.toString().trim()
                    val income_s = income.text.toString().trim()
                    val phone_num = phoneNum.text.toString().trim()
                    val occupation_s = occupation.text.toString().trim()
                    val account_num_s = account_num.text.toString().trim()
                    val pas = password.text.toString().trim()
                    val pass_c = conform_pass.text.toString().trim()
                    val dob_s = dob.text.toString().trim()
                    val home_s = home_number.text.toString().trim()
                    val street_s = street.text.toString().trim()
                    val city_s = city.text.toString().trim()
                    val pincode_s = pincode.text.toString().trim()
                    val reference_s = refcode.text.toString().trim()

                    if (full_name.isEmpty()) {
                        fullName.setError("Empty Field")
                    } else if (emaile_s.isEmpty() || !isEmailValid(emaile_s)!!) {
                        email.setError("Empty Field")
                    } else if (income_s.isEmpty()) {
                        income.setError("Empty Field")
                    } else if (phone_num.isEmpty() || phone_num.length < 10) {
                        phoneNum.setError("Empty Field")
                    } else if (occupation_s.isEmpty()) {
                        occupation.setError("Empty Field")
                    } else if (account_num_s.isEmpty()) {
                        account_num.setError("Empty Field")
                    } else if (pas.isEmpty()) {
                        password.setError("Empty Field")
                    } else if (dob_s.equals("DOB- DD/MM/YYYY")) {
                        dob.setError("Not selected")
                    } else if (home_s.isEmpty()) {
                        home_number.setError("Empty Field")
                    } else if (street_s.isEmpty()) {
                        street.setError("Empty Field")
                    } else if (city_s.isEmpty()) {
                        city.setError("Empty Field")
                    } else if (pincode_s.isEmpty()) {
                        pincode.setError("Empty Field")
                    } else if (pas.equals(pass_c)) {


                        if(reference_s != "") {

                            val progressDialog = ProgressDialog(this@AgentRegister)
                            progressDialog.setMessage("Checking Reference")
                            progressDialog.setCancelable(false)
                            progressDialog.show()


                            Volley.newRequestQueue(this@AgentRegister).add(
                                object : StringRequest(
                                    Method.POST,
                                    Constants.url_reference,
                                    { s ->
                                        try {
                                            val response = JSONObject(s)
                                            progressDialog.dismiss()

                                            if(response.getString("message") == "Valid Code") {
                                                val b = "yes"
                                                val adress = "$home_s, $street_s, $city_s, $pincode_s"

                                                Signup(emaile_s, full_name, phone_num, pas, occupation_s, account_num_s, income_s, b, dob_s, adress, response.getString("id"))

                                            } else {
                                                refcode.error = "Invalid Code"
                                            }
                                        } catch (e: JSONException) { e.stackTrace }
                                    },
                                    { error ->
                                        progressDialog.dismiss()
                                        error.stackTrace
                                    }
                                ){
                                    override fun getParams(): MutableMap<String, String> {
                                        val params : MutableMap<String, String> = HashMap()
                                        params["refcode"] = reference_s
                                        return params
                                    }
                                }
                            )
                        } else {
                            var b = "no"
                            val adress = "$home_s, $street_s, $city_s, $pincode_s"

                                b = "yes"

                            Signup(emaile_s, full_name, phone_num, pas, occupation_s, account_num_s, income_s, b, dob_s, adress, "")

                        }

                    } else {
                        Toast.makeText(
                            this@AgentRegister,
                            "Password is not matching",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }
                else{
                    val alertDialog= AlertDialog.Builder(this@AgentRegister)
                    alertDialog.setTitle("No Internet")
                    alertDialog.setMessage("No Active Internet Connection Found!!")
                    alertDialog.setPositiveButton("Open Settings"){text,listener->
                        startActivity(Intent(Settings.ACTION_SETTINGS))
                    }
                    alertDialog.setNegativeButton("Exit"){text,listener->
                        ActivityCompat.finishAffinity(this@AgentRegister)
                    }
                    alertDialog.show()

                }
            }
        })

    }

    private fun showdialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Enter Password")
        val input = EditText(this)
        input.setHint("Enter Password")
        input.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        builder.setView(input)
        // Set up the buttons
        builder.setPositiveButton("OK") { dialog, which ->
            // Here you get get input text from the Edittext
            val m_Text = input.text.toString()

            if (m_Text.equals("agentsignup")) {
                Toast.makeText(this@AgentRegister, "Ok", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@AgentRegister, "INCORRECT PASSWORD", Toast.LENGTH_LONG).show()
                val intent = Intent(this@AgentRegister, Login::class.java)
                startActivity(intent)
                finish()
            }
        }

        builder.show()
    }

    private fun Signup(
        emaileS: String,
        fullName: String,
        phoneNum: String,
        pas: String,
        occupationS: String,
        accountNumS: String,
        incomeS: String,
        b: String,
        dobS: String,
        adress: String,
        userId: String
    ) {

        val progressDialog = ProgressDialog(this@AgentRegister)
        progressDialog.setMessage("Registering User.....")
        progressDialog.setCancelable(false)
        progressDialog.show()

        Volley.newRequestQueue(this@AgentRegister).add(
            object  : StringRequest(
                Method.POST,
                Constants.url_register,
                { s ->
                    try {
                        progressDialog.dismiss()
                        val res = JSONObject(s)
                        Log.e("Register Process", "Succefully done $res")
                        Toast.makeText(this@AgentRegister, res.getString("message"), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@AgentRegister, Login::class.java)
                        startActivity(intent)
                    } catch (e : JSONException) {
                        Log.e("Register Process1", e.toString())
                        e.stackTrace
                    }

                },
                { error ->

                    progressDialog.dismiss()
                    Log.e("Register Process2", error.toString())

                    error.stackTrace }
            ){
                override fun getParams(): MutableMap<String, String> {
                    val params : MutableMap<String, String> = HashMap()
                    params["email"] = emaileS
                    params["name"] =  fullName
                    params["phone_num"] = phoneNum
                    params["password"] = pas
                    params["occupation"] = occupationS
                    params["adhar_number"] = accountNumS
                    params["annual_Income"] = incomeS
                    params["agent"] = b
                    params["dob"] = dobS
                    params["address"] = adress
                    params["ref"] = phoneNum.substring(0,5) + "_" + fullName.substring(0,2) + accountNumS.substring(0,2)
                    params["uid"] = userId
                    return params
                }
            }
        )

    }

    fun isEmailValid(email: CharSequence?): Boolean? {
        return email?.let { Patterns.EMAIL_ADDRESS.matcher(it).matches() }
    }
}

