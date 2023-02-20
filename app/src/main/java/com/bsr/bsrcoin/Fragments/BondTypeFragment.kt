package com.bsr.bsrcoin.Fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.network.BondList
import com.bsr.bsrcoin.network.RetrofitBondAndDepositInstance
import com.bsr.bsrcoin.network.retrofitBondService
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BondTypeFragment : Fragment() {
 var param1: String? = null
    private var param2: String? = null
    private val mHandler=Handler();
    private lateinit var graph:GraphView
    private lateinit var mTimer1:Runnable;
    private lateinit var mSeries1: LineGraphSeries<DataPoint>;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("BondTypeFragment", "call")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_bond_type, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("BondTypeFragment", "call")
        graph=view.findViewById<GraphView>(R.id.idGraphView)
        mSeries1= LineGraphSeries()
        getdata()
        graph.viewport.setMinimalViewport(1.0,12.0, Double.NaN, Double.NaN)
        graph.getViewport().isScrollable = true
        graph.getViewport().isScalable = true;  // activate horizontal zooming and scrolling
        graph.viewport.setScalableY(true);  // activate horizontal and vertical zooming and scrolling
        graph.viewport.setScrollableY(true);
        graph.viewport.isXAxisBoundsManual=true
        graph.getViewport().scrollToEnd()
         graph.gridLabelRenderer.horizontalAxisTitle = "Month"
        // Inflate the layout for this fragment
        Log.d("BondTypeFragment", "graph implemented")
    }
        override fun onResume() {
        super.onResume()
        mTimer1 = object : Runnable {
            override fun run() {
//                mSeries1.resetData(arrayOfNulls(0))
                getdata()
                mHandler.postDelayed(this, 10000)
            }
        }
        mHandler.postDelayed(mTimer1, 10000);
    }

    override fun onPause() {
        mHandler.removeCallbacks(mTimer1);
        super.onPause()
    }

fun getdata(){
   val call=
            RetrofitBondAndDepositInstance.getretrofitBondAndDepositConnection().create(
                retrofitBondService::class.java).getallBond(
            )
        call.enqueue(object : Callback, retrofit2.Callback<BondList> {
            override fun onResponse(
                call: Call<BondList>,
                response: Response<BondList>
            ) {

                if (response.isSuccessful) {
                    Log.v("Api Call type", "type ${response.body()!!}")
                    if (response.body()!!.bond == null
                    ) {
                        Toast.makeText(context, "No Bond Till Now", Toast.LENGTH_SHORT).show()
                    } else {
                        val count = response.body()!!.bond.size
                    val values = ArrayList<DataPoint>()
                        val bonds = response.body()!!.bond
                        var x = 0.0;
                        for (i in 0 until count) {
                            val y=bonds[i].history.split(",").toTypedArray()
                            for(j in y){
                                x = x + 0.1
                                values.add( DataPoint(x,j.toDouble()))
                            }
                        }
                        mSeries1.resetData(values.toTypedArray())
//mSeries1=LineGraphSeries(values.toTypedArray())
                        graph.addSeries(mSeries1)
                        Log.d("Api Type","graph.addseries")
                    }
                } else
                    Log.d("Api Call", "Bad Response Code")
            }


            override fun onFailure(call: Call<BondList>, t: Throwable) {
                Toast.makeText(activity as Context,"Exception Occurred: ${t.message} ", Toast.LENGTH_LONG).show()
            }
        })
}

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BondTypeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BondTypeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}