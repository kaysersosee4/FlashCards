package com.example.kaysersose.fiszki.addingcards;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kaysersose.fiszki.R;
import com.example.kaysersose.fiszki.database.FiszkiDatabase;
import com.example.kaysersose.fiszki.database.entities.FlashCard;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;


public class AddingCardsActivity extends AppCompatActivity implements IAddingCardsView{
    @BindView(R.id.ev_card_front)
    EditText mFrontCard;
    @BindView(R.id.ev_card_back)
    EditText mBackCard;

    private AddingCardsPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_cards);

        ButterKnife.bind(this);

        presenter = new AddingCardsPresenter(getDeckId(), this, this);
    }

    @OnClick(R.id.b_add_card)
    public void add(){
        String front = mFrontCard.getText().toString();
        String back = mBackCard.getText().toString();
        presenter.addCard(front, back);
    }


    @OnClick(R.id.b_finish)
    public void endAdding() {
        setResult(RESULT_OK, null);
        finish();
    }

    @Override
    public void clearFields() {
        mFrontCard.getText().clear();
        mBackCard.getText().clear();

    }

    int getDeckId(){
        long deckID = getIntent().getLongExtra("DECK_ID", -1);
        Long l = new Long(deckID);
        int deckId = l.intValue();
        return deckId;
    }


}
