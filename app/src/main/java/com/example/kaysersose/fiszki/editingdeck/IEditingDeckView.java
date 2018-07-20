package com.example.kaysersose.fiszki.editingdeck;

import com.example.kaysersose.fiszki.database.entities.Deck;

public interface IEditingDeckView {
    void refreshData();
    void showDeckInfo(Deck deck);
    void onPresenterPrepared();
    void showMessage(int message);

}
