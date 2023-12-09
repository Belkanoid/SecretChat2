package com.belkanoid.secretchat2.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.belkanoid.secretchat2.R
import com.belkanoid.secretchat2.presentation.screens.chatList.ChatListFragment

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