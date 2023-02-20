package com.bsr.bsrcoin.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bsr.bsrcoin.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ShareMainFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_share_main, container, false)
        val share_view = view.findViewById<TextView>(R.id.txt_shareview)
        val share_apply = view.findViewById<TextView>(R.id.txt_shareapply)

        share_view.setTextColor(resources.getColor(R.color.green))
        parentFragmentManager.beginTransaction().replace(R.id.fragment_share_container, ViewShareFragment()).commit()

        share_view.setOnClickListener{
            share_view.setTextColor(resources.getColor(R.color.green))
            share_apply.setTextColor(resources.getColor(R.color.black))
            parentFragmentManager.beginTransaction().replace(R.id.fragment_share_container, ViewShareFragment()).commit()
        }

        share_apply.setOnClickListener{
            share_apply.setTextColor(resources.getColor(R.color.green))
            share_view.setTextColor(resources.getColor(R.color.black))
            parentFragmentManager.beginTransaction().replace(R.id.fragment_share_container, ShareFragment()).commit()
        }
        return view

    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShareMainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}