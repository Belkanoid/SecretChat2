package com.belkanoid.secretchat2.presentation.chatList.chatListRecycler


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.belkanoid.secretchat2.R
import com.belkanoid.secretchat2.domain.model.Message

class ChatListAdapter: ListAdapter<Message, ChatListHolder>(ChatListDiffUtil()) {

    val onOpenMessage: ((Message) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chat_list_item, parent, false)

        return ChatListHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatListHolder, position: Int) {
        val message = getItem(position)
        holder.onBind(message)
        holder.itemView.setOnClickListener {
            onOpenMessage?.invoke(message)
        }
    }
}