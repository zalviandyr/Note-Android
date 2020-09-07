package com.zukron.note.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zukron.note.model.*

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 9/5/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
@Database(entities = [
    Note::class,
    DefaultNote::class,
    ListNote::class,
], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun defaultNoteDao(): DefaultNoteDao
    abstract fun listNoteDao(): ListNoteDao
}