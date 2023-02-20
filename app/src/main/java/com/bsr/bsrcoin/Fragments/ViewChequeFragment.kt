package com.bsr.bsrcoin.Fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bsr.bsrcoin.Adapter.SendRecieveAdapter
import com.bsr.bsrcoin.Models.ChequeModel
import com.bsr.bsrcoin.MysqlConst.Constants
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.SharedPrefmanager
import org.json.JSONException
import org.json.JSONObject

class ViewChequeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_cheque, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cheque = arrayOf("Received Cheques", "Sent Cheques")
        val spinner = view.findViewById<Spinner>(R.id.send_recSpinner)
        val recyclerView = view.findViewById<RecyclerView>(R.id.send_rec_recycle)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        spinner.adapter = ArrayAdapter(activity as Context, android.R.layout.simple_spinner_dropdown_item, cheque)
        val sentChequeArrayList = ArrayList<ChequeModel>()
        val receivedChequeArrayList = ArrayList<ChequeModel>()

        val RecieveAdapter = SendRecieveAdapter("receive", receivedChequeArrayList, requireContext())
        val sendAdapter = SendRecieveAdapter("send", sentChequeArrayList, requireContext())
        recyclerView.adapter = RecieveAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(p2 == 1) {
                    recyclerView.adapter = sendAdapter
                } else {
                    recyclerView.adapter = RecieveAdapter
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                recyclerView.adapter = RecieveAdapter
                spinner.setSelection(0)
            }

        }

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Loading...")
        progressDialog.show()

        Volley.newRequestQueue(requireContext()).add(
            @SuppressLint("NotifyDataSetChanged")
            object : StringRequest (
                Method.GET,
                Constants.url_get_cheque + SharedPrefmanager.getInstance(requireContext().applicationContext).keyId,
                { s->
                    progressDialog.dismiss()
                    try {
                        Log.e("sendRecieve", s)
                        val send = JSONObject(s).getJSONArray("sent")
                        val recieve = JSONObject(s).getJSONArray("recieved")
                        for(i in 0 until send.length()) {
                            val s_obj = send.getJSONObject(i)
                            sentChequeArrayList.add(
                                ChequeModel(
                                    s_obj.getString("chequeId"),
                                    s_obj.getString("account"),
                                    s_obj.getString("date"),
                                    s_obj.getString("amount"),
                                    s_obj.getString("status")
                                )
                            )
                        }

                        for(i in 0 until recieve.length()) {
                            val s_obj = recieve.getJSONObject(i)
                            receivedChequeArrayList.add(
                                ChequeModel(
                                    s_obj.getString("chequeId"),
                                    s_obj.getString("account"),
                                    s_obj.getString("date"),
                                    s_obj.getString("amount"),
                                    s_obj.getString("status")
                                )
                            )
                        }

                        RecieveAdapter.notifyDataSetChanged()
                        sendAdapter.notifyDataSetChanged()
                    } catch (e: JSONException) {
                        e.stackTrace
                    }
                },
                { e->
                    progressDialog.dismiss()
                    e.stackTrace
                }
            ) {

            }
        )

    }
}