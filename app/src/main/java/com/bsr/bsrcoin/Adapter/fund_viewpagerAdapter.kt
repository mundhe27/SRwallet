package com.bsr.bsrcoin.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bsr.bsrcoin.Fragments.fund_bond
import com.bsr.bsrcoin.Fragments.fund_deposit
import com.bsr.bsrcoin.Fragments.fund_share

class fund_viewpagerAdapter(val fm:FragmentManager,val lifecycle:Lifecycle) : FragmentStateAdapter(fm,lifecycle) {
    override fun getItemCount(): Int {
        return 3;
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0->{return fund_deposit()}
            1->{return fund_bond()
            }
            else->{return fund_share()
            }
            }
        }
    }
