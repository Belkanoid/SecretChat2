package com.belkanoid.secretchat2.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.belkanoid.secretchat2.R
import com.belkanoid.secretchat2.domain.util.SharedPreferences
import com.belkanoid.secretchat2.presentation.screens.chatList.ChatListFragment
import com.belkanoid.secretchat2.presentation.screens.registration.RegistrationFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {


    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val component by lazy {
        (application as ChatApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        component.inject(this)
        if (savedInstanceState == null) {
            val fragment = if (sharedPreferences.getLong() == -1L) {
                RegistrationFragment.newInstance()
            } else {
                ChatListFragment.newInstance()
            }

            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }


}