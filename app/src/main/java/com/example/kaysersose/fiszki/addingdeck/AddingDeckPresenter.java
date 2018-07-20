package com.example.kaysersose.fiszki.addingdeck;

import android.content.Context;

import com.example.kaysersose.fiszki.database.FiszkiDatabase;
import com.example.kaysersose.fiszki.database.entities.Deck;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddingDeckPresenter {
    private static int BLANK_ID = 0;
    private FiszkiDatabase db;
    private IAddingDeckView view;

    public AddingDeckPresenter(Context context, IAddingDeckView view){
        db = FiszkiDatabase.getAppDatabase(context);
        this.view = view;

    }


    public void addDeck(String name, String newPerLesson){
        int intNewPerLesson = Integer.parseInt(newPerLesson);
        Observable.fromCallable(() -> db.getDeckDao()
                .insertDeck(new Deck(BLANK_ID, name, intNewPerLesson)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(Long id) {
                        view.addCards(id);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showToast("Deck with this name already exists. Change it!");
                    }

                    @Override
                    public void onComplete() {}
                });
    }

}
