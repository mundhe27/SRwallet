package com.bsr.bsrcoin

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bsr.bsrcoin.MysqlConst.Constants
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException


class Permissions : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_permissions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val depotoggle = view.findViewById<ToggleButton>(R.id.depotoggle)
        val insaurancetoggle = view.findViewById<ToggleButton>(R.id.insaurancetoggle)
        val loantoggle = view.findViewById<ToggleButton>(R.id.loantoggle)
        val sharetoggle = view.findViewById<ToggleButton>(R.id.sharetoggle)
        val button_Permission = view.findViewById<Button>(R.id.Button_permission)



        var depobool="true"
        var insaurancebool="true"
        var loanbool="true"
        var sharebool="true"



        depotoggle.setOnCheckedChangeListener{compoundButton, isChecked ->
            if (depotoggle.isChecked()){
                    depobool="true"


                Toast.makeText(requireActivity(), "Deposit permission is enabled", Toast.LENGTH_SHORT).show()
                }else{
                    depobool="false"

                    Toast.makeText(requireActivity(), "Deposit permission is disabled", Toast.LENGTH_SHORT).show()
                }
        }

        insaurancetoggle.setOnCheckedChangeListener{compoundButton, isChecked ->
            if (insaurancetoggle.isChecked()){
                insaurancebool="true"
                Toast.makeText(requireActivity(), "Insaurance permission is enabled", Toast.LENGTH_SHORT).show()
            }else{
                insaurancebool="false"
                Toast.makeText(requireActivity(), "Insaurance permission is disabled", Toast.LENGTH_SHORT).show()
            }
        }
        loantoggle.setOnCheckedChangeListener{compoundButton, isChecked ->
            if (loantoggle.isChecked()){
                loanbool="true"
                Toast.makeText(requireActivity(), "Loan permission is enabled", Toast.LENGTH_SHORT).show()

            }else{
                loanbool="false"
                Toast.makeText(requireActivity(), "Loan permission is disabled", Toast.LENGTH_SHORT).show()
            }
        }
        sharetoggle.setOnCheckedChangeListener{compoundButton, isChecked ->
            if (sharetoggle.isChecked()){
                sharebool="true"
                Toast.makeText(requireActivity(), "Share permission is enabled", Toast.LENGTH_SHORT).show()
            }else{
                sharebool="false"
                Toast.makeText(requireActivity(), "Share permission is disabled", Toast.LENGTH_SHORT).show()
            }
        }

        button_Permission.setOnClickListener {
            Toast.makeText(requireActivity(), "clicked", Toast.LENGTH_SHORT).show()
            val progressDialog = ProgressDialog(requireActivity())
            progressDialog.setMessage("permission setting.....")
            progressDialog.setCancelable(false)
            progressDialog.show()
            Volley.newRequestQueue(requireActivity()).add(
                object : StringRequest(
                    Method.POST,
                    Constants.url_PermissionDetails,
                    { s->
                        try {
                            progressDialog.dismiss()
                            val res = JSONObject(s)
                            Log.e("Permission Setting", "Successfully done $res")
                            Toast.makeText(requireActivity(), res.getString("message"), Toast.LENGTH_SHORT).show()
                            val intent = Intent(requireActivity(),Permissions::class.java)
                            startActivity(intent)
                        }catch (e:JSONException){
                            Log.e("Permission setting1", e.toString())
                            e.stackTrace
                        }
                    },
                    {
                        error->
                        progressDialog.dismiss()
                        Log.e("Permission setting 2", error.toString())
                        error.stackTrace
                    }
                ){
                    override fun getParams(): MutableMap<String, String>? {
                        val params: MutableMap<String, String> =HashMap()
                        params["t1"]=depobool
                        params["t2"] = insaurancebool
                        params["t3"]= loanbool
                        params["t4"]= sharebool
                        return params
                    }

    })

}}}