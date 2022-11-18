package com.setjy.practiceapp.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.R
import com.setjy.practiceapp.ZulipApp
import com.setjy.practiceapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)
        if (savedInstanceState == null) {
            navController.addOnDestinationChangedListener { _, destination, _ ->
                with(this.applicationContext as ZulipApp) {
                    if (destination.id == R.id.topicFragment) {
                        binding.bottomNavView.isVisible = false
                    } else {
                        binding.bottomNavView.isVisible = true
                        clearTopicComponent()
                    }
                    when (destination.id) {
                        R.id.channels_fragment -> {
                            clearPeopleComponent()
                            clearProfileComponent()
                        }
                        R.id.people_fragment -> {
                            clearChannelsComponent()
                            clearProfileComponent()
                        }
                        R.id.profile_fragment -> {
                            clearChannelsComponent()
                            clearPeopleComponent()
                        }
                    }
                }
            }
        }
    }
}