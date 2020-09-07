package com.zukron.note.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 8/7/2020
 */
@Entity(
        tableName = "default_note",
        foreignKeys = [ForeignKey(
                entity = Note::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("note_id"),
                onUpdate = CASCADE,
                onDelete = CASCADE
        )],
        indices = [Index(value = ["note_id"], unique = true)]
)
data class DefaultNote(
        var body: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var title: String = ""

    @ColumnInfo(name = "note_id")
    var noteId: Long = 0
}