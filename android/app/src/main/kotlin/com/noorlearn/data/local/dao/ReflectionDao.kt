package com.noorlearn.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.noorlearn.data.local.entity.ReflectionEntity

@Dao
interface ReflectionDao {
    @Query("SELECT * FROM reflections WHERE userId = :userId ORDER BY date DESC")
    suspend fun getReflectionsForUser(userId: String): List<ReflectionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReflection(reflection: ReflectionEntity)

    @Query("DELETE FROM reflections WHERE id = :id AND userId = :userId")
    suspend fun deleteReflection(id: String, userId: String)
}
