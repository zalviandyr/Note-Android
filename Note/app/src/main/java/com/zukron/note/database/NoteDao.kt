package com.zukron.note.database

import androidx.room.*
import com.zukron.note.model.Note

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 9/5/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
@Dao
interface NoteDao {
    @Query("SELECT * FROM note ORDER BY id DESC")
    suspend fun getAllNotes(): List<Note>

    @Query("SELECT * FROM note WHERE type IN (${Note.Type.defaultNote}, ${Note.Type.longNote}) ORDER BY id DESC")
    suspend fun getAllDefaultNotes(): List<Note>

    @Query("SELECT * FROM note WHERE type = ${Note.Type.listNote} ORDER BY id DESC")
    suspend fun getAllListNotes(): List<Note>

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNote(id: Long): Note

    @Insert
    suspend fun insert(note: Note): Long

    @Update
    suspend fun update(note: Note)

    @Query("DELETE FROM note WHERE id = :id")
    suspend fun delete(id: Long)
}