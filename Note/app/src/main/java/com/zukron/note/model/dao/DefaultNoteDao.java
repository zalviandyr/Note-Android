package com.zukron.note.model.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.zukron.note.model.DefaultNote;
import com.zukron.note.util.Database;

import java.util.ArrayList;

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 8/10/2020
 */
public class DefaultNoteDao {
    private SQLiteDatabase database;

    public DefaultNoteDao(Context context) {
        database = Database.getDatabase(context);
    }

    public DefaultNote get(int id) {
        DefaultNote defaultNote = null;
        Cursor cursor = database.rawQuery("SELECT * FROM default_note WHERE id=" + id, null);
        if (cursor.moveToFirst()) {
            do {
                defaultNote = new DefaultNote(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                );
            } while (cursor.moveToNext());
        }

        cursor.close();
        return defaultNote;
    }

    public ArrayList<DefaultNote> getAll() {
        ArrayList<DefaultNote> defaultNotes = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM default_note ORDER BY id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                defaultNotes.add(new DefaultNote(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return defaultNotes;
    }

    public void insert(DefaultNote defaultNote) {
        Cursor cursor = database.rawQuery("SELECT id FROM note", null);
        cursor.moveToLast();
        int lastId = cursor.getInt(0);

        String sql = "INSERT INTO default_note(id, title, body) VALUES (?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindLong(1, lastId);
        statement.bindString(2, defaultNote.getTitle());
        statement.bindString(3, defaultNote.getBody());

        cursor.close();
        statement.executeInsert();
    }

    public void update(DefaultNote defaultNote) {
        String sql = "UPDATE default_note SET title = ?, body = ? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindString(1, defaultNote.getTitle());
        statement.bindString(2, defaultNote.getBody());
        statement.bindLong(3, defaultNote.getId());

        statement.executeUpdateDelete();
    }

    public void delete(int id) {
        String sql = "DELETE FROM default_note WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindLong(1, id);

        statement.executeUpdateDelete();
    }
}
