package com.example.kaysersose.fiszki.editingdeck;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.kaysersose.fiszki.R;
import com.example.kaysersose.fiszki.database.FiszkiDatabase;
import com.example.kaysersose.fiszki.database.entities.Deck;
import com.example.kaysersose.fiszki.database.entities.FlashCard;
import com.example.kaysersose.fiszki.editingcard.IEditingCardView;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import io.reactivex.schedulers.Schedulers;

public class CardListPresenter{
    private Deck deck;
    private List<FlashCard> cards;
    private FiszkiDatabase db;
    private IEditingDeckView editingDeckView;


    @SuppressLint("CheckResult")
    public CardListPresenter(int deckId, Context context, IEditingDeckView v) {
        db = FiszkiDatabase.getAppDatabase(context);
        editingDeckView = v;

        db.getDeckDao().getDeck(deckId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x->{
                    setDeck(x);
                    editingDeckView.showDeckInfo(x);
                });

        db.getCardDao().getCardsFromDeck(deckId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x->{
                    setCards(x);
                    editingDeckView.onPresenterPrepared();
                });
    }


    public void onBindWordItemViewAtPosition(int position, IWordItemView view){
        FlashCard card = cards.get(position);
        view.setCardFront(card.getObverse());
        view.setCardBack(card.getReverse());
    }

    public int getCardId(int position){
        return cards.get(position).getId();
    }

    public int getWordsItemsCount(){
        return cards.size();
    }

    @SuppressLint("CheckResult")
    public void changeDeck(String name, String newPerLesson){
        deck.setName(name);
        deck.setNewPerLesson(Integer.parseInt(newPerLesson));

        Completable.fromAction(() -> db.getDeckDao().updateDeck(deck))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(()->{
                    editingDeckView.showMessage(R.string.on_success_message);
                });
    }

    private void setCards(List<FlashCard> cards){
        this.cards = cards;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    @SuppressLint("CheckResult")
    public void editCard(IEditingCardView view, int position, String front, String back){
        FlashCard card = cards.get(position);
        card.setObverse(front);
        card.setReverse(back);

        Completable.fromAction(() -> db.getCardDao().updateFlashCard(card))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(()->{
                    view.onSuccess();
                    editingDeckView.refreshData();
                });
    }

    public void setData(IEditingCardView view, int position){
        FlashCard card = cards.get(position);
        view.setBack(card.getReverse());
        view.setFront(card.getObverse());
    }

    @SuppressLint("CheckResult")
    public void deleteCard(int position, IWordItemView view){
        FlashCard card = cards.get(position);
        Completable.fromAction(() -> db.getCardDao().deleteFlashCard(card))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(()->{
                    cards.remove(card);
                    view.refreshCardsList();
                });
    }

    @SuppressLint("CheckResult")
    public void checkForUpdates(){
        db.getDeckDao().getDeck(deck.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x->{
                    setDeck(x);
                    editingDeckView.refreshData();
                });
    }

    public int getDeckId(){
        return deck.getId();
    }
}
