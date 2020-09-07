package com.zukron.note.ui.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.jakewharton.threetenabp.AndroidThreeTen
import com.zukron.note.ui.fragment.DefaultNoteFragment
import com.zukron.note.ui.fragment.ListNoteFragment
import com.zukron.note.ui.fragment.LongNoteFragment
import com.zukron.note.model.DefaultNote
import com.zukron.note.model.ListNote
import com.zukron.note.model.Note
import com.zukron.note.repository.NoteRepository
import org.threeten.bp.LocalDate

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 9/5/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class DetailViewModel(private var app: Application) : AndroidViewModel(app) {
    private val repository = NoteRepository.getInstance(app)
    private val _noteId: MutableLiveData<Long> = MutableLiveData()

    val noteDetail: LiveData<Note> = Transformations
            .switchMap(_noteId) {
                repository.getNote(it)
            }

    val fragment: MutableLiveData<Fragment> = MutableLiveData()
    val noteFragment: LiveData<Fragment> = Transformations
            .switchMap(noteDetail) {
                var fragment1: Fragment? = null

                if (it != null) {
                    if (it.type == Note.Type.defaultNote) {
                        fragment1 = DefaultNoteFragment.newInstance(it.id)
                    }

                    if (it.type == Note.Type.listNote) {
                        fragment1 = ListNoteFragment.newInstance(it.id)
                    }

                    if (it.type == Note.Type.longNote) {
                        fragment1 = LongNoteFragment.newInstance(it.id)
                    }
                } else {
                    fragment1 = DefaultNoteFragment()
                }

                fragment.value = fragment1
                fragment
            }

    fun setFragment(tag: String) {
        var fragment1: Fragment? = null

        if (tag == DefaultNoteFragment.TAG) {
            fragment1 = DefaultNoteFragment()
        }

        if (tag == ListNoteFragment.TAG) {
            fragment1 = ListNoteFragment()
        }

        if (tag == LongNoteFragment.TAG) {
            fragment1 = LongNoteFragment()
        }

        fragment.value = fragment1
    }

    fun setNoteId(id: Long) {
        if (_noteId.value == id) {
            return
        }
        _noteId.value = id
    }

    fun insertUpdateDefaultNote(defaultNote: DefaultNote, currentColor: Int, action: String) {
        AndroidThreeTen.init(app)

        val note = Note(
                defaultNote.title,
                Note.Type.defaultNote,
                LocalDate.now().toString(),
                LocalDate.now().toString(),
                currentColor
        )

        if (action == Note.Action.insert) repository.insertDefaultNote(note, defaultNote)
        if (action == Note.Action.update) {
            note.id = defaultNote.noteId
            repository.updateDefaultNote(note, defaultNote)
        }
    }

    fun insertUpdateListNote(listNote: List<ListNote>, currentColor: Int, action: String) {
        AndroidThreeTen.init(app)

        val note = Note(
                listNote[0].title,
                Note.Type.listNote,
                LocalDate.now().toString(),
                LocalDate.now().toString(),
                currentColor
        )

        if (action == Note.Action.insert) repository.insertListNote(note, listNote)

        if (action == Note.Action.update) {
            note.id = listNote[0].noteId

            repository.deleteNote(note.id)
            repository.insertListNote(note, listNote)
        }
    }

    fun insertUpdateLongNote(defaultNote: DefaultNote, currentColor: Int, action: String) {
        AndroidThreeTen.init(app)

        val note = Note(
                defaultNote.title,
                Note.Type.longNote,
                LocalDate.now().toString(),
                LocalDate.now().toString(),
                currentColor
        )

        if (action == Note.Action.insert) repository.insertDefaultNote(note, defaultNote)
        if (action == Note.Action.update) {
            note.id = defaultNote.noteId
            repository.updateDefaultNote(note, defaultNote)
        }
    }

    fun deleteNote(id: Long) {
        repository.deleteNote(id)
    }
}