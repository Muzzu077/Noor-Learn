package com.noorlearn.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.noorlearn.data.local.dao.AyahDao
import com.noorlearn.data.local.dao.SurahDao
import com.noorlearn.data.local.entity.AyahEntity
import com.noorlearn.data.local.entity.SurahEntity

@Database(
    entities = [
        SurahEntity::class,
        AyahEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NoorLearnDatabase : RoomDatabase() {
    abstract val surahDao: SurahDao
    abstract val ayahDao: AyahDao
}
