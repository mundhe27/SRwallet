package com.bsr.bsrcoin.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Spinner
import com.bsr.bsrcoin.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var Frame: FrameLayout
    val choice= arrayOf<String>("Send Money","Deposit","Bond")
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
        val v=inflater.inflate(R.layout.fragment_main, container, false)
        Frame=v.findViewById(R.id.mainFrame)
        openHome()
        val spin = v.findViewById<Spinner>(R.id.Choice)
        spin.adapter = ArrayAdapter<String>(activity as Context, android.R.layout.simple_spinner_dropdown_item, choice)
        var selectedfragment: String ?= choice[0]
        spin.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                val frame=R.id.mainFrame
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedfragment= choice[p2]
                    if(p2==0) openHome()
                    if(p2==1){
                        fragmentManager?.beginTransaction()?.replace(frame,DepositFragment())?.commit()
                    }
                    else if(p2==2){
                        fragmentManager?.beginTransaction()?.replace(frame,BondFragment())?.commit()
                           }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }



        return v
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun openHome() {
        fragmentManager?.beginTransaction()?.replace(R.id.mainFrame,HomeFragment())?.commit()
    }
}