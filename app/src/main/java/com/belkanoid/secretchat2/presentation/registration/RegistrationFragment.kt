package com.belkanoid.secretchat2.presentation.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.belkanoid.secretchat2.R
import com.belkanoid.secretchat2.databinding.FragmentChatListBinding
import com.belkanoid.secretchat2.databinding.FragmentRegistrationBinding
import com.belkanoid.secretchat2.presentation.ChatApplication
import com.belkanoid.secretchat2.presentation.factory.ViewModelFactory
import com.belkanoid.secretchat2.presentation.registration.viewModel.RegistrationViewModel
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

    companion object {

        fun newInstance() = RegistrationFragment()
    }
}