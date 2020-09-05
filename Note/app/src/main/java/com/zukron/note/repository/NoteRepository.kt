package com.zukron.note.repository

import android.content.Context
import androidx.room.Room
import com.zukron.note.database.NoteDatabase

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 9/5/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
object NoteRepository {
    private const val DB_NAME = "note_database"
    private var noteDatabase: NoteDatabase? = null

    fun getInstance(context: Context): NoteDatabase? {
        noteDatabase ?: Room.databaseBuilder(
                context,
                NoteDatabase::class.java,
                DB_NAME
        ).also { noteDatabase = it.build() }

        return noteDatabase!!
    }
}