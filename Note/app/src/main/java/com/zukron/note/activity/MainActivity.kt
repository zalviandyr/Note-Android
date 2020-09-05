package com.zukron.note.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zukron.note.R
import com.zukron.note.adapter.NoteAdapter
import com.zukron.note.model.Note
import com.zukron.note.model.dao.*
import com.zukron.note.repository.NoteRepository
import com.zukron.note.util.Database
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 9/4/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var noteDaoJava: NoteDaoJava
    private lateinit var defaultNoteDaoJava: DefaultNoteDaoJava
    private lateinit var listNoteDaoJava: ListNoteDaoJava

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // toolbar
        material_toolbar.title = ""
        setSupportActionBar(material_toolbar)

        // database
        Database.init(this)
        noteDaoJava = NoteDaoJava(this)
        defaultNoteDaoJava = DefaultNoteDaoJava(this)
        listNoteDaoJava = ListNoteDaoJava(this)

        // floating button listener
        fab_create.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()

        // recycler view
        showItem()
    }

    private fun showItem() {
        val noteAdapter = NoteAdapter(this, noteDaoJava.all)
        noteAdapter.defaultNoteList = defaultNoteDaoJava.all
        noteAdapter.listNoteList = listNoteDaoJava.all

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, 1)
        recycler_view.layoutManager = staggeredGridLayoutManager
        recycler_view.adapter = noteAdapter

        noteAdapter.setOnClickSelected {
            val intent = Intent(this@MainActivity, DetailNoteActivity::class.java)
            intent.putExtra(DetailNoteActivity.EXTRA_ACTION, Note.Action.update)
            intent.putExtra(DetailNoteActivity.EXTRA_NOTE, it)
            startActivity(intent)
        }
    }

    override fun onClick(view: View?) {
        val intent = Intent(this@MainActivity, DetailNoteActivity::class.java)
        intent.putExtra(DetailNoteActivity.EXTRA_ACTION, Note.Action.insert)
        startActivity(intent)
    }
}