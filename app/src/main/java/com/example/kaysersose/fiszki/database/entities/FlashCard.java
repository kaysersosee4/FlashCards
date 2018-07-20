package com.example.kaysersose.fiszki.database.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;


/**
 * Created by Kayser Sose on 2018-06-29.
 */

@Entity (tableName="Flashcards", foreignKeys = {
        @ForeignKey(
                entity = Deck.class,
                parentColumns = "id",
                childColumns = "deckId",
                onDelete = ForeignKey.CASCADE
        )},
        indices = {@Index(name = "deckId", value="deckId")}

)
public class FlashCard implements Serializable{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int deckId;
    private String obverse;
    private String reverse;
    private int daysToNextRevision;
    private Date lastRevision;


    public FlashCard(int id, int deckId, String obverse, String reverse, int daysToNextRevision, Date lastRevision) {
        this.id = id;
        this.deckId = deckId;
        this.obverse = obverse;
        this.reverse = reverse;
        this.daysToNextRevision = daysToNextRevision;
        this.lastRevision = lastRevision;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeckId() {
        return deckId;
    }

    public void setDeckId(int deckId) {
        this.deckId = deckId;
    }

    public String getObverse() {
        return obverse;
    }

    public void setObverse(String obverse) {
        this.obverse = obverse;
    }

    public String getReverse() {
        return reverse;
    }

    public void setReverse(String reverse) {
        this.reverse = reverse;
    }

    public int getDaysToNextRevision() {
        return daysToNextRevision;
    }

    public void setDaysToNextRevision(int daysToNextRevision) {
        this.daysToNextRevision = daysToNextRevision;
    }

    public Date getLastRevision() {
        return lastRevision;
    }

    public void setLastRevision(Date lastRevision) {
        this.lastRevision = lastRevision;
    }
}
