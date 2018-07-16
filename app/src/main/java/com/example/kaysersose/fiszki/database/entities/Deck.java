package com.example.kaysersose.fiszki.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Kayser Sose on 2018-06-29.
 */

@Entity(tableName = "Decks",
        indices = {@Index(value = {"name"},
        unique = true)})

public class Deck {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name="name")
    private String name;
    private int newPerLesson;


    public Deck(int id, String name, int newPerLesson) {
        this.id = id;
        this.name = name;
        this.newPerLesson = newPerLesson;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNewPerLesson() {
        return newPerLesson;
    }

    public void setNewPerLesson(int newPerLesson) {
        this.newPerLesson = newPerLesson;
    }
}
