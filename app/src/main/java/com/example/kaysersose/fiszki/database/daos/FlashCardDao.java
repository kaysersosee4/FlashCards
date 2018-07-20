package com.example.kaysersose.fiszki.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.kaysersose.fiszki.database.entities.FlashCard;


import java.util.Date;
import java.util.List;


import io.reactivex.Flowable;

import io.reactivex.Single;

/**
 * Created by Kayser Sose on 2018-06-29.
 */
@Dao
public interface FlashCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFlashCard(FlashCard card);

    @Delete
    void deleteFlashCard(FlashCard card);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateFlashCard(FlashCard card);

    @Query("SELECT * FROM flashcards")
    Flowable<List<FlashCard>> getAllCards();

    @Query("SELECT * FROM flashcards WHERE deckId = :deckID")
    Flowable<List<FlashCard>> getCardsFromDeck(int deckID);

    @Query("SELECT COUNT(id)FROM flashcards WHERE deckId =:deckID AND daysToNextRevision=0")
    Single<Integer> getNewCount(int deckID);

    @Query("SELECT COUNT(id) FROM flashcards WHERE deckId=:deckID AND :currentDate-lastRevision>86400000*daysToNextRevision")
    Single <Integer>getRepeatCount(int deckID, Date currentDate);

    @Query("SELECT * FROM flashcards WHERE deckId = :deckID AND (daysToNextRevision=0 OR :currentDate-lastRevision>86400000*daysToNextRevision)")
    Flowable<List<FlashCard>> getLearningCardsFromDeck(int deckID, Date currentDate);

    @Query("SELECT * FROM flashcards WHERE id=:cardId")
    Single<FlashCard> getCard(int cardId);
}
