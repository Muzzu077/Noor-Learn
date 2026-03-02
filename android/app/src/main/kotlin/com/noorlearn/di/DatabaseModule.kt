package com.noorlearn.di

import android.content.Context
import androidx.room.Room
import com.noorlearn.data.local.NoorLearnDatabase
import com.noorlearn.data.local.dao.AyahDao
import com.noorlearn.data.local.dao.SurahDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext context: Context
    ): NoorLearnDatabase {
        return Room.databaseBuilder(
            context,
            NoorLearnDatabase::class.java,
            "noorlearn_db"
        ).build()
    }

    @Provides
    fun provideSurahDao(db: NoorLearnDatabase): SurahDao = db.surahDao

    @Provides
    fun provideAyahDao(db: NoorLearnDatabase): AyahDao = db.ayahDao
}
