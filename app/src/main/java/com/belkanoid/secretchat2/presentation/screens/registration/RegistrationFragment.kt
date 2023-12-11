package com.belkanoid.secretchat2.presentation.screens.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.belkanoid.secretchat2.R
import com.belkanoid.secretchat2.databinding.FragmentRegistrationBinding
import com.belkanoid.secretchat2.presentation.ChatApplication
import com.belkanoid.secretchat2.presentation.factory.ViewModelFactory
import com.belkanoid.secretchat2.presentation.screens.chatList.ChatListFragment
import com.belkanoid.secretchat2.presentation.screens.registration.viewModel.RegistrationState
import com.belkanoid.secretchat2.presentation.screens.registration.viewModel.RegistrationViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding: FragmentRegistrationBinding
        get() = _binding ?: throw RuntimeException("FragmentRegistrationBinding is null")

    private val component by lazy {
        (requireActivity().application as ChatApplication).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[RegistrationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        component.inject(this@RegistrationFragment)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegistration.setOnClickListener {
            val nickname = binding.inputText.text.toString()
            viewModel.createUser(nickname)
        }

        viewModel.registrationSate
            .flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
            .onEach { state -> handleState(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: RegistrationState) {
        when(state) {
            RegistrationState.Empty -> Unit
            is RegistrationState.Error -> binding.inputLayout.error = state.message
            RegistrationState.Loading -> {}
            is RegistrationState.Success -> {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, ChatListFragment.newInstance())
                    .commit()
            }
        }
    }

    companion object {

        fun newInstance() = RegistrationFragment()
    }
}