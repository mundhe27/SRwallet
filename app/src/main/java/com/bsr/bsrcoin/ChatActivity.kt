package com.bsr.bsrcoin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.bsr.bsrcoin.Adapter.ChatAdapter
import com.bsr.bsrcoin.ViewModels.MessageModel
import com.bsr.bsrcoin.databinding.ActivityChatBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.Exception

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    var messages = mutableListOf<MessageModel>()
    lateinit var id : String
    val database = Firebase.database
    var ref : DatabaseReference?=null
    var aref : DatabaseReference?=null
    var spm = SharedPrefmanager.getInstance(this@ChatActivity)
    companion object mysalt
    {
        val salt = "++++"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        id = intent.getStringExtra("id")!!

        ref = database.reference.child("Chats").child("Users").child(id)
        aref = database.reference.child("Chats").child("Agents").child(id)

        if(spm.isAgent)
        {
            val arr = id.split(salt)
            var name = ""
            for(i in arr.indices)
            {
                if(i!=arr.size-1)
                    name+=arr[i]
            }
            binding.sendername.setText(name)
        }
        else binding.sendername.setText("")

        binding.back.setOnClickListener { view -> finish() }
        binding.menu.setOnClickListener { view ->
            Toast.makeText(this, "Menu Clicked", Toast.LENGTH_SHORT).show()
        }

//        getmessages()
        val adapter = ChatAdapter(messages,spm)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this, VERTICAL,false)

        ref!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot)
            {
                messages.clear()
                for(data in snapshot.children) {
                    try {
                        data.getValue(MessageModel::class.java)?.let { messages.add(it) }
                    }
                    catch (e : Exception)
                    {
                        break
                    }
                }
                adapter.notifyDataSetChanged()
                binding.recyclerview.scrollToPosition(messages.size-1)
            }

            override fun onCancelled(error: DatabaseError)
            {
                Toast.makeText(this@ChatActivity,"Error getting messages",Toast.LENGTH_SHORT).show()
            }
        })


        adapter.setonitemclicklistener(object : ChatAdapter.Clicklistener
        {
            override fun onitemclick(position: Int, v: View)
            {
                Toast.makeText(this@ChatActivity,"Short Click : " + position,Toast.LENGTH_SHORT).show()
            }

            override fun onlongitemclick(position: Int, v: View)
            {
                Toast.makeText(this@ChatActivity,"Long Click : " + position,Toast.LENGTH_SHORT).show()
            }
        })

        binding.send.setOnClickListener { view ->
            val msg = binding.typemsg.text.toString()
            if(msg.isNotEmpty())
            {
                val time = System.currentTimeMillis()

                var message : MessageModel

                if(spm.isAgent)
                {
                    val senderid = spm.keyUsernameName + salt + spm.keyId
                    message = MessageModel(msg,senderid,id,UUID.randomUUID().toString(),time,spm.keyUsernameName)
                }
                else
                    message = MessageModel(msg,id,UUID.randomUUID().toString(),time)

                ref!!.push().setValue(message)
                { error,myref ->
                    if (error!=null) Toast.makeText(this@ChatActivity,error.message,Toast.LENGTH_SHORT).show()
                    else Toast.makeText(this@ChatActivity,"Sent Success",Toast.LENGTH_SHORT).show()
                }
                ref!!.child("lastmsgtime").setValue(time)
                ref!!.child("lastmsg").setValue(msg)

                aref!!.push().setValue(message)
                { error,myref ->
                    if (error!=null) Toast.makeText(this@ChatActivity,error.message,Toast.LENGTH_SHORT).show()
                    else Toast.makeText(this@ChatActivity,"Receive Success",Toast.LENGTH_SHORT).show()
                }
                aref!!.child("lastmsgtime").setValue(time)
                aref!!.child("lastmsg").setValue(msg)

                binding.typemsg.text.clear()
            }
            else
                binding.typemsg.setError("Invalid msg")
        }
    }

//    private fun getmessages()
//    {
//        messages.clear()
//        ref?.get()?.addOnSuccessListener { snapshot ->
//            for (data in snapshot.children) {
//                data.getValue(MessageModel::class.java)?.let { messages.add(it) }
//            }
//        }?.addOnFailureListener { OnFailureListener{ e->
//            Toast.makeText(this,e.message,Toast.LENGTH_SHORT).show()
//        } }
//    }
}

