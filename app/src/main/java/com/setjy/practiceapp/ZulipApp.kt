package com.setjy.practiceapp

import android.app.Application
import android.util.Log
import com.setjy.practiceapp.data.Data
import com.setjy.practiceapp.data.database.ZulipRepo
import com.setjy.practiceapp.util.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ZulipApp : Application() {

    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()
        ZulipRepo.initialize(this)
        getOwnUser()
        registerEventQueue()
    }

    override fun onTerminate() {
        super.onTerminate()
        disposable.dispose()
    }

    private fun registerEventQueue() {
        disposable += Data.registerEventQueue()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t -> Log.d("xxx", "register success: $t") },
                { e -> Log.d("xxx", "register error: $e") })
    }

    private fun getOwnUser() {
        disposable += Data.getOwnUserFromDatabase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterSuccess { users ->
                if (users.isEmpty()) {
                    disposable += Data.getOwnUser()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doAfterSuccess { user ->
                            disposable += Data.insertUser(user)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe()
                        }
                        .subscribe()
                }
            }
            .subscribe()
    }

}