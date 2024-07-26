package com.db.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageRVAdapter(
    private val messageModalArrayList: ArrayList<MessageModel>,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return when (viewType) {
            0 -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.user_msg, parent, false)
                UserViewHolder(view)
            }
            1 -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.bot_msg, parent, false)
                BotViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val modal = messageModalArrayList[position]
        when (modal.sender) {
            "user" -> (holder as UserViewHolder).userTV.text = modal.message
            "bot" -> (holder as BotViewHolder).botTV.text = modal.message
        }
    }

    override fun getItemCount(): Int {
        return messageModalArrayList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (messageModalArrayList[position].sender) {
            "user" -> 0
            "bot" -> 1
            else -> -1
        }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userTV: TextView = itemView.findViewById(R.id.idTVUser)
    }

    class BotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val botTV: TextView = itemView.findViewById(R.id.idTVBot)
    }
}
