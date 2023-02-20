package com.bsr.bsrcoin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bsr.bsrcoin.History.UserOrAgentChoice

class AdminMain : AppCompatActivity() {
    @SuppressLint("WrongViewCast")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity_main)
        val bal = findViewById<TextView>(R.id.tvBalance);
        val addAgentBtn = findViewById<Button>(R.id.btn1);
        val removeAgentBtn = findViewById<Button>(R.id.btn2);
        val bsrChangeBtn = findViewById<Button>(R.id.btn3);
        val historyBtn = findViewById<Button>(R.id.btn4);
        val loanBtn = findViewById<Button>(R.id.btn5);
        val insuranceBtn = findViewById<Button>(R.id.btn6);

        val email = findViewById<EditText>(R.id.etEmail);
        val amount = findViewById<EditText>(R.id.etAmount);

        val addMoney = findViewById<Button>(R.id.button1);
        val sendMoney = findViewById<Button>(R.id.button2);

        addAgentBtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                openAddAgentWin();
            }
        })
        removeAgentBtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                openRemoveAgentWin();
            }
        })
        bsrChangeBtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                bsrChange();    // TODO :alert Dialog
            }
        })
        historyBtn.setOnClickListener(object : View.OnClickListener{   // history (includes all 3 tran loan insurance of both user and agent)
            override fun onClick(v: View?) {
                val intent = Intent(this@AdminMain, UserOrAgentChoice::class.java)
                startActivity(intent)
            }
        })
        loanBtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(this@AdminMain, AllLoansActivity::class.java)
                startActivity(intent)
            }
        })
        insuranceBtn.setOnClickListener(object : View.OnClickListener{  // insurance
            override fun onClick(v: View?) {
                val intent = Intent(this@AdminMain, AllInsuranceActivity::class.java)
                startActivity(intent)
            }
        })

// Button Add Money and Send Money using Email and Amount of different User or different Agent
        addMoney.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val email: String = email.getText().toString().trim { it <= ' ' }
                val amount: String = amount.getText().toString().trim { it <= ' ' }

                //TODO: Change the balance of Admin
            }
        })
        sendMoney.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                // TODO: Send money from Admin account to other user/agent
            }
        })
    }

    private fun openAddAgentWin(){
        // TODO: direct to signup page
    }
    private fun openRemoveAgentWin(){
        // TODO: ...
    }

    private fun bsrChange(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this);
        builder.setTitle("Change the BSR Rate")
    }


}