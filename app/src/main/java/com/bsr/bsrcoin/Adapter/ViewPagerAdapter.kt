package com.bsr.bsrcoin.Adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bsr.bsrcoin.Fragments.DocumentsFragment
import com.bsr.bsrcoin.Fragments.ProfileFragment

class ViewPagerAdapter(fm:FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm,lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            1 -> {return DocumentsFragment()}
            else -> {return ProfileFragment()}
        }
    }
}