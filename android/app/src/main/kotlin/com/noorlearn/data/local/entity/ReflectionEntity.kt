package com.noorlearn.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.noorlearn.domain.model.Reflection

@Entity(tableName = "reflections")
data class ReflectionEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val content: String,
    val date: String,
    val linkedAyahId: Int?
) {
    fun toDomain() = Reflection(
        id = id,
        userId = userId,
        content = content,
        date = date,
        linkedAyahId = linkedAyahId
    )

    companion object {
        fun fromDomain(reflection: Reflection) = ReflectionEntity(
            id = reflection.id,
            userId = reflection.userId,
            content = reflection.content,
            date = reflection.date,
            linkedAyahId = reflection.linkedAyahId
        )
    }
}
