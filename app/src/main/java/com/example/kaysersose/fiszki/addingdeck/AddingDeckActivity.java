package com.example.kaysersose.fiszki.addingdeck;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kaysersose.fiszki.R;
import com.example.kaysersose.fiszki.addingcards.AddingCardsActivity;
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


public class AddingDeckActivity extends AppCompatActivity implements IAddingDeckView{
    private static int REQUEST_EXIT = 1;

    private AddingDeckPresenter presenter;

    @BindView(R.id.et_deck_name)
    EditText mName;
    @BindView(R.id.et_new_per_lesson)
    EditText mNewPerLesson;
    @BindView(R.id.b_add)
    Button mAdd;
    @BindView(R.id.b_cancel)
    Button mCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_deck);

        ButterKnife.bind(this);
        presenter = new AddingDeckPresenter(this, this);

        mCancel.setOnClickListener(v -> finish());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
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


    @OnClick(R.id.b_add)
    public void addDeck(View v){
        String newPerLesson = mNewPerLesson.getText().toString();
        String name = mName.getText().toString();

        presenter.addDeck(name, newPerLesson);
    }


    @Override
    public void addCards(Long id) {
        Intent intent = new Intent(this, AddingCardsActivity.class);
        intent.putExtra("DECK_ID", id);

        startActivityForResult(intent, REQUEST_EXIT);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show();
    }
}
