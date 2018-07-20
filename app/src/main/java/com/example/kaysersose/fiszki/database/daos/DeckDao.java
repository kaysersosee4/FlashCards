package com.example.kaysersose.fiszki.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.kaysersose.fiszki.database.entities.Deck;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Kayser Sose on 2018-06-29.
 */

@Dao
public interface DeckDao {
    @Insert
    long insertDeck(Deck deck);

    @Delete
    void deleteDeck(Deck deck);

    @Update
    void updateDeck(Deck deck);

    @Query("SELECT * from decks")
    Flowable<List<Deck>> getAllDecks();

    @Query("SELECT * from decks WHERE id=:deckID")
    Single<Deck> getDeck(int deckID);





}
