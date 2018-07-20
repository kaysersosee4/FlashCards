package com.example.kaysersose.fiszki.learning;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kaysersose.fiszki.R;
import com.example.kaysersose.fiszki.database.FiszkiDatabase;
import com.example.kaysersose.fiszki.database.entities.FlashCard;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.view.View.VISIBLE;

public class LearningFromDeckActivity extends AppCompatActivity {
    @BindView(R.id.tv_przod)
    TextView mPrzod;
    @BindView(R.id.tv_tyl)
    TextView mTyl;
    @BindView(R.id.tv_info_tv)
    TextView mInfo;

    @BindView(R.id.b_latwe)
    Button mLatwe;
    @BindView(R.id.b_srednie)
    Button mSrednie;
    @BindView(R.id.b_trudne)
    Button mTrudne;
    @BindView(R.id.b_pokaz_odpowiedz)
    Button mPokazOdpowiedz;
    @BindView(R.id.b_powrot)
    Button mPowrot;

    List<FlashCard> cards;
    FiszkiDatabase db;


    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_drom_deck);

        ButterKnife.bind(this);

        mLatwe.setOnClickListener(v -> onEasyButtonClicked());

        mSrednie.setOnClickListener(v -> onMediumButtonClicked());

        mTrudne.setOnClickListener(v -> onHardButtonClicked());

        mPokazOdpowiedz.setOnClickListener(v -> showReverse());

        mPowrot.setOnClickListener(v -> finish());

        hideAll();

        db = FiszkiDatabase.getAppDatabase(this);

        int deckID = getIntent().getIntExtra("DECK_ID", -1);

        db.getCardDao().getLearningCardsFromDeck(deckID, new Date())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x->{
                    setCards(x);

                    if(cards==null || cards.isEmpty()){
                        onEmptyCardList();
                    }else{
                        showLearningStuff();
                        learnFirst();
                 }
        });
    }

    @SuppressLint("CheckResult")
    private void onEasyButtonClicked(){
        FlashCard card = cards.get(0);
        int daysToNextRevision = card.getDaysToNextRevision();

        if(daysToNextRevision==0){
            card.setDaysToNextRevision(1);
        }else{
            card.setDaysToNextRevision(2*daysToNextRevision);
        }

        card.setLastRevision(new Date());

        Completable.fromAction(() -> db.getCardDao().updateFlashCard(card))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ()->{
                            cards.remove(0);
                            if(cards.isEmpty()){
                                onEmptyCardList();
                            }else{
                                learnFirst();
                            }
                        }
                );
    }

    private void onMediumButtonClicked(){
        Collections.shuffle(cards);
        learnFirst();

    }

    @SuppressLint("CheckResult")
    private void onHardButtonClicked(){
        FlashCard card = cards.get(0);
        if(card.getDaysToNextRevision()>1){
            card.setDaysToNextRevision(1);
        }
        else{
            card.setDaysToNextRevision(0);
        }

        Completable.fromAction(() -> db.getCardDao().updateFlashCard(card))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ()->{
                            Collections.shuffle(cards);
                            learnFirst();
                        }
                );
    }

    private void learnFirst(){
        hideReverse();
        mPrzod.setText(cards.get(0).getObverse());
        mTyl.setText(cards.get(0).getReverse());
    }

    private void onEmptyCardList(){
        hideAll();
        showInfo(getResources().getString(R.string.no_cards_to_learn));
    }



    private void hideAll(){
        mPrzod.setVisibility(View.INVISIBLE);
        mTyl.setVisibility(View.INVISIBLE);
        mInfo.setVisibility(View.INVISIBLE);
        mLatwe.setVisibility(View.INVISIBLE);
        mSrednie.setVisibility(View.INVISIBLE);
        mTrudne.setVisibility(View.INVISIBLE);
        mPokazOdpowiedz.setVisibility(View.INVISIBLE);
        mPowrot.setVisibility(View.INVISIBLE);
    }

    private void showLearningStuff(){
        mPrzod.setVisibility(VISIBLE);
        mPokazOdpowiedz.setVisibility(VISIBLE);
        mLatwe.setVisibility(VISIBLE);
        mTrudne.setVisibility(VISIBLE);
        mSrednie.setVisibility(VISIBLE);
        mPowrot.setVisibility(VISIBLE);
    }

    private void showReverse(){
        mPokazOdpowiedz.setVisibility(View.INVISIBLE);
        mTyl.setVisibility(VISIBLE);
    }

    private void hideReverse(){
        mTyl.setVisibility(View.INVISIBLE);
        mPokazOdpowiedz.setVisibility(VISIBLE);
    }

    private void showInfo(String info){
        mInfo.setText(info);
        mInfo.setVisibility(VISIBLE);
        mPowrot.setVisibility(VISIBLE);
    }

    public void setCards(List<FlashCard> cards) {
        this.cards = cards;
    }
}
