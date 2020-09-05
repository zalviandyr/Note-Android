package com.zukron.note.activity

import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.jakewharton.threetenabp.AndroidThreeTen
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.OnClickListener
import com.orhanobut.dialogplus.ViewHolder
import com.zukron.note.R
import com.zukron.note.fragment.DefaultNoteFragment
import com.zukron.note.fragment.ListNoteFragment
import com.zukron.note.fragment.LongNoteFragment
import com.zukron.note.model.Note
import com.zukron.note.model.dao.DefaultNoteDaoJava
import com.zukron.note.model.dao.ListNoteDaoJava
import com.zukron.note.model.dao.NoteDaoJava
import com.zukron.note.util.Utilities.formatToString
import kotlinx.android.synthetic.main.activity_note_detail.*
import org.threeten.bp.LocalDate

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 9/4/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class DetailNoteActivity : AppCompatActivity(), OnClickListener, View.OnClickListener {
    companion object {
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_ACTION = "extra_action"
    }

    private lateinit var dialogPlus: DialogPlus
    private lateinit var defaultNoteFragment: DefaultNoteFragment
    private lateinit var listNoteFragment: ListNoteFragment
    private lateinit var longNoteFragment: LongNoteFragment
    private lateinit var noteDaoJava: NoteDaoJava
    private lateinit var defaultNoteDaoJava: DefaultNoteDaoJava
    private lateinit var listNoteDaoJava: ListNoteDaoJava
    private var currentColor: Int = R.color.cardBgOrange
    private val setBackgroundColor: (color: Int) -> Unit = {
        currentColor = it
        coordinator_layout.setBackgroundColor(
                ContextCompat.getColor(this, it)
        )

        // status bar and button save color
        val window = window
        window.statusBarColor = ContextCompat.getColor(
                this, it
        )
        btn_save.setTextColor(ContextCompat.getColor(
                this, it
        ))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        // build dialog plus
        dialogPlus = DialogPlus.newDialog(this)
                .setExpanded(true)
                .setGravity(Gravity.TOP)
                .setOnClickListener(this)
                .setContentHolder(ViewHolder(R.layout.more_menu))
                .create()

        // toolbar
        material_toolbar.title = ""
        setSupportActionBar(material_toolbar)

        // change background color and set text date modified
        val action = intent.getStringExtra(EXTRA_ACTION)
        val note = intent.getParcelableExtra<Note>(EXTRA_NOTE)
        note.let {
            if (action != null) {
                if (action == Note.Action.insert) {
                    tv_last_modified.text = ""
                    tv_created.text = ""
                } else if (action == Note.Action.update) {
                    if (it != null) {
                        // change background
                        currentColor = it.color

                        // text view
                        tv_last_modified.text = it.modifiedDate.formatToString()
                        tv_created.text = it.createdDate.formatToString()
                    }
                }
            }
        }
        setBackgroundColor(currentColor)

        // set note body
        setNoteBody()

        // init database
        noteDaoJava = NoteDaoJava(this)
        defaultNoteDaoJava = DefaultNoteDaoJava(this)
        listNoteDaoJava = ListNoteDaoJava(this)

        // button listener
        btn_save.setOnClickListener(this)
        btn_delete.setOnClickListener(this)
        btn_fav.setOnClickListener(this)
        btn_more.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    override fun onClick(dialog: DialogPlus?, view: View?) {
        when (view?.id) {
            R.id.btn_add_default_note -> changeNoteBody(DefaultNoteFragment.TAG)
            R.id.btn_add_list_note -> changeNoteBody(ListNoteFragment.TAG)
            R.id.btn_add_long_note -> changeNoteBody(LongNoteFragment.TAG)
            R.id.btn_pink_bg -> setBackgroundColor(R.color.cardBgPink)
            R.id.btn_blue_bg -> setBackgroundColor(R.color.cardBgBlue)
            R.id.btn_violet_bg -> setBackgroundColor(R.color.cardBgViolet)
            R.id.btn_orange_bg -> setBackgroundColor(R.color.cardBgOrange)
        }

        dialog?.dismiss()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_save -> insertUpdateAction()
            R.id.btn_delete -> deleteAction()
            R.id.btn_fav -> favAction()
            R.id.btn_more -> dialogPlus.show()
        }
    }

    private fun setNoteBody() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        // note data
        val action = intent.getStringExtra(EXTRA_ACTION)
        val note = intent.getParcelableExtra<Note>(EXTRA_NOTE)

        if (action != null) {
            // action = insert
            if (action == Note.Action.insert) {
                defaultNoteFragment = DefaultNoteFragment()
                fragmentTransaction.add(R.id.fl_note, defaultNoteFragment, DefaultNoteFragment.TAG)
            }

            // action = update
            if (action == Note.Action.update) {
                if (note != null) {
                    if (note.type == Note.Type.defaultNote) {
                        defaultNoteFragment = DefaultNoteFragment.newInstance(note)
                        fragmentTransaction.add(R.id.fl_note, defaultNoteFragment, DefaultNoteFragment.TAG)
                    }

                    if (note.type == Note.Type.listNote) {
                        listNoteFragment = ListNoteFragment.newInstance(note)
                        fragmentTransaction.add(R.id.fl_note, listNoteFragment, ListNoteFragment.TAG)
                    }

                    if (note.type == Note.Type.longNote) {
                        longNoteFragment = LongNoteFragment.newInstance(note)
                        fragmentTransaction.add(R.id.fl_note, longNoteFragment, LongNoteFragment.TAG)
                    }
                }
            }

            fragmentTransaction.commit()
        }
    }

    private fun changeNoteBody(tag: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        if (tag == DefaultNoteFragment.TAG) {
            defaultNoteFragment = DefaultNoteFragment()
            fragmentTransaction.replace(R.id.fl_note, defaultNoteFragment, DefaultNoteFragment.TAG)
        }

        if (tag == ListNoteFragment.TAG) {
            listNoteFragment = ListNoteFragment()
            fragmentTransaction.replace(R.id.fl_note, listNoteFragment, ListNoteFragment.TAG)
        }

        if (tag == LongNoteFragment.TAG) {
            longNoteFragment = LongNoteFragment()
            fragmentTransaction.replace(R.id.fl_note, longNoteFragment, LongNoteFragment.TAG)
        }

        fragmentTransaction.commit()
    }

    private fun insertUpdateAction() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fl_note)
        val action = intent.getStringExtra(EXTRA_ACTION)
        AndroidThreeTen.init(this)

        if (fragment is DefaultNoteFragment) {
            val defaultNote = defaultNoteFragment.getNote()

            if (defaultNote != null && action != null) {
                val note = Note(
                        id = defaultNote.id,
                        title = defaultNote.title,
                        type = Note.Type.defaultNote,
                        modifiedDate = LocalDate.now(),
                        createdDate = LocalDate.now(),
                        color = currentColor
                )

                if (action == Note.Action.insert) {
                    noteDaoJava.insert(note)
                    defaultNoteDaoJava.insert(defaultNote)
                }

                if (action == Note.Action.update) {
                    noteDaoJava.update(note)
                    defaultNoteDaoJava.update(defaultNote)
                }

                finish()
            }
        }

        if (fragment is ListNoteFragment) {
            val listNote = listNoteFragment.getNote()

            if (listNote != null && action != null) {
                val note = Note(
                        id = listNote.id,
                        title = listNote.title,
                        type = Note.Type.listNote,
                        modifiedDate = LocalDate.now(),
                        createdDate = LocalDate.now(),
                        color = currentColor
                )

                if (action == Note.Action.insert) {
                    noteDaoJava.insert(note)
                    listNoteDaoJava.insert(listNote)
                }

                if (action == Note.Action.update) {
                    noteDaoJava.update(note)
                    listNoteDaoJava.update(listNote)
                }

                finish()
            }
        }

        if (fragment is LongNoteFragment) {
            val defaultNote = longNoteFragment.getNote()

            if (defaultNote != null && action != null) {
                val note = Note(
                        id = defaultNote.id,
                        title = defaultNote.title,
                        type = Note.Type.longNote,
                        modifiedDate = LocalDate.now(),
                        createdDate = LocalDate.now(),
                        color = currentColor
                )

                if (action == Note.Action.insert) {
                    noteDaoJava.insert(note)
                    defaultNoteDaoJava.insert(defaultNote)
                }

                if (action == Note.Action.update) {
                    noteDaoJava.update(note)
                    defaultNoteDaoJava.update(defaultNote)
                }

                finish()
            }
        }
    }

    private fun deleteAction() {
        val note = intent.getParcelableExtra<Note>(EXTRA_NOTE)

        if (note != null) {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Delete")
            alertDialog.setMessage("Are you sure ?")
            alertDialog.setPositiveButton("Yes") { _, _ ->
                val id = note.id
                noteDaoJava.delete(id)

                if (note.type == Note.Type.defaultNote) defaultNoteDaoJava.delete(id)
                if (note.type == Note.Type.listNote) listNoteDaoJava.delete(id)

                finish()
            }

            alertDialog.setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }

            alertDialog.show()
        }
    }

    private fun favAction() {
        btn_fav.isChecked = btn_fav.isChecked

        val icon: Int = if (btn_fav.isChecked) {
            R.drawable.ic_baseline_favorite_border_24
        } else {
            R.drawable.ic_baseline_favorite_24
        }

        btn_fav.icon = ContextCompat.getDrawable(
                this, icon
        )
    }
}