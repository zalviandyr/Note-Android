package com.zukron.note.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 8/7/2020
 */
@Entity(tableName = "default_note")
data class DefaultNote(
        @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "default_note_id") val id: Int,
        val title: String,
        val body: String
)