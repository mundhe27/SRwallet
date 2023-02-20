package com.bsr.bsrcoin.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bsr.bsrcoin.MysqlConst.Constants
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.SharedPrefmanager
import com.bsr.bsrcoin.databinding.FragmentProfileBinding
import com.bsr.bsrcoin.databinding.FragmentProfileTabBinding
import org.json.JSONObject
import java.lang.reflect.Method


class ProfileFragment : Fragment() {
    private val TAG = "ProfileFragment"
    private lateinit var binding : FragmentProfileTabBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileTabBinding.inflate(inflater,container,false)
        val userId = SharedPrefmanager.getInstance(context).keyId

        sendProfileRequest(userId)
        return binding.root
    }

    private fun sendProfileRequest(userId: String?) {
        val url = Constants.url_get_userInfo + "?id=$userId"
        val queue = Volley.newRequestQueue(context)

        val request = StringRequest(Request.Method.GET, url,{response ->
            val res = JSONObject(response)
            val array = res.getJSONArray("user")
            val obj = array.getJSONObject(0)

            Log.e(TAG, "sendProfileRequest: $response" )
            binding.nametxt.text = obj.getString("name")
            binding.emailtxt.text = obj.getString("email")
            binding.mobtxt.text = obj.getString("phone_num")
            binding.dobtxt.text = obj.getString("dob")
            binding.adhtxt.text = obj.getString("adhar_number")

        },{error -> Log.e(TAG, "sendProfileRequest: $error" )})

        queue.add(request)

    }


}