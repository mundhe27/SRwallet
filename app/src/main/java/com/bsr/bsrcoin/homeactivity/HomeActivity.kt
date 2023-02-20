package com.bsr.bsrcoin.homeactivity

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bsr.bsrcoin.MainActivity
import com.bsr.bsrcoin.R
import java.util.jar.Manifest

class HomeActivity : AppCompatActivity() {

//    when {
//
////        ContextCompat.checkSelfPermission(
//val requestPermissionLauncher = registerForActivityResult(RequestPermissions())
//{
//        isGranted : Boolean ->
//    if(isGranted){
//    }
//    else
//    {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_fragment_update)


    }

    fun To_myaccount(view: View)
    {
        val intent = Intent(this@HomeActivity, my_account::class.java)
        startActivity(intent)
    }

    fun To_fundtransfer(view: View)
    {
        val intent = Intent(this@HomeActivity, MainActivity::class.java)
        startActivity(intent)
    }

    fun to_payments(view: View)
    {
        val intent = Intent(this@HomeActivity, payments::class.java)
        startActivity(intent)
    }

    fun To_paydeues(view: View)
    {
        val intent = Intent(this@HomeActivity, pay_dues::class.java)
        startActivity(intent)
    }


    fun To_recharge(view: View)
    {
        val intent = Intent(this@HomeActivity, recharge::class.java)
        startActivity(intent)
    }

    fun To_TNEB(view: View)
    {
        val intent = Intent(this@HomeActivity, TNEB::class.java)
        startActivity(intent)
    }

    fun To_checkservice(view: View)
    {
        val intent = Intent(this@HomeActivity, checkservice::class.java)
        startActivity(intent)
    }

    fun To_bharatqr(view: View)
    {
        val intent = Intent(this@HomeActivity, bharatqr::class.java)
        startActivity(intent)
    }

    fun To_wealthmanagement(view: View)
    {
        val intent = Intent(this@HomeActivity, wealthManagement::class.java)
        startActivity(intent)
    }

}

//                }
