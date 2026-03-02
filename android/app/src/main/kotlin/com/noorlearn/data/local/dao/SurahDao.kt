package com.noorlearn.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.noorlearn.data.local.entity.SurahEntity

@Dao
interface SurahDao {
    @Query("SELECT * FROM surahs ORDER BY id ASC")
    suspend fun getAllSurahs(): List<SurahEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurahs(surahs: List<SurahEntity>)
}
