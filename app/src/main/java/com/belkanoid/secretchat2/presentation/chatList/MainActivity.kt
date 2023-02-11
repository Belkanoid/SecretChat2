package com.belkanoid.secretchat2.presentation.chatList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.belkanoid.secretchat2.R
import com.belkanoid.secretchat2.databinding.ActivityMainBinding
import com.belkanoid.secretchat2.presentation.ChatApplication
import com.belkanoid.secretchat2.presentation.chatList.viewModel.ChatListEvent
import com.belkanoid.secretchat2.presentation.chatList.viewModel.ChatListState
import com.belkanoid.secretchat2.presentation.chatList.viewModel.ChatListViewModel
import com.belkanoid.secretchat2.presentation.factory.ViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val component by lazy { (application as ChatApplication).component }
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding ?: throw RuntimeException("ActivityMainBinding iS null")

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ChatListViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        component.inject(this)
        handleSate()
    }

    private fun handleSate() {
        lifecycleScope.launch {
            viewModel.chatListState.collect{state->
                when(state) {
                    is ChatListState.Success -> {

                    }
                    is ChatListState.Error -> {

                    }
                    is ChatListState.OnSendMessage -> {

                    }
                    is ChatListState.OnCreateUser.NeedToCreateUser -> {
                        binding.tvUserName.text = "Need to create User"
                    }
                    is ChatListState.OnCreateUser.IsUserCreated -> {
                        binding.anotherTv.text = state.success.toString()
                        if (state.success) {
                            binding.tvUserName.text = "your id is ${viewModel.currentUserId}"
                        }
                    }
                    is ChatListState.Loading -> {

                    }

                    is ChatListState.Empty -> Unit
                }
            }
        }
    }
    private fun handleEvent(event: ChatListEvent) {
        binding.pushButton.setOnClickListener {
            viewModel.onChatListEvent(
                ChatListEvent.CreateNewUser(
                    binding.tvUserName.text.toString()
                )
            )
        }
    }
}