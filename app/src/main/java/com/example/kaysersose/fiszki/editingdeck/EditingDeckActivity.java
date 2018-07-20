package com.example.kaysersose.fiszki.editingdeck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaysersose.fiszki.R;
import com.example.kaysersose.fiszki.addingcards.AddingCardsActivity;
import com.example.kaysersose.fiszki.database.entities.Deck;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditingDeckActivity extends AppCompatActivity implements IEditingDeckView{
    private CardListAdapter adapter;
    private CardListPresenter presenter;

    @BindView(R.id.et_name)
    TextView mName;
    @BindView(R.id.et_new_per_lesson)
    TextView mNewPerLesson;

    @BindView(R.id.b_confirm)
    Button bConfirm;
    @BindView(R.id.b_cancel_editing)
    Button bCancel;
    @BindView(R.id.b_add_cards)
    Button bAdd;

    @BindView(R.id.rv_words)
    RecyclerView mWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_deck);

        ButterKnife.bind(this);


        mWords.setHasFixedSize(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mWords.setLayoutManager(mLayoutManager);

        presenter = new CardListPresenter(getDeckId(), this, this);
    }



   @OnClick(R.id.b_add_cards)
    public void startAddingCardsActivity(){
        Intent intent = new Intent(this, AddingCardsActivity.class);
        intent.putExtra("DECK_ID", Long.valueOf(presenter.getDeckId()));
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        presenter.checkForUpdates();
    }

    int getDeckId(){
        int deckID = getIntent().getIntExtra("DECK_ID", -1);
        return deckID;
    }

    @Override
    public void refreshData() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showDeckInfo(Deck deck) {
        mName.setText(deck.getName());
        mNewPerLesson.setText(String.valueOf(deck.getNewPerLesson()));
    }


    @Override
    public void onPresenterPrepared() {
        bConfirm.setOnClickListener(v -> presenter.changeDeck(mName.getText().toString(), mNewPerLesson.getText().toString()));
        bCancel.setOnClickListener(v -> finish());

        adapter = new CardListAdapter(presenter);
        mWords.setAdapter(adapter);
    }

    @Override
    public void showMessage(int message) {
        Toast.makeText(this, getString(message), Toast.LENGTH_LONG).show();
    }
}
