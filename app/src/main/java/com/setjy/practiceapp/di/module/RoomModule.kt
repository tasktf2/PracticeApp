package com.setjy.practiceapp.di.module

import android.content.Context
import androidx.room.Room
import com.setjy.practiceapp.data.local.db.ZulipDatabase
import com.setjy.practiceapp.di.scope.AppScope
import dagger.Module
import dagger.Provides

@Module
class RoomModule {

    @Provides
    @AppScope
    fun provideDatabase(context: Context): ZulipDatabase = Room.databaseBuilder(
        context.applicationContext,
        ZulipDatabase::class.java,
        DATABASE_NAME
    ).build()

    companion object {
        private const val DATABASE_NAME = "zulip.db"
    }
}
