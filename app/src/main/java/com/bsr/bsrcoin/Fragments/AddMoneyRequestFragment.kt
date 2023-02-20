package com.bsr.bsrcoin.Fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bsr.bsrcoin.Adapter.RequestAdapter
import com.bsr.bsrcoin.Models.AddRequestModel
import com.bsr.bsrcoin.MysqlConst.Constants
import com.bsr.bsrcoin.R
import org.json.JSONArray

class AddMoneyRequestFragment : Fragment() {
      override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_money_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arrayList = ArrayList<AddRequestModel>()

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycle_add_req)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val requestAdapter = RequestAdapter(requireContext(), arrayList)
        recyclerView.adapter = requestAdapter

        val dialog = ProgressDialog(requireContext())
        dialog.setMessage("Loading..")
        dialog.setCancelable(false)
        dialog.show()

        Volley.newRequestQueue(requireContext()).add(
            @SuppressLint("NotifyDataSetChanged")
            object : StringRequest(
                Method.GET,
                Constants.url_get_requests,
                {s ->
                    dialog.dismiss()
                    try {
                        val array = JSONArray(s)
                        for(i in 0 until array.length()){
                            val obj = array.getJSONObject(i)
                            arrayList.add(
                                AddRequestModel(
                                obj.getString("reqId"),
                                    obj.getString("userId"),
                                    obj.getString("amount"),
                                    Constants.url_get_requests_image + obj.getString("image")
                            )
                            )
                        }
                        requestAdapter.notifyDataSetChanged()

                    } catch (e: Exception) { e.stackTrace }
                },
                {e ->
                e.stackTrace
                    dialog.dismiss()
                }
            ){

            }
        )
    }

}