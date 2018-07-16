package com.example.kaysersose.fiszki.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.kaysersose.fiszki.database.daos.DeckDao;
import com.example.kaysersose.fiszki.database.daos.FlashCardDao;
import com.example.kaysersose.fiszki.database.entities.Deck;
import com.example.kaysersose.fiszki.database.entities.FlashCard;
import com.example.kaysersose.fiszki.utils.Converters;

/**
 * Created by Kayser Sose on 2018-06-29.
 */

@Database(entities = {Deck.class, FlashCard.class}, version=2)
@TypeConverters(Converters.class)
public abstract class FiszkiDatabase extends RoomDatabase {
    private static FiszkiDatabase INSTANCE;


    public abstract DeckDao getDeckDao();
    public abstract FlashCardDao getCardDao();

    public static FiszkiDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), FiszkiDatabase.class, "fiszki")
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
