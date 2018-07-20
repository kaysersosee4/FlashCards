package com.example.kaysersose.fiszki.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.example.kaysersose.fiszki.addingdeck.AddingDeckActivity;
import com.example.kaysersose.fiszki.R;
import com.example.kaysersose.fiszki.database.FiszkiDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    private DeckListAdapter mAdapter;
    private FiszkiDatabase db;

    @BindView(R.id.b_add_deck)
    Button addDeck;
    @BindView(R.id.rv_decks)
    RecyclerView mRecyclerView;

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshRecycleView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        addDeck.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AddingDeckActivity.class);
            startActivity(intent);
        });

        db = FiszkiDatabase.getAppDatabase(this);

        setupRecyclerView();
    }

    @SuppressLint("CheckResult")
    private void refreshRecycleView(){
        DeckListAdapter adapter = (DeckListAdapter) mRecyclerView.getAdapter();

        db.getDeckDao().getAllDecks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x->{
           adapter.setDecks(x);
           adapter.notifyDataSetChanged();
        });
    }

    @SuppressLint("CheckResult")
    private void setupRecyclerView(){

        mRecyclerView.setHasFixedSize(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        db.getDeckDao().getAllDecks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> {
                    mAdapter = new DeckListAdapter(x);
                    mRecyclerView.setAdapter(mAdapter);
                });


    }
}
