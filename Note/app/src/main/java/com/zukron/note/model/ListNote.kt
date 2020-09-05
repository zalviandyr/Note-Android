package com.zukron.note.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 8/8/2020
 */
@Entity(tableName = "list_note")
data class ListNote(
        @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "list_note_id") val id: Int,
        val title: String,
        @ColumnInfo(name = "check_list") val checkList: MutableList<Boolean>,
        @ColumnInfo(name = "item_list") val itemList: MutableList<String>
)
