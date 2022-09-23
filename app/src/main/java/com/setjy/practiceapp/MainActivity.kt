package com.setjy.practiceapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.data.Data
import com.setjy.practiceapp.databinding.ActivityMainBinding
import com.setjy.practiceapp.util.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding()

    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavView.isVisible = destination.id != R.id.topicFragment
        }
        registerEventQueue()
    }

    //    override fun onDestroy() {
//        super.onDestroy()
//        disposable.dispose()
//    }
    override fun onStop() {
        super.onStop()
        disposable.dispose()
    }

    private fun registerEventQueue() {
        disposable += Data.registerEventQueue()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t -> Log.d("xxx", "register success: $t") },
                { e -> Log.d("xxx", "register error: $e") })
    }
}


