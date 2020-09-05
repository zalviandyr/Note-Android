package com.zukron.note.database

import androidx.room.*
import com.zukron.note.model.ListNote

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 9/5/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
@Dao
interface ListNoteDao {
    @Query("SELECT * FROM list_note WHERE list_note_id = :id ")
    suspend fun get(id: Int): ListNote

    @Insert
    suspend fun insert(listNote: ListNote)

    @Update
    suspend fun update(listNote: ListNote)

    @Query("DELETE FROM list_note WHERE list_note_id = :id")
    suspend fun delete(id: Int)
}