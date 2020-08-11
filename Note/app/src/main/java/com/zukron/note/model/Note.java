package com.zukron.note.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDate;

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 8/9/2020
 */
public class Note implements Parcelable {
    private Integer id;
    private String title;
    private int type;
    private LocalDate modified;
    private LocalDate created;
    private int color;

    public Note(Integer id, String title, int type, LocalDate modified, LocalDate created, int color) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.modified = modified;
        this.created = created;
        this.color = color;
    }

    public Note(Integer id, String title, LocalDate modified, int color) {
        this.id = id;
        this.title = title;
        this.modified = modified;
        this.color = color;
    }

    protected Note(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        title = in.readString();
        type = in.readInt();
        modified = (LocalDate) in.readSerializable();
        created = (LocalDate) in.readSerializable();
        color = in.readInt();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        return type;
    }

    public LocalDate getModified() {
        return modified;
    }

    public LocalDate getCreated() {
        return created;
    }

    public int getColor() {
        return color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(title);
        parcel.writeInt(type);
        parcel.writeSerializable(modified);
        parcel.writeSerializable(created);
        parcel.writeInt(color);
    }

    public interface Action {
        String insert = "insert";
        String update = "update";
    }

    public interface Type {
        int DefaultNote = 1;
        int ListNote = 2;
        int LongNote = 3;
    }
}