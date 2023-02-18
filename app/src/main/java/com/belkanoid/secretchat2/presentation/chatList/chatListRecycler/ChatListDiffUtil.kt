package com.belkanoid.secretchat2.presentation.chatList.chatListRecycler

import com.belkanoid.secretchat2.domain.model.Message
import androidx.recyclerview.widget.DiffUtil

class ChatListDiffUtil: DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}