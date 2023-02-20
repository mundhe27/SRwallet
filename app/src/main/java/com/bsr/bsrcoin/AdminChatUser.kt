package com.bsr.bsrcoin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsr.bsrcoin.Adapter.AdminChatAdapter
import com.bsr.bsrcoin.Adapter.ChatAdapter
import com.bsr.bsrcoin.ViewModels.MessageModel
import com.bsr.bsrcoin.databinding.ActivityAdminChatUserBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.snapshot.Node
import com.google.firebase.ktx.Firebase

class AdminChatUser : AppCompatActivity() {

    lateinit var binding : ActivityAdminChatUserBinding
    val database = Firebase.database
    var aref : DatabaseReference?=null
    var userids = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminChatUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        aref = database.reference.child("Chats").child("Agents")
        val adapter = AdminChatAdapter(userids)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        adapter.setonitemclicklistener(object : AdminChatAdapter.Clicklistener
        {
            override fun onitemclick(position: Int, v: View)
            {
                intent = Intent(this@AdminChatUser,ChatActivity::class.java)
                intent.putExtra("id",userids.get(position))
                startActivity(intent)
            }

            override fun onlongitemclick(position: Int, v: View)
            {
                Toast.makeText(this@AdminChatUser,"Long Click : " + position,Toast.LENGTH_SHORT).show()
            }
        })

        aref!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot)
            {
                userids.clear()
                for(data in snapshot.children)
                    data.key?.let { userids.add(it) }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError)
            {
                Toast.makeText(this@AdminChatUser,"Error getting messages", Toast.LENGTH_SHORT).show()
            }
        })
    }
}