package com.example.kaysersose.fiszki.addingcards;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.kaysersose.fiszki.database.FiszkiDatabase;
import com.example.kaysersose.fiszki.database.entities.FlashCard;

import java.util.Date;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class AddingCardsPresenter{
    int deckId;
    FiszkiDatabase db;
    IAddingCardsView addingCardsView;

    public AddingCardsPresenter(int deckId, Context context, IAddingCardsView v){
        this.deckId = deckId;
        db = FiszkiDatabase.getAppDatabase(context);
        addingCardsView = v;
    }


    @SuppressLint("CheckResult")
    public void addCard(String front, String back) {
        Completable.fromAction(() -> db.getCardDao().insertFlashCard(new FlashCard(0, deckId, front, back, 0, new Date())))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(()->{
                    addingCardsView.clearFields();
                });
    }
}
