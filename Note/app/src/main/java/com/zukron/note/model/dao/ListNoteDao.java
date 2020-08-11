package com.zukron.note.model.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.zukron.note.model.ListNote;
import com.zukron.note.model.Note;
import com.zukron.note.util.Database;

import java.util.ArrayList;

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 8/10/2020
 */
public class ListNoteDao {
    private SQLiteDatabase database;

    public ListNoteDao(Context context) {
        database = Database.getDatabase(context);
    }

    public ArrayList<ListNote> getAll() {
        ArrayList<Integer> id = new ArrayList<>();
        ArrayList<ListNote> listNotes = new ArrayList<>();

        Cursor allId = database.rawQuery("SELECT id FROM note WHERE type=" + Note.Type.ListNote + " ORDER BY id DESC", null);
        if (allId.moveToFirst()) {
            do {
                id.add(allId.getInt(0));
            } while (allId.moveToNext());
        }
        allId.close();

        for (int i : id) {
            ArrayList<Boolean> checks = new ArrayList<>();
            ArrayList<String> items = new ArrayList<>();

            Cursor cursor = database.rawQuery("SELECT * FROM list_note WHERE id=" + i, null);
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getInt(2) == 0) {
                        checks.add(false);
                    } else {
                        checks.add(true);
                    }
                    items.add(cursor.getString(3));
                } while (cursor.moveToNext());
            }

            cursor.moveToFirst();
            listNotes.add(new ListNote(
                    cursor.getInt(0),
                    cursor.getString(1),
                    checks,
                    items
            ));

            cursor.close();
        }

        return listNotes;
    }

    public ListNote get(int id) {
        Cursor cursor = database.rawQuery("SELECT * FROM list_note WHERE id=" + id, null);
        ArrayList<Boolean> checks = new ArrayList<>();
        ArrayList<String> items = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(2) == 0) {
                    checks.add(false);
                } else {
                    checks.add(true);
                }
                items.add(cursor.getString(3));
            } while (cursor.moveToNext());
        }

        cursor.moveToFirst();
        ListNote listNote = new ListNote(
                cursor.getInt(0),
                cursor.getString(1),
                checks,
                items
        );

        cursor.close();
        return listNote;
    }

    public void insert(ListNote listNote) {
        Cursor cursor = database.rawQuery("SELECT id FROM note", null);
        cursor.moveToLast();
        int lastId = cursor.getInt(0);

        for (int i = 0; i < listNote.getItems().size(); i++) {
            String sql = "INSERT INTO list_note(id, title, checked, item) VALUES (?, ?, ?, ?)";
            SQLiteStatement statement = database.compileStatement(sql);
            statement.bindLong(1, lastId);
            statement.bindString(2, listNote.getTitle());

            int checked = listNote.getChecks().get(i) ? 1 : 0;
            statement.bindLong(3, checked);
            statement.bindString(4, listNote.getItems().get(i));

            statement.executeInsert();
        }

        cursor.close();
    }

    public void update(ListNote listNote) {
        ArrayList<Boolean> checks = listNote.getChecks();
        ArrayList<String> items = listNote.getItems();

        // hapus yang lama dan tambah kan yang baru
        SQLiteStatement statementDelete = database.compileStatement("DELETE FROM list_note WHERE id = ?");
        statementDelete.bindLong(1, listNote.getId());
        statementDelete.executeUpdateDelete();

        String sql = "INSERT INTO list_note(id, title, checked, item) VALUES (?, ?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);

        for (int i = 0; i < items.size(); i++) {
            int check = checks.get(i) ? 1 : 0;
            statement.bindLong(1, listNote.getId());
            statement.bindString(2, listNote.getTitle());
            statement.bindLong(3, check);
            statement.bindString(4, items.get(i));

            statement.executeInsert();
        }
    }

    public void delete(int id) {
        SQLiteStatement statementDelete = database.compileStatement("DELETE FROM list_note WHERE id = ?");
        statementDelete.bindLong(1, id);
        statementDelete.executeUpdateDelete();
    }
}
