package com.zukron.note.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.zukron.note.R
import com.zukron.note.adapter.NoteListAdapter
import com.zukron.note.model.ListNote
import com.zukron.note.model.Note
import com.zukron.note.model.dao.ListNoteDaoJava
import com.zukron.note.util.Utilities.toEditable
import kotlinx.android.synthetic.main.list_note_fragment.*

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 9/5/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class ListNoteFragment : Fragment() {
    companion object {
        const val TAG: String = "ListNoteFragment"
        private const val BUNDLE_NOTE = "bundle_note"

        fun newInstance(note: Note): ListNoteFragment {
            val listNoteFragment = ListNoteFragment()
            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_NOTE, note)

            listNoteFragment.arguments = bundle
            return listNoteFragment
        }
    }

    // 0 if user want to insert
    private var idNote: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_note_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listNoteDaoJava = ListNoteDaoJava(context)
        // if user want to update
        if (arguments != null) {
            val note: Note? = arguments!!.getParcelable(BUNDLE_NOTE)

            if (note != null) {
                val listNote = listNoteDaoJava.get(note.id)
                idNote = note.id
                input_title.text = listNote.title.toEditable()

                val noteListAdapter = NoteListAdapter(context, listNote.checkList, listNote.itemList)
                rv_note.adapter = noteListAdapter
            }
        }
        // if user want to insert
        else {
            // init index 0
            val checkList = mutableListOf<Boolean>()
            val itemList = mutableListOf<String>()
            checkList.add(false)
            itemList.add("")

            val noteListAdapter = NoteListAdapter(context, checkList, itemList)
            rv_note.adapter = noteListAdapter
        }
    }

    fun getNote(): ListNote? {
        var listNote: ListNote? = null

        if (validate()) {
            val title = input_title.text.toString().trim()
            val checkList = mutableListOf<Boolean>()
            val itemList = mutableListOf<String>()

            for (i in 0 until rv_note.childCount) {
                val view = rv_note.getChildAt(i)
                val checkBox: CheckBox = view.findViewById(R.id.cb_item)
                val input: TextInputEditText = view.findViewById(R.id.input_item)

                checkList.add(checkBox.isChecked)
                itemList.add(input.text.toString().trim())
            }

            listNote = ListNote(idNote, title, checkList, itemList)
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