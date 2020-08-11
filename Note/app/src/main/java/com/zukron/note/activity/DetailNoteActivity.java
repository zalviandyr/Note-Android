package com.zukron.note.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.zukron.note.R;
import com.zukron.note.fragment.DefaultNoteFragment;
import com.zukron.note.fragment.ListNoteFragment;
import com.zukron.note.fragment.LongNoteFragment;
import com.zukron.note.model.DefaultNote;
import com.zukron.note.model.ListNote;
import com.zukron.note.model.Note;
import com.zukron.note.model.dao.DefaultNoteDao;
import com.zukron.note.model.dao.ListNoteDao;
import com.zukron.note.model.dao.NoteDao;
import com.zukron.note.util.Tools;

import org.threeten.bp.LocalDate;

public class DetailNoteActivity extends AppCompatActivity implements View.OnClickListener, OnClickListener {
    private MaterialToolbar mtCreate;
    private MaterialButton btnSaveCreate, btnDeleteCreate, btnFavCreate, btnMoreCreate;
    private TextView tvLastModifiedCreate, tvCreatedCreate;
    private CoordinatorLayout coordinatorLayout;
    private DialogPlus dialogPlus;
    private Window window;
    private DefaultNoteFragment defaultNoteFragment;
    private ListNoteFragment listNoteFragment;
    private NoteDao noteDao;
    private DefaultNoteDao defaultNoteDao;
    private ListNoteDao listNoteDao;
    private LongNoteFragment longNoteFragment;
    private int currentColor;
    public static final String EXTRA_NOTE = "extra_note";
    public static final String EXTRA_ACTION = "extra_action";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        mtCreate = findViewById(R.id.mt_create);
        btnSaveCreate = findViewById(R.id.btn_save_create);
        btnDeleteCreate = findViewById(R.id.btn_delete_create);
        btnFavCreate = findViewById(R.id.btn_fav_create);
        btnMoreCreate = findViewById(R.id.btn_more_create);

        tvLastModifiedCreate = findViewById(R.id.tv_last_modified_create);
        tvCreatedCreate = findViewById(R.id.tv_created_create);

        coordinatorLayout = findViewById(R.id.coordinator_layout);

        // set dialog more menu
        setDialogMoreMenu();

        // set default note body
        setNoteBody();
    }

    private void setNoteBody() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Note Data
        String action = getIntent().getStringExtra(DetailNoteActivity.EXTRA_ACTION);
        Note note = getIntent().getParcelableExtra(DetailNoteActivity.EXTRA_NOTE);

        if (action != null) {
            if (action.equals(Note.Action.insert)) { // insert
                defaultNoteFragment = new DefaultNoteFragment();
                fragmentTransaction.add(R.id.fl_note_create, defaultNoteFragment, DefaultNoteFragment.TAG);
            }

            if (action.equals(Note.Action.update)) { // update
                if (note != null) {
                    if (note.getType() == Note.Type.DefaultNote) {
                        defaultNoteFragment = DefaultNoteFragment.newInstance(note);
                        fragmentTransaction.add(R.id.fl_note_create, defaultNoteFragment, DefaultNoteFragment.TAG);
                    }

                    if (note.getType() == Note.Type.ListNote) {
                        listNoteFragment = ListNoteFragment.newInstance(note);
                        fragmentTransaction.add(R.id.fl_note_create, listNoteFragment, ListNoteFragment.TAG);
                    }

                    if (note.getType() == Note.Type.LongNote) {
                        longNoteFragment = LongNoteFragment.newInstance(note);
                        fragmentTransaction.add(R.id.fl_note_create, longNoteFragment, LongNoteFragment.TAG);
                    }
                }
            }
        }

        fragmentTransaction.commit();
    }

    private void changeNoteBody(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (tag.equals(DefaultNoteFragment.TAG)) {
            defaultNoteFragment = new DefaultNoteFragment();
            fragmentTransaction.replace(R.id.fl_note_create, defaultNoteFragment, DefaultNoteFragment.TAG);
        }

        if (tag.equals(ListNoteFragment.TAG)) {
            listNoteFragment = new ListNoteFragment();
            fragmentTransaction.replace(R.id.fl_note_create, listNoteFragment, ListNoteFragment.TAG);
        }

        if (tag.equals(LongNoteFragment.TAG)) {
            longNoteFragment = new LongNoteFragment();
            fragmentTransaction.replace(R.id.fl_note_create, longNoteFragment, LongNoteFragment.TAG);
        }

        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mtCreate.setTitle("");
        setSupportActionBar(mtCreate);

        // window
        window = getWindow();

        // init background color
        initBackgroundColor();

        // set date
        setDateModifiedCreated();

        // button
        btnSaveCreate.setOnClickListener(this);
        btnDeleteCreate.setOnClickListener(this);
        btnFavCreate.setOnClickListener(this);
        btnMoreCreate.setOnClickListener(this);

        // database
        noteDao = new NoteDao(this);
        defaultNoteDao = new DefaultNoteDao(this);
        listNoteDao = new ListNoteDao(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save_create:
                insertUpdateAction();
                break;
            case R.id.btn_delete_create:
                deleteAction();
                break;
            case R.id.btn_fav_create:
                favAction();
                break;
            case R.id.btn_more_create:
                dialogPlus.show();
                break;
        }
    }

    @Override
    public void onClick(DialogPlus dialog, View view) {
        switch (view.getId()) {
            case R.id.btn_add_default_note:
                changeNoteBody(DefaultNoteFragment.TAG);
                break;
            case R.id.btn_add_list_note:
                changeNoteBody(ListNoteFragment.TAG);
                break;
            case R.id.btn_add_long_note:
                changeNoteBody(LongNoteFragment.TAG);
                break;
            case R.id.btn_pink_bg_create:
                setBackgroundColor(R.color.cardBgPink);
                break;
            case R.id.btn_blue_bg_create:
                setBackgroundColor(R.color.cardBgBlue);
                break;
            case R.id.btn_violet_bg_create:
                setBackgroundColor(R.color.cardBgViolet);
                break;
            case R.id.btn_orange_bg_create:
                setBackgroundColor(R.color.cardBgOrange);
                break;
        }

        dialog.dismiss();
    }

    private void initBackgroundColor() {
        Note note = getIntent().getParcelableExtra(DetailNoteActivity.EXTRA_NOTE);

        // change current color
        if (note == null) {
            currentColor = R.color.cardBgOrange;
        } else {
            currentColor = note.getColor();
        }

        setBackgroundColor(currentColor);
    }

    private void setDateModifiedCreated() {
        String action = getIntent().getStringExtra(DetailNoteActivity.EXTRA_ACTION);
        Note note = getIntent().getParcelableExtra(DetailNoteActivity.EXTRA_NOTE);

        if (action != null) {
            if (action.equals(Note.Action.insert)) {
                tvLastModifiedCreate.setText("");
                tvCreatedCreate.setText("");
            } else if (action.equals(Note.Action.update)) {
                if (note != null) {
                    tvLastModifiedCreate.setText(Tools.formatDate(note.getModified()));
                    tvCreatedCreate.setText(Tools.formatDate(note.getCreated()));
                }
            }
        }
    }

    private void setDialogMoreMenu() {
        dialogPlus = DialogPlus.newDialog(this)
                .setExpanded(true, 600)
                .setGravity(Gravity.TOP)
                .setExpanded(true)
                .setOnClickListener(this)
                .setContentHolder(new ViewHolder(R.layout.more_menu))
                .create();
    }

    private void setBackgroundColor(int color) {
        currentColor = color;
        coordinatorLayout.setBackgroundColor(ContextCompat.getColor(this, color));

        // status bar and button save
        window.setStatusBarColor(ContextCompat.getColor(this, color));
        btnSaveCreate.setTextColor(ContextCompat.getColor(this, color));
    }

    private void favAction() {
        btnFavCreate.setChecked(btnFavCreate.isChecked());

        if (btnFavCreate.isChecked()) {
            btnFavCreate.setIcon(getDrawable(R.drawable.ic_baseline_favorite_border_24));
        } else {
            btnFavCreate.setIcon(getDrawable(R.drawable.ic_baseline_favorite_24));
        }
    }

    private void deleteAction() {
        final Note note = getIntent().getParcelableExtra(DetailNoteActivity.EXTRA_NOTE);

        if (note != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Hapus");
            builder.setMessage("Apakah yakin hapus ?");
            builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    int id = note.getId();
                    noteDao.delete(id);

                    if (note.getType() == Note.Type.DefaultNote)
                        defaultNoteDao.delete(id);

                    if (note.getType() == Note.Type.ListNote)
                        listNoteDao.delete(id);

                    finish();
                }
            });
            builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            builder.show();
        }
    }

    private void insertUpdateAction() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_note_create);
        String action = getIntent().getStringExtra(DetailNoteActivity.EXTRA_ACTION);
        AndroidThreeTen.init(this);

        if (fragment instanceof DefaultNoteFragment) {
            DefaultNote defaultNote = defaultNoteFragment.getNote();

            if (defaultNote != null && action != null) {
                if (action.equals(Note.Action.insert)) {
                    Note note = new Note(defaultNote.getId(), defaultNote.getTitle(), Note.Type.DefaultNote, LocalDate.now(), LocalDate.now(), currentColor);

                    noteDao.insert(note);
                    defaultNoteDao.insert(defaultNote);
                }

                if (action.equals(Note.Action.update)) {
                    Note note = new Note(defaultNote.getId(), defaultNote.getTitle(), LocalDate.now(), currentColor);

                    noteDao.update(note);
                    defaultNoteDao.update(defaultNote);
                }

                finish();
            }
        }

        if (fragment instanceof ListNoteFragment) {
            ListNote listNote = listNoteFragment.getNote();

            if (listNote != null && action != null) {
                if (action.equals(Note.Action.insert)) {
                    Note note = new Note(listNote.getId(), listNote.getTitle(), Note.Type.ListNote, LocalDate.now(), LocalDate.now(), currentColor);

                    noteDao.insert(note);
                    listNoteDao.insert(listNote);
                }

                if (action.equals(Note.Action.update)) {
                    Note note = new Note(listNote.getId(), listNote.getTitle(), LocalDate.now(), currentColor);

                    noteDao.update(note);
                    listNoteDao.update(listNote);
                }

                finish();
            }
        }

        if (fragment instanceof LongNoteFragment) {
            DefaultNote defaultNote = longNoteFragment.getNote();

            if (defaultNote != null && action != null) {
                if (action.equals(Note.Action.insert)) {
                    Note note = new Note(defaultNote.getId(), defaultNote.getTitle(), Note.Type.LongNote, LocalDate.now(), LocalDate.now(), currentColor);

                    noteDao.insert(note);
                    defaultNoteDao.insert(defaultNote);
                }

                if (action.equals(Note.Action.update)) {
                    Note note = new Note(defaultNote.getId(), defaultNote.getTitle(), LocalDate.now(), currentColor);

                    noteDao.update(note);
                    defaultNoteDao.update(defaultNote);
                }

                finish();
            }
        }
    }
}