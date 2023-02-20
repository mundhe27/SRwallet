package com.bsr.bsrcoin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsr.bsrcoin.Adapter.UserAgentAdapter
import com.bsr.bsrcoin.ViewModels.UserAgentViewModel

class UserAgentListActivity : AppCompatActivity() {
    private lateinit var recyclerview : RecyclerView
    private lateinit var UserAdminList : ArrayList<UserAgentViewModel>

//    private  var userOrAgent : Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_agent_recyclerview)

        recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)
        val data = ArrayList<UserAgentViewModel>()

        val adapter = UserAgentAdapter(data)
        recyclerview.adapter = adapter
    }
}