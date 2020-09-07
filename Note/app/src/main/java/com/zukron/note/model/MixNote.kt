package com.zukron.note.model

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 9/6/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
data class MixNote (
        val noteList: List<Note>?,
        val defaultNoteList: List<DefaultNote>?,
        val listNoteList: List<List<ListNote>>?
)