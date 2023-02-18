package com.belkanoid.secretchat2.presentation.chatList

import android.os.Bundle
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.belkanoid.secretchat2.R
import com.belkanoid.secretchat2.databinding.FragmentChatListBinding
import com.belkanoid.secretchat2.presentation.ChatApplication
import com.belkanoid.secretchat2.presentation.chatList.viewModel.ChatListState
import com.belkanoid.secretchat2.presentation.chatList.viewModel.ChatListViewModel
import com.belkanoid.secretchat2.presentation.factory.ViewModelFactory
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
        lifecycleScope.launchWhenStarted {
            viewModel.chatListState.collect { state ->
                handleState(state)
            }
        }
    }

    private fun handleState(state: ChatListState) {
        binding.chatListProgressBar.visibility = View.INVISIBLE
        when (state) {
            is ChatListState.Data<*> -> {
                Log.d("ABDC", state.data.toString())
            }
            is ChatListState.Loading -> {
                binding.chatListProgressBar.visibility = View.VISIBLE
            }
            is ChatListState.Error -> {
            }
            is ChatListState.Empty -> Unit
        }

    }

    companion object {

        fun newInstance() = ChatListFragment()
    }
}