package com.belkanoid.secretchat2.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.belkanoid.secretchat2.R
import com.belkanoid.secretchat2.databinding.ActivityMainBinding
import com.belkanoid.secretchat2.presentation.chatList.ChatListFragment
import com.belkanoid.secretchat2.presentation.chatList.viewModel.ChatListState
import com.belkanoid.secretchat2.presentation.chatList.viewModel.ChatListViewModel
import com.belkanoid.secretchat2.presentation.factory.ViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, ChatListFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }


}