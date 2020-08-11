package com.zukron.note.model.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.zukron.note.model.Note;
import com.zukron.note.util.Database;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 8/10/2020
 */
public class NoteDao {
    private SQLiteDatabase database;

    public NoteDao(Context context) {
        database = Database.getDatabase(context);
    }

    public ArrayList<Note> getAll() {
        ArrayList<Note> notes = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM note ORDER BY id DESC", null);

        if (cursor.moveToFirst()) {
            do {
                notes.add(new Note(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        LocalDate.parse(cursor.getString(3)),
                        LocalDate.parse(cursor.getString(4)),
                        cursor.getInt(5)
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return notes;
    }

    public void insert(Note note) {
        String sql = "INSERT INTO note(title, type, modified, created, color) VALUES(?, ?, ?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindString(1, note.getTitle());
        statement.bindLong(2, note.getType());
        statement.bindString(3, note.getModified().toString());
        statement.bindString(4, note.getCreated().toString());
        statement.bindLong(5, note.getColor());

        statement.executeInsert();
    }

    public void update(Note note) {
        String sql = "UPDATE note SET title = ?, modified = ?, color = ? where id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindString(1, note.getTitle());
        statement.bindString(2, note.getModified().toString());
        statement.bindLong(3, note.getColor());
        statement.bindLong(4, note.getId());

        statement.executeUpdateDelete();
    }

    public void delete(int id) {
        String sql = "DELETE FROM note WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindLong(1, id);
        statement.executeUpdateDelete();
    }
}
