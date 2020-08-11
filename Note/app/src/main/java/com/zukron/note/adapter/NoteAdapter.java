package com.zukron.note.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.card.MaterialCardView;
import com.zukron.note.R;
import com.zukron.note.model.DefaultNote;
import com.zukron.note.model.ListNote;
import com.zukron.note.model.Note;
import com.zukron.note.util.Tools;

import java.util.ArrayList;

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 8/7/2020
 */
public class NoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Note> notes;
    private ArrayList<DefaultNote> defaultNotes;
    private ArrayList<ListNote> listNotes;
    private OnClickSelected onClickSelected;
    private int shiftDefaultNote = 0;
    private int shiftListNote = 0;

    public NoteAdapter(Context context, ArrayList<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    public void setDefaultNotes(ArrayList<DefaultNote> defaultNotes) {
        this.defaultNotes = defaultNotes;
    }

    public void setListNotes(ArrayList<ListNote> listNotes) {
        this.listNotes = listNotes;
    }

    public void setOnClickSelected(OnClickSelected onClickSelected) {
        this.onClickSelected = onClickSelected;
    }

    @Override
    public int getItemViewType(int position) {
        Note note = notes.get(position);
        return note.getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (viewType == Note.Type.DefaultNote || viewType == Note.Type.LongNote) {
            View view = layoutInflater.inflate(R.layout.snippet_item_default_note, parent, false);
            return new ViewHolder1(view);
        } else {
            View view = layoutInflater.inflate(R.layout.snippet_item_list_note, parent, false);
            return new ViewHolder2(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Note note = notes.get(position);
        switch (holder.getItemViewType()) {
            case Note.Type.LongNote:
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                layoutParams.setFullSpan(true);

            case Note.Type.DefaultNote:
                ViewHolder1 holder1 = (ViewHolder1) holder;
                DefaultNote defaultNote = defaultNotes.get(shiftDefaultNote);

                holder1.mcvSnippetDefaultNote.setCardBackgroundColor(ContextCompat.getColor(context, note.getColor()));
                holder1.tvTitleSnippetDefaultNote.setText(defaultNote.getTitle());
                holder1.tvBodySnippetDefaultNote.setText(defaultNote.getBody());
                holder1.tvModifiedDateSnippetDefaultNote.setText(Tools.formatDate(note.getModified()));
                holder1.mcvSnippetDefaultNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickSelected.onClickSelected(note);
                    }
                });

                shiftDefaultNote++;
                break;

            case Note.Type.ListNote:
                ViewHolder2 holder2 = (ViewHolder2) holder;
                ListNote listNote = listNotes.get(shiftListNote);
                ArrayList<Boolean> checks = listNote.getChecks();
                ArrayList<String> items = listNote.getItems();

                for (int i = 0; i < checks.size(); i++) {
                    holder2.checkBoxes.get(i).setVisibility(View.VISIBLE);
                    holder2.textViews.get(i).setVisibility(View.VISIBLE);

                    holder2.checkBoxes.get(i).setChecked(checks.get(i));
                    holder2.textViews.get(i).setText(items.get(i));
                }

                holder2.mcvSnippetListNote.setCardBackgroundColor(ContextCompat.getColor(context, note.getColor()));
                holder2.tvTitleSnippetListNote.setText(listNote.getTitle());
                holder2.tvModifiedDateSnippetListNote.setText(Tools.formatDate(note.getModified()));
                holder2.mcvSnippetListNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickSelected.onClickSelected(note);
                    }
                });

                shiftListNote++;
                break;
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class ViewHolder1 extends RecyclerView.ViewHolder {
        private MaterialCardView mcvSnippetDefaultNote;
        private TextView tvTitleSnippetDefaultNote, tvBodySnippetDefaultNote, tvModifiedDateSnippetDefaultNote;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);

            mcvSnippetDefaultNote = itemView.findViewById(R.id.mcv_snippet_item_default_note);
            tvTitleSnippetDefaultNote = itemView.findViewById(R.id.tv_title_snippet_item_default_note);
            tvBodySnippetDefaultNote = itemView.findViewById(R.id.tv_body_snippet_item_default_note);
            tvModifiedDateSnippetDefaultNote = itemView.findViewById(R.id.tv_modified_date_snippet_item_default_note);
        }
    }

    static class ViewHolder2 extends RecyclerView.ViewHolder {
        private MaterialCardView mcvSnippetListNote;
        private TextView tvTitleSnippetListNote;
        private ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        private ArrayList<TextView> textViews = new ArrayList<>();
        private TextView tvModifiedDateSnippetListNote;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);

            mcvSnippetListNote = itemView.findViewById(R.id.mcv_snippet_item_list_note);
            tvTitleSnippetListNote = itemView.findViewById(R.id.tv_title_snippet_item_list_note);

            CheckBox cbSnippetListNote1 = itemView.findViewById(R.id.cb_snippet_item_list_note_1);
            checkBoxes.add(cbSnippetListNote1);
            CheckBox cbSnippetListNote2 = itemView.findViewById(R.id.cb_snippet_item_list_note_2);
            checkBoxes.add(cbSnippetListNote2);
            CheckBox cbSnippetListNote3 = itemView.findViewById(R.id.cb_snippet_item_list_note_3);
            checkBoxes.add(cbSnippetListNote3);
            CheckBox cbSnippetListNote4 = itemView.findViewById(R.id.cb_snippet_item_list_note_4);
            checkBoxes.add(cbSnippetListNote4);

            TextView tvSnippetListNote1 = itemView.findViewById(R.id.tv_snippet_item_list_note_1);
            textViews.add(tvSnippetListNote1);
            TextView tvSnippetListNote2 = itemView.findViewById(R.id.tv_snippet_item_list_note_2);
            textViews.add(tvSnippetListNote2);
            TextView tvSnippetListNote3 = itemView.findViewById(R.id.tv_snippet_item_list_note_3);
            textViews.add(tvSnippetListNote3);
            TextView tvSnippetListNote4 = itemView.findViewById(R.id.tv_snippet_item_list_note_4);
            textViews.add(tvSnippetListNote4);

            tvModifiedDateSnippetListNote = itemView.findViewById(R.id.tv_modified_date_snippet_item_list_note);

            for (int i = 0; i < checkBoxes.size(); i++) {
                checkBoxes.get(i).setVisibility(View.GONE);
                textViews.get(i).setVisibility(View.GONE);
            }
        }
    }

    public interface OnClickSelected {
        void onClickSelected(Note note);
    }
}