package com.noorlearn.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.noorlearn.data.local.dao.AyahDao
import com.noorlearn.data.local.dao.SurahDao
import com.noorlearn.data.local.dao.ReflectionDao
import com.noorlearn.data.local.entity.AyahEntity
import com.noorlearn.data.local.entity.SurahEntity
import com.noorlearn.data.local.entity.ReflectionEntity

@Database(
    entities = [
        SurahEntity::class,
        AyahEntity::class,
        ReflectionEntity::class
    ],
    version = 7,
    exportSchema = false
)
abstract class NoorLearnDatabase : RoomDatabase() {
    abstract val surahDao: SurahDao
    abstract val ayahDao: AyahDao
    abstract val reflectionDao: ReflectionDao
}
