package com.zukron.note.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.zukron.note.R
import com.zukron.note.adapter.NoteListAdapter
import com.zukron.note.model.ListNote
import com.zukron.note.ui.viewmodel.NoteViewModel
import com.zukron.note.util.Utilities.toEditable
import kotlinx.android.synthetic.main.list_note_fragment.*
import kotlinx.android.synthetic.main.list_note_fragment.input_title

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 9/5/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class ListNoteFragment : Fragment() {
    companion object {
        const val TAG: String = "ListNoteFragment"
        private const val BUNDLE_NOTE_ID = "bundle_note_id"

        fun newInstance(id: Long): ListNoteFragment {
            val listNoteFragment = ListNoteFragment()
            val bundle = Bundle()
            bundle.putLong(BUNDLE_NOTE_ID, id)

            listNoteFragment.arguments = bundle
            return listNoteFragment
        }
    }

    private lateinit var noteViewModel: NoteViewModel
    private var _listNote: List<ListNote>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_note_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // note id from Note entity
        val noteId = arguments?.getLong(BUNDLE_NOTE_ID, 0) ?: 0

        // view model
        noteViewModel = ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory(activity!!.application))
                .get(NoteViewModel::class.java)

        noteViewModel.setNoteId(noteId)
        noteViewModel.listNote.observe(this) { listNote ->
            if (listNote.isNotEmpty()) {
                input_title.text = listNote[0].title.toEditable()

                val checkList: MutableList<Boolean> = listNote.map { it ->
                    it.check
                }.toMutableList()

                val itemList: MutableList<String> = listNote.map {
                    it.item
                }.toMutableList()

                val adapter = NoteListAdapter(context, checkList, itemList)
                rv_note.adapter = adapter

                _listNote = listNote
            } else {
                val checkList = mutableListOf<Boolean>()
                val itemList = mutableListOf<String>()
                checkList.add(false)
                itemList.add("")

                val noteListAdapter = NoteListAdapter(context, checkList, itemList)
                rv_note.adapter = noteListAdapter

                _listNote = listOf()
            }
        }
    }

    fun getNote(): List<ListNote> {
        val listNote: MutableList<ListNote> = mutableListOf()

        if (validate()) {
            val title = input_title.text.toString().trim()

            for (i in 0 until rv_note.childCount) {
                val view = rv_note.getChildAt(i)
                val checkBox: CheckBox = view.findViewById(R.id.cb_item)
                val input: TextInputEditText = view.findViewById(R.id.input_item)

                val temp = ListNote(checkBox.isChecked, input.text.toString().trim())
                temp.title = title
                temp.noteId = if (_listNote!!.isEmpty()) {
                    0
                } else {
                    _listNote!![0].noteId
                }

                listNote.add(temp)
            }
        } else {
            Toast.makeText(context, "Judul tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }

        return listNote
    }

    private fun validate(): Boolean {
        return when {
            input_title.text!!.isBlank() -> false
            else -> true
        }
    }
}