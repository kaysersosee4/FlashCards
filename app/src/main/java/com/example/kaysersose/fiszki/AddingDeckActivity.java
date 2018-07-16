package com.example.kaysersose.fiszki;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kaysersose.fiszki.database.FiszkiDatabase;
import com.example.kaysersose.fiszki.database.entities.Deck;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class AddingDeckActivity extends AppCompatActivity {
    private static int REQUEST_EXIT = 1;
    private static int BLANK_ID = 0;

    @BindView(R.id.et_deck_name)
    EditText mName;
    @BindView(R.id.et_new_per_lesson)
    EditText mNewPerLesson;
    @BindView(R.id.b_add)
    Button mAdd;
    @BindView(R.id.b_cancel)
    Button mCancel;

    @OnClick(R.id.b_add)
    public void addDeck(View v){
        FiszkiDatabase db = FiszkiDatabase.getAppDatabase(v.getContext());

        int newPerLesson = Integer.parseInt(mNewPerLesson.getText().toString());
        String name = mName.getText().toString();

        Observable.fromCallable(() -> db.getDeckDao()
                .insertDeck(new Deck(BLANK_ID, name, newPerLesson)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(Long aLong) {
                        startAddingCardsActivity(aLong, v);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(),"Duplicate name, change it!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {}
                });
    }

    private void startAddingCardsActivity(Long aLong, View v){
        Intent intent = new Intent(v.getContext(), AddingCardsActivity.class);
        intent.putExtra("DECK_ID", aLong);

        startActivityForResult(intent, REQUEST_EXIT);
    }
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EXIT) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_deck);

        ButterKnife.bind(this);
        mCancel.setOnClickListener(v -> finish());



    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }



}
