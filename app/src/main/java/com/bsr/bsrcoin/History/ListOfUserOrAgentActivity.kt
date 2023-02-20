package com.bsr.bsrcoin.History

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsr.bsrcoin.Adapter.UserAgentAdapter
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.ViewModels.UserAgentViewModel

class ListOfUserOrAgentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_agent_recyclerview)

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)

        val data = ArrayList<UserAgentViewModel>()

        for (i in 1..20) {
            data.add(UserAgentViewModel( "Item " + i))
        }

        val adapter = UserAgentAdapter(data)

        recyclerview.adapter = adapter
    }
}