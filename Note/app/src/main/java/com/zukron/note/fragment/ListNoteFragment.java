package com.zukron.note.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.zukron.note.R;
import com.zukron.note.adapter.NoteListAdapter;
import com.zukron.note.model.ListNote;
import com.zukron.note.model.Note;
import com.zukron.note.model.dao.ListNoteDao;

import java.util.ArrayList;

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 8/8/2020
 */
public class ListNoteFragment extends Fragment {
    public static final String TAG = ListNoteFragment.class.getSimpleName();
    private TextInputEditText inputTitleNoteList;
    private RecyclerView rvNoteList;
    private Integer id = null;
    private static final String BUNDLE_NOTE = "bundle_note";

    public ListNoteFragment() {
    }

    public static ListNoteFragment newInstance(Note note) {
        ListNoteFragment listNoteFragment = new ListNoteFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_NOTE, note);

        listNoteFragment.setArguments(bundle);
        return listNoteFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_note_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputTitleNoteList = view.findViewById(R.id.input_title_note_list);
        rvNoteList = view.findViewById(R.id.rv_note_list);
    }

    @Override
    public void onStart() {
        super.onStart();

        ListNoteDao listNoteDao = new ListNoteDao(getContext());
        // jika user update
        if (getArguments() != null) {
            Note note = getArguments().getParcelable(BUNDLE_NOTE);

            if (note != null) {
                ListNote listNote = listNoteDao.get(note.getId());
                id = note.getId();
                inputTitleNoteList.setText(listNote.getTitle());

                NoteListAdapter noteListAdapter = new NoteListAdapter(getContext(), listNote.getChecks(), listNote.getItems());
                rvNoteList.setAdapter(noteListAdapter);
            }
        } else { // jika user insert
            // init index 0
            ArrayList<Boolean> checks = new ArrayList<>();
            checks.add(false);
            ArrayList<String> items = new ArrayList<>();
            items.add("");

            NoteListAdapter noteListAdapter = new NoteListAdapter(getContext(), checks, items);
            rvNoteList.setAdapter(noteListAdapter);
        }
    }

    public ListNote getNote() {
        ListNote listNote = null;

        if (validate()) {
            assert inputTitleNoteList.getText() != null;
            String title = inputTitleNoteList.getText().toString().trim();

            ArrayList<Boolean> checks = new ArrayList<>();
            ArrayList<String> items = new ArrayList<>();

            for (int i = 0; i < rvNoteList.getChildCount(); i++) {
                View v = rvNoteList.getChildAt(i);
                CheckBox checkBox = v.findViewById(R.id.cb_item_list_note);
                TextInputEditText textInputEditText = v.findViewById(R.id.input_item_list_note);

                assert textInputEditText.getText() != null;
                boolean check = checkBox.isChecked();
                checks.add(check);
                items.add(textInputEditText.getText().toString().trim());

            }

            listNote = new ListNote(id, title, checks, items);
        } else {
            Toast.makeText(getContext(), "Judul tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }

        return listNote;
    }

    private boolean validate() {
        boolean valid = true;

        assert inputTitleNoteList.getText() != null;
        if (TextUtils.isEmpty(inputTitleNoteList.getText().toString())) {
            valid = false;
        }

        return valid;
    }
}
