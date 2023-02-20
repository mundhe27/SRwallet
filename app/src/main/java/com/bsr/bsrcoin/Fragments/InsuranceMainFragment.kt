package com.bsr.bsrcoin.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bsr.bsrcoin.R

class InsuranceMainFragment : Fragment() {

   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insurance_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val l_view = view.findViewById<TextView>(R.id.txt_iview)
        val l_apply = view.findViewById<TextView>(R.id.txt_iapply)

        l_view.setTextColor(resources.getColor(R.color.green))
        parentFragmentManager.beginTransaction().replace(R.id.fragment_insurance_container, ViewInsuranceFragment()).commit()

        l_view.setOnClickListener{
            l_view.setTextColor(resources.getColor(R.color.green))
            l_apply.setTextColor(resources.getColor(R.color.black))
            parentFragmentManager.beginTransaction().replace(R.id.fragment_insurance_container, ViewInsuranceFragment()).commit()
        }

        l_apply.setOnClickListener{
            l_apply.setTextColor(resources.getColor(R.color.green))
            l_view.setTextColor(resources.getColor(R.color.black))
            parentFragmentManager.beginTransaction().replace(R.id.fragment_insurance_container, InsuranceFragment()).commit()
        }
    }
}