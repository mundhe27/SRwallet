package com.bsr.bsrcoin.History

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bsr.bsrcoin.R

class UserOrAgentChoice : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_agent)
        val user = findViewById<TextView>(R.id.tvUser);
        val agent = findViewById<TextView>(R.id.tvAgent);
    }
    private fun onClickUser(){
        val intent = Intent(this, ListOfUserOrAgentActivity::class.java)
        startActivity(intent)
    }

    private fun onClickAgent(){
        val intent = Intent(this, ListOfUserOrAgentActivity::class.java)
        startActivity(intent)
    }
}