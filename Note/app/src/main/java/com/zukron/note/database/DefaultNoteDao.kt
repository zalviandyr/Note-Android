package com.zukron.note.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.zukron.note.model.DefaultNote

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 9/5/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
@Dao
interface DefaultNoteDao {
    @Query("SELECT * FROM default_note WHERE note_id = :id")
    suspend fun get(id: Long): DefaultNote

    @Insert
    suspend fun insert(defaultNote: DefaultNote)

    @Update
    suspend fun update(defaultNote: DefaultNote)
}