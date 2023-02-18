package com.belkanoid.secretchat2.presentation.chatList.chatListRecycler

import com.belkanoid.secretchat2.domain.model.Message
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.belkanoid.secretchat2.R
import java.text.SimpleDateFormat
import java.util.*

class ChatListHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val messageSenderName = itemView.findViewById<TextView>(R.id.chatListItemName)
    private val messageText = itemView.findViewById<TextView>(R.id.chatListItemCroppedMessage)
    private val messageDate= itemView.findViewById<TextView>(R.id.chatListItemDate)

    private val formatter = SimpleDateFormat("dd MMM", Locale.US);

    fun onBind(message: Message) {
        messageSenderName.text = message.sender.toString()
        messageText.text = message.message
        messageDate.text = formatter.format(Date(message.timestamp))
    }
}