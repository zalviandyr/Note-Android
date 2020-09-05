package com.zukron.note.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 9/5/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
@Entity
data class NoteAndListNote(
        @Embedded val note: Note,
        @Relation(
                parentColumn = "note_id",
                entityColumn = "list_note_id"
        ) val listNote: ListNote
)