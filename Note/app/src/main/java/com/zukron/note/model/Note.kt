package com.zukron.note.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 8/9/2020
 */

@Parcelize
@Entity
data class Note(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "note_id") val id: Int,
        val title: String,
        val type: Int,
        @ColumnInfo(name = "modified_date") val modifiedDate: String,
        @ColumnInfo(name = "created_date") val createdDate: String,
        val color: Int
) : Parcelable {

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