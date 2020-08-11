package com.zukron.note.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.zukron.note.R;
import com.zukron.note.model.DefaultNote;
import com.zukron.note.model.Note;
import com.zukron.note.model.dao.DefaultNoteDao;

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 8/8/2020
 */
public class DefaultNoteFragment extends Fragment {
    public static final String TAG = DefaultNoteFragment.class.getSimpleName();
    private static final String EXTRA_NOTE = "note";
    private TextInputEditText inputBodyNote, inputTitleNote;
    private Integer id = null;

    public DefaultNoteFragment() {
    }

    public static DefaultNoteFragment newInstance(Note note) {
        DefaultNoteFragment defaultNoteFragment = new DefaultNoteFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_NOTE, note);

        defaultNoteFragment.setArguments(bundle);
        return defaultNoteFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.default_note_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputTitleNote = view.findViewById(R.id.input_title_note);
        inputBodyNote = view.findViewById(R.id.input_body_note);
    }

    @Override
    public void onStart() {
        super.onStart();

        DefaultNoteDao defaultNoteDao = new DefaultNoteDao(getContext());

        // jika user ingin mengupdate
        if (getArguments() != null) {
            Note note = getArguments().getParcelable(EXTRA_NOTE);

            if (note != null) {
                DefaultNote defaultNote = defaultNoteDao.get(note.getId());
                id = defaultNote.getId();
                inputTitleNote.setText(defaultNote.getTitle());
                inputBodyNote.setText(defaultNote.getBody());
            }
        }
    }

    public DefaultNote getNote() {
        DefaultNote defaultNote = null;

        if (validated()) {
            if (inputBodyNote.getText() != null && inputTitleNote.getText() != null) {
                String title = inputTitleNote.getText().toString().trim();
                String body = inputBodyNote.getText().toString().trim();

                defaultNote = new DefaultNote(id, title, body);
            }
        } else {
            Toast.makeText(getContext(), "Judul atau isi tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }

        return defaultNote;
    }

    private boolean validated() {
        boolean valid = true;

        assert inputTitleNote.getText() != null;
        if (TextUtils.isEmpty(inputTitleNote.getText().toString())) {
            valid = false;
        }

        assert inputBodyNote.getText() != null;
        if (TextUtils.isEmpty(inputBodyNote.getText().toString())) {
            valid = false;
        }

        return valid;
    }
}
