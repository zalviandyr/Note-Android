package com.zukron.note.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.zukron.note.model.NoteAndListNote

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 9/5/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
@Dao
interface NoteAndListNoteDao {
    @Transaction
    @Query("SELECT * FROM note ORDER BY note_id DESC")
    suspend fun getNotesAndListNotes(): List<NoteAndListNote>
}