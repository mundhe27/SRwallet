package com.bsr.bsrcoin.Fragments

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsr.bsrcoin.Adapter.ViewDepositAdapter
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.SharedPrefmanager
import com.bsr.bsrcoin.network.Deposit
import com.bsr.bsrcoin.network.DepositList
import com.bsr.bsrcoin.network.RetrofitBondAndDepositInstance
import com.bsr.bsrcoin.network.retrofitDepositService
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


class fund_share : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fund_deposit, container, false)
    }


}