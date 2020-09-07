package com.zukron.note.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.zukron.note.database.NoteDatabase
import com.zukron.note.model.DefaultNote
import com.zukron.note.model.ListNote
import com.zukron.note.model.MixNote
import com.zukron.note.model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 9/5/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class NoteRepository(context: Context) {
    companion object {
        private const val DB_NAME = "note_database"

        @Volatile
        private var INSTANCE: NoteRepository? = null

        @Synchronized
        fun getInstance(context: Context): NoteRepository {
            INSTANCE ?: NoteRepository(context).also { INSTANCE = it }
            return INSTANCE!!
        }
    }

    private val noteDatabase: NoteDatabase by lazy {
        Room.databaseBuilder(
                context,
                NoteDatabase::class.java,
                DB_NAME
        ).build()
    }

    fun getMixNote(): MutableLiveData<MixNote> {
        return object : MutableLiveData<MixNote>() {
            override fun onActive() {
                super.onActive()

                CoroutineScope(IO).launch {
                    val allNote = withContext(Default) {
                        val note = mutableListOf<Note>()
                        val noteTemp = noteDatabase.noteDao().getAllNotes()

                        for (i in noteTemp.indices) {
                            note.add(noteTemp[i])
                        }

                        note
                    }

                    val allDefaultNote = withContext(Default) {
                        val note = mutableListOf<DefaultNote>()
                        val defaultNote = noteDatabase.noteDao().getAllDefaultNotes()

                        for (i in defaultNote.indices) {
                            val id = defaultNote[i].id
                            note.add(noteDatabase.defaultNoteDao().get(id))
                        }

                        note
                    }

                    val allListNote = withContext(Default) {
                        val note = mutableListOf<List<ListNote>>()
                        val listNote = noteDatabase.noteDao().getAllListNotes()

                        for (i in listNote.indices) {
                            val id = listNote[i].id
                            note.add(noteDatabase.listNoteDao().getAll(id))
                        }

                        note
                    }

                    withContext(Main) {
                        val mixNote = MixNote(allNote, allDefaultNote, allListNote)
                        value = mixNote
                    }
                }
            }
        }
    }

    fun getNote(id: Long): LiveData<Note> {
        return object : LiveData<Note>() {
            override fun onActive() {
                super.onActive()

                CoroutineScope(IO).launch {
                    val note = noteDatabase.noteDao().getNote(id)

                    withContext(Main) {
                        value = note
                    }
                }
            }
        }
    }

    fun getDefaultNote(id: Long): LiveData<DefaultNote> {
        return object : LiveData<DefaultNote>() {
            override fun onActive() {
                super.onActive()

                CoroutineScope(IO).launch {
                    val note = noteDatabase.defaultNoteDao().get(id)

                    withContext(Main) {
                        value = note
                    }
                }
            }
        }
    }

    fun getListNote(id: Long): LiveData<List<ListNote>> {
        return object : LiveData<List<ListNote>>() {
            override fun onActive() {
                super.onActive()

                CoroutineScope(IO).launch {
                    val note = noteDatabase.listNoteDao().getAll(id)

                    withContext(Main) {
                        value = note
                    }
                }
            }
        }
    }

    fun insertDefaultNote(note: Note, defaultNote: DefaultNote) {
        CoroutineScope(IO).launch {
            val id = noteDatabase.noteDao().insert(note)
            defaultNote.noteId = id
            defaultNote.title = note.title
            noteDatabase.defaultNoteDao().insert(defaultNote)
        }
    }

    fun insertListNote(note: Note, listNote: List<ListNote>) {
        CoroutineScope(IO).launch {
            val id = noteDatabase.noteDao().insert(note)
            listNote.map {
                it.noteId = id
                it.title = note.title
            }
            noteDatabase.listNoteDao().insert(listNote)
        }
    }

    fun updateDefaultNote(note: Note, defaultNote: DefaultNote) {
        CoroutineScope(IO).launch {
            noteDatabase.noteDao().update(note)
            noteDatabase.defaultNoteDao().update(defaultNote)
        }
    }

    fun deleteNote(id: Long) {
        CoroutineScope(IO).launch {
            noteDatabase.noteDao().delete(id)
        }
    }
}
