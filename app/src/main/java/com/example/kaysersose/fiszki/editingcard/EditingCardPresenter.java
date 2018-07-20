package com.example.kaysersose.fiszki.editingcard;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.kaysersose.fiszki.R;
import com.example.kaysersose.fiszki.database.FiszkiDatabase;
import com.example.kaysersose.fiszki.database.entities.FlashCard;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class EditingCardPresenter {
    private IEditingCardView view;
    private FlashCard card;
    private FiszkiDatabase db;

    @SuppressLint("CheckResult")
    public EditingCardPresenter(IEditingCardView view, Context context, int cardID) {
        this.view = view;
        db= FiszkiDatabase.getAppDatabase(context);

        db.getCardDao().getCard(cardID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x->{
                    setCard(x);
                    view.setFront(card.getObverse());
                    view.setBack(card.getReverse());
                });


    }

    private void setCard(FlashCard card){
        this.card = card;
    }

    @SuppressLint("CheckResult")
    public void updateCard(String front, String back){
        card.setObverse(front);
        card.setReverse(back);
        Completable.fromAction(() -> db.getCardDao().updateFlashCard(card))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(()->{
                    view.onSuccess();
                });
    }
}
