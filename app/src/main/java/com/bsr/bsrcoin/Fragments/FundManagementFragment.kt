package com.bsr.bsrcoin.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bsr.bsrcoin.Adapter.fund_viewpagerAdapter
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.databinding.FragmentFundManagementBinding
import com.google.android.material.tabs.TabLayoutMediator

class FundManagementFragment : Fragment() {

    private lateinit var binding : FragmentFundManagementBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFundManagementBinding.inflate(inflater,container,false)
        binding.fundViewpager.adapter = fund_viewpagerAdapter(childFragmentManager,lifecycle)

        TabLayoutMediator(binding.fundTab,binding.fundViewpager){tab,position->
            when(position){
                0->{tab.text="Deposit"}
                1->{tab.text="Bond"}
                else->{tab.text="Share"}
            }
        }.attach()

        return binding.root
    }


}