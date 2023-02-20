package com.bsr.bsrcoin.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bsr.bsrcoin.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BondMainFragment : Fragment() {
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
        val view=inflater.inflate(R.layout.fragment_bond_main, container, false)

        val bond_view = view.findViewById<TextView>(R.id.txt_bondview)
        val bond_apply = view.findViewById<TextView>(R.id.txt_bondapply)

        bond_view.setTextColor(resources.getColor(R.color.green))
        parentFragmentManager.beginTransaction().replace(R.id.fragment_bond_container, ViewBondFragment()).commit()

        bond_view.setOnClickListener{
            bond_view.setTextColor(resources.getColor(R.color.green))
            bond_apply.setTextColor(resources.getColor(R.color.black))
            parentFragmentManager.beginTransaction().replace(R.id.fragment_bond_container, ViewBondFragment()).commit()
        }

        bond_apply.setOnClickListener{
            bond_apply.setTextColor(resources.getColor(R.color.green))
            bond_view.setTextColor(resources.getColor(R.color.black))
            parentFragmentManager.beginTransaction().replace(R.id.fragment_bond_container, BondFragment()).commit()
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BondMainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BondMainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}