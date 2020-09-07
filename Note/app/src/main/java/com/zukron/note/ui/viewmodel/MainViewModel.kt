package com.zukron.note.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.zukron.note.model.MixNote
import com.zukron.note.repository.NoteRepository

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 9/5/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NoteRepository.getInstance(application)

    val mixNote: LiveData<MixNote> = repository.getMixNote()
}