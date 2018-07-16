package com.example.kaysersose.fiszki;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kaysersose.fiszki.database.FiszkiDatabase;
import com.example.kaysersose.fiszki.database.entities.FlashCard;

import java.util.Date;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;


public class AddingCardsActivity extends AppCompatActivity {
    public EditText mFrontCard, mBackCard;
    public Button mAdd, mFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_cards);

        long deckID = getIntent().getLongExtra("DECK_ID", -1);
        Long l = new Long(deckID);
        int deckid = l.intValue();

        FiszkiDatabase db = FiszkiDatabase.getAppDatabase(this);

        mFrontCard = (EditText) findViewById(R.id.ev_card_front);
        mBackCard = (EditText) findViewById(R.id.ev_card_back);

        mAdd = (Button) findViewById(R.id.b_add_card);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View v) {
                Completable.fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        db.getCardDao().insertFlashCard(new FlashCard(0, deckid, mFrontCard.getText().toString(), mBackCard.getText().toString(), 0, new Date()));
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(()->{
                            clearFields();
                        });

            }
        });


        mFinish = (Button) findViewById(R.id.b_finish);
        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, null);
                finish();
            }
        });
    }


    private void clearFields(){
        mFrontCard.getText().clear();
        mBackCard.getText().clear();
    }

}
