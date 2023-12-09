package com.belkanoid.secretchat2.presentation.screens.chatList.chatListRecycler

import androidx.recyclerview.widget.DiffUtil
import com.belkanoid.secretchat2.domain.model.Message

class ChatListDiffUtil: DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}