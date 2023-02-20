package com.bsr.bsrcoin.Adapter

import android.opengl.Visibility
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bsr.bsrcoin.SharedPrefmanager
import com.bsr.bsrcoin.ViewModels.MessageModel
import com.bsr.bsrcoin.databinding.ActivityReceiverChatBinding
import com.bsr.bsrcoin.databinding.ActivitySenderChatBinding
import kotlinx.coroutines.NonDisposableHandle.parent
import java.lang.StringBuilder
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class ChatAdapter(private val messages: MutableList<MessageModel>?, private val instance: SharedPrefmanager) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    private val SENDER_TYPE : Int = 0
    private val RECEIVER_TYPE : Int = 1
    companion object listener
    {
        private lateinit var clicklistener : Clicklistener
    }

    private class SenderViewHolder(var binding: ActivitySenderChatBinding) : RecyclerView.ViewHolder(
        binding.root
    ),View.OnClickListener,View.OnLongClickListener
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

    private class ReceiverViewHolder(var binding: ActivityReceiverChatBinding) : RecyclerView.ViewHolder(binding.root),View.OnClickListener,View.OnLongClickListener {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        if(viewType==SENDER_TYPE)
        {
            val binding : ActivitySenderChatBinding = ActivitySenderChatBinding.inflate(
                LayoutInflater.from(parent.context),parent,false)
            return SenderViewHolder(binding)
        }
        else
        {
            val binding : ActivityReceiverChatBinding = ActivityReceiverChatBinding.inflate(
                LayoutInflater.from(parent.context),parent,false)
            return ReceiverViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        val message : MessageModel? = messages?.get(position)
        val formatter = SimpleDateFormat("hh:mm aa")
        if(holder::class==SenderViewHolder::class)
        {
            if (message != null) {
                (holder as SenderViewHolder).binding.sentmsg.setText(message.message)
                val time = formatter.format(Date(message.time))
                holder.binding.time.setText(time)
                if(instance.isAgent) holder.binding.sentby.setText("Sent by : " + message.sentby)
                else holder.binding.sentby.visibility = View.GONE
            }
        }
        if(holder::class==ReceiverViewHolder::class)
        {
            if (message != null) {
                (holder as ReceiverViewHolder).binding.receivedmsg.setText(message.message)
                val time = formatter.format(Date(message.time))
                holder.binding.time.setText(time)
            }
        }
    }

    override fun getItemCount(): Int
    {
        return messages?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int
    {
        super.getItemViewType(position)
        val nullrid = (messages?.get(position)?.receiverid.equals(""))
        if(nullrid)
        {
            if(instance.isAgent) return RECEIVER_TYPE
            else return SENDER_TYPE
        }
        else
        {
            if(instance.isAgent) return SENDER_TYPE
            else return RECEIVER_TYPE
        }
    }

    fun setonitemclicklistener(clicklistener: Clicklistener)
    {
        ChatAdapter.clicklistener = clicklistener
    }

    interface Clicklistener
    {
        fun onitemclick(position : Int, v : View)
        fun onlongitemclick(position : Int , v : View)
    }
}