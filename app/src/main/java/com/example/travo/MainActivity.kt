package com.example.travo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val locationFrag=LocationFrag()
    private val allPlaceFrag=AllPlaceFrag()
    private val profileFrag=ProfileFrag()
    private val chatFrag=ChatFrag()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(allPlaceFrag)

        val bottomNavigationView : BottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener {
            // do stuff
            when(it.itemId){
                R.id.dashboard->replaceFragment(allPlaceFrag)
                R.id.location->replaceFragment(locationFrag)
                R.id.chat->replaceFragment(chatFrag)
                R.id.profile->replaceFragment(profileFrag)

            }
            return@setOnItemSelectedListener true
        }

    }
    private fun replaceFragment(fragment: Fragment) {
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragContainer,fragment)
        transaction.commit()
    }
}