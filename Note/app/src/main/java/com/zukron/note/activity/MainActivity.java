package com.zukron.note.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.zukron.note.R;
import com.zukron.note.adapter.NoteAdapter;
import com.zukron.note.model.Note;
import com.zukron.note.model.dao.DefaultNoteDao;
import com.zukron.note.model.dao.ListNoteDao;
import com.zukron.note.model.dao.NoteDao;
import com.zukron.note.util.Database;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NoteAdapter.OnClickSelected {
    private ExtendedFloatingActionButton fabCreateMain;
    private MaterialToolbar mtMain;
    private RecyclerView rvMain;
    private NoteDao noteDao;
    private DefaultNoteDao defaultNoteDao;
    private ListNoteDao listNoteDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtMain = findViewById(R.id.mt_main);
        rvMain = findViewById(R.id.rv_main);
        fabCreateMain = findViewById(R.id.fab_create_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // toolbar
        mtMain.setTitle("");
        setSupportActionBar(mtMain);

        // database
        Database.init(this);
        noteDao = new NoteDao(this);
        defaultNoteDao = new DefaultNoteDao(this);
        listNoteDao = new ListNoteDao(this);

        // floating button
        fabCreateMain.setOnClickListener(this);

        showRecyclerView();
    }

    private void showRecyclerView() {
        NoteAdapter noteAdapter = new NoteAdapter(this, noteDao.getAll());
        noteAdapter.setDefaultNotes(defaultNoteDao.getAll());
        noteAdapter.setListNotes(listNoteDao.getAll());
        noteAdapter.setOnClickSelected(this);

        StaggeredGridLayoutManager staggeredGridLayoutManager =new StaggeredGridLayoutManager(2, 1);
        rvMain.setLayoutManager(staggeredGridLayoutManager);
        rvMain.setAdapter(noteAdapter);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, DetailNoteActivity.class);
        intent.putExtra(DetailNoteActivity.EXTRA_ACTION, Note.Action.insert);
        startActivity(intent);
    }

    @Override
    public void onClickSelected(Note note) {
        Intent intent = new Intent(this, DetailNoteActivity.class);
        intent.putExtra(DetailNoteActivity.EXTRA_ACTION, Note.Action.update);
        intent.putExtra(DetailNoteActivity.EXTRA_NOTE, note);
        startActivity(intent);
    }
}