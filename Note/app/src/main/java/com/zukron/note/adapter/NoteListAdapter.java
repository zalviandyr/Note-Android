package com.zukron.note.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.zukron.note.R;

import java.util.ArrayList;

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 8/8/2020
 */
public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Boolean> checks;
    private ArrayList<String> items;

    public NoteListAdapter(Context context, ArrayList<Boolean> checks, ArrayList<String> items) {
        this.context = context;
        this.checks = checks;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_list_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        boolean check = checks.get(position);
        String item = items.get(position);

        holder.cbItemListNote.setChecked(check);
        holder.inputItemListNote.setText(item);
        holder.btnAddNoteListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
        holder.btnRemoveNoteListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(position);
            }
        });
    }

    private void add() {
        checks.add(false);
        items.add("");

        int lastIndex = items.size() - 1;
        notifyItemInserted(lastIndex);
    }

    /***
     * mengatasi karena nilai posisi akan tetap menyimpan data yang tidak di re order
     */
    private void remove(int position) {
        if (position == items.size() - 1) { // jika element terakhir dihapus
            checks.remove(position);
            items.remove(position);
            notifyItemRemoved(position);
        } else {
            int shift = 0;
            while (true) {
                try {
                    checks.remove(position - shift);
                    items.remove(position - shift);
                    notifyItemRemoved(position);
                    break;
                } catch (IndexOutOfBoundsException e) {
                    shift++;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cbItemListNote;
        private TextInputEditText inputItemListNote;
        private MaterialButton btnRemoveNoteListItem, btnAddNoteListItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cbItemListNote = itemView.findViewById(R.id.cb_item_list_note);
            inputItemListNote = itemView.findViewById(R.id.input_item_list_note);
            btnRemoveNoteListItem = itemView.findViewById(R.id.btn_remove_item_list_note);
            btnAddNoteListItem = itemView.findViewById(R.id.btn_add_item_list_note);
        }
    }
}
