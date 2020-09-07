package com.zukron.note.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 8/9/2020
 */

@Entity(tableName = "note")
data class Note(
        val title: String,
        val type: Int,
        @ColumnInfo(name = "modified_date") val modifiedDate: String,
        @ColumnInfo(name = "created_date") val createdDate: String,
        val color: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    object Action {
        const val insert = "insert"
        const val update = "update"
    }

    object Type {
        const val defaultNote = 1
        const val listNote = 2
        const val longNote = 3
    }
}