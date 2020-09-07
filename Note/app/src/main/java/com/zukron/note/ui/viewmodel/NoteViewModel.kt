package com.zukron.note.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.zukron.note.model.DefaultNote
import com.zukron.note.model.ListNote
import com.zukron.note.repository.NoteRepository

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 9/5/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NoteRepository.getInstance(application)
    private val _noteId: MutableLiveData<Long> = MutableLiveData()

    val defaultNote: LiveData<DefaultNote> = Transformations
            .switchMap(_noteId) {
                repository.getDefaultNote(it)
            }

    val listNote: LiveData<List<ListNote>> = Transformations
            .switchMap(_noteId) {
                repository.getListNote(it)
            }

    fun setNoteId(id: Long) {
        if (_noteId.value == id) {
            return
        }
        _noteId.value = id
    }
}