package com.zukron.note.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.zukron.note.R
import com.zukron.note.model.DefaultNote
import com.zukron.note.model.Note
import com.zukron.note.model.dao.DefaultNoteDaoJava
import com.zukron.note.util.Utilities.toEditable
import kotlinx.android.synthetic.main.default_note_fragment.*

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 9/5/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class LongNoteFragment : Fragment() {
    companion object {
        const val TAG: String = "LongNoteFragment"
        private const val BUNDLE_NOTE = "bundle_note"

        fun newInstance(note: Note): LongNoteFragment {
            val longNoteFragment = LongNoteFragment()
            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_NOTE, note)

            longNoteFragment.arguments = bundle
            return longNoteFragment
        }
    }

    // 0 if user want to insert
    private var idNote: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.default_note_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val defaultNoteDaoJava = DefaultNoteDaoJava(context)
        // if user want to update
        if (arguments != null) {
            val note: Note? = arguments!!.getParcelable(BUNDLE_NOTE)

            if (note != null) {
                val defaultNote = defaultNoteDaoJava.get(note.id)

                idNote = defaultNote.id
                input_title.text = defaultNote.title.toEditable()
                input_body.text = defaultNote.body.toEditable()
            }
        }
    }

    fun getNote(): DefaultNote? {
        var defaultNote: DefaultNote? = null

        if (validate()) {
            val title = input_title.text.toString().trim()
            val body = input_body.text.toString().trim()

            defaultNote = DefaultNote(idNote, title, body)
        } else {
            Toast.makeText(context, "Judul atau isi tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }

        return defaultNote
    }

    private fun validate(): Boolean {
        return when {
            input_title.text!!.isBlank() -> false
            input_body.text!!.isBlank() -> false
            else -> true
        }
    }
}