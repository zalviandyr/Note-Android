package com.zukron.note.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.zukron.note.model.DefaultNote
import com.zukron.note.model.Note

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 9/5/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
@Entity
data class NoteAndDefaultNote(
        @Embedded val note: Note,
        @Relation(
                parentColumn = "note_id",
                entityColumn = "default_note_id"
        ) val defaultNote: DefaultNote
)