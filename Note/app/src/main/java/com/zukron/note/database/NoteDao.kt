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
    @Query("SELECT * FROM note ORDER BY note_id DESC")
    suspend fun getAll(): List<Note>

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Query("DELETE FROM note WHERE note_id = :id")
    suspend fun delete(id: Int)
}