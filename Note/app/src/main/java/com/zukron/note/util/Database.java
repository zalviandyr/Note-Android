package com.zukron.note.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 8/10/2020
 */
public class Database {
    private static final String DB_NAME = "note_db";
    private static final String NOTE_TABLE = "note";
    private static final String DEFAULT_NOTE_TABLE = "default_note";
    private static final String LIST_NOTE_TABLE = "list_note";

    public static void init(Context context) {
        SQLiteDatabase database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        database.execSQL(getNoteTableQuery());
        database.execSQL(getDefaultNoteTableQuery());
        database.execSQL(getListNoteTableQuery());
    }

    public static SQLiteDatabase getDatabase(Context context) {
        return context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
    }

    private static String getNoteTableQuery() {
        return "CREATE TABLE IF NOT EXISTS " + NOTE_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, type INTEGER, modified TEXT, created TEXT, color INTEGER);";
    }

    private static String getDefaultNoteTableQuery() {
        return "CREATE TABLE IF NOT EXISTS " + DEFAULT_NOTE_TABLE + " (id INTEGER, title TEXT, body TEXT, " +
                "FOREIGN KEY (id) REFERENCES note(id) ON DELETE CASCADE ON UPDATE CASCADE);";
    }

    public static String getListNoteTableQuery() {
        return "CREATE TABLE IF NOT EXISTS " + LIST_NOTE_TABLE + " (id INTEGER, title TEXT, checked INTEGER, item TEXT, " +
                "FOREIGN KEY (id) REFERENCES note(id) ON DELETE CASCADE ON UPDATE CASCADE);";
    }
}
