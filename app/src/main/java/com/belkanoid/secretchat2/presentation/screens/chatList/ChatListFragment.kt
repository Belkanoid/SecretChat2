package com.belkanoid.secretchat2.presentation.screens.chatList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.belkanoid.secretchat2.databinding.FragmentChatListBinding
import com.belkanoid.secretchat2.presentation.ChatApplication
import com.belkanoid.secretchat2.presentation.screens.chatList.chatListRecycler.ChatListAdapter
import com.belkanoid.secretchat2.presentation.screens.chatList.viewModel.ChatListState
import com.belkanoid.secretchat2.presentation.screens.chatList.viewModel.ChatListViewModel
import com.belkanoid.secretchat2.presentation.factory.ViewModelFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


class ChatListFragment : Fragment() {

    private var _binding: FragmentChatListBinding? = null
    private val binding: FragmentChatListBinding
        get() = _binding ?: throw RuntimeException("FragmentChatListBinding is null")

    private val component by lazy {
        (requireActivity().application as ChatApplication).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ChatListViewModel::class.java]
    }

    private lateinit var chatListAdapter: ChatListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatListBinding.inflate(inflater, container, false)
        component.inject(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.apply {
            chatListAdapter = ChatListAdapter()
            adapter = chatListAdapter
        }
        viewModel.chatListState
            .onEach { state -> handleState(state) }.
            launchIn(lifecycleScope)

    }

    private fun handleState(state: ChatListState) {
        with(binding) {
            when (state) {
                is ChatListState.Data -> {
                    val newMessages = state.data.toMutableList()
                    val oldMessagesSize = chatListAdapter.currentList.size
                    chatListAdapter.submitList(newMessages)
                    if (oldMessagesSize != newMessages.size) {
                        binding.recyclerView.smoothScrollToPosition(0)
                    }
                }

                is ChatListState.Loading -> {
                    binding.chatListProgressBar.visibility = View.VISIBLE
                }

                is ChatListState.Error -> {
                }

                is ChatListState.Empty -> Unit
            }
        }

    }

    companion object {
        fun newInstance() = ChatListFragment()
    }
}