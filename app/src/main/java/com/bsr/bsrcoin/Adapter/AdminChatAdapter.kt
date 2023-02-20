package com.bsr.bsrcoin.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bsr.bsrcoin.AdminChatUser
import com.bsr.bsrcoin.ChatActivity
import com.bsr.bsrcoin.ViewModels.MessageModel
import com.bsr.bsrcoin.databinding.ActivityAdminChatBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class AdminChatAdapter(private val userids : MutableList<String>) : RecyclerView.Adapter<AdminChatAdapter.ViewHolder>()
{
    val database = Firebase.database
    var aref = database.reference.child("Chats").child("Agents")

    companion object listener
    {
        private lateinit var clicklistener : AdminChatAdapter.Clicklistener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val binding : ActivityAdminChatBinding = ActivityAdminChatBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    class ViewHolder(var binding: ActivityAdminChatBinding) : RecyclerView.ViewHolder(
        binding.root
    ), View.OnClickListener, View.OnLongClickListener
    {
        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(p0: View?)
        {
            clicklistener.onitemclick(adapterPosition,itemView)
        }

        override fun onLongClick(p0: View?): Boolean
        {
            clicklistener.onlongitemclick(adapterPosition,itemView)
            return true
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val arr = userids.get(position).split(ChatActivity.salt)
        var name = ""
        for(i in arr.indices)
        {
            if(i!=arr.size-1)
                name+=arr[i]
        }
        holder.binding.UserName.setText(name)

        var myref = database.reference.child("Chats").child("Agents").child(userids.get(position)).child("lastmsg")
        myref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot)
            {
                holder.binding.lastmsg.setText(snapshot.getValue(String::class.java))
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        myref = database.reference.child("Chats").child("Agents").child(userids.get(position)).child("lastmsgtime")
        myref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot)
            {
                val time : Long = snapshot.getValue(Long::class.java)!!
                val formatter = SimpleDateFormat("hh:mm aa")
                val stringtime = formatter.format(Date(time))
                holder.binding.lastmsgtime.setText(stringtime)
            }

            override fun onCancelled(error: DatabaseError)
            {
                TODO("Not yet implemented")
            }
        })
    }

    override fun getItemCount(): Int
    {
        return userids.size
    }

    fun setonitemclicklistener(clicklistener: Clicklistener)
    {
        AdminChatAdapter.clicklistener = clicklistener
    }

    interface Clicklistener {
        fun onitemclick(position : Int, v : View)
        fun onlongitemclick(position : Int , v : View)
    }

}