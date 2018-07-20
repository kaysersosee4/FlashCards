package com.example.kaysersose.fiszki.editingcard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kaysersose.fiszki.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditingCardActivity extends AppCompatActivity implements IEditingCardView {
    int cardId;
    EditingCardPresenter presenter;

    @BindView(R.id.et_frontcard)
    EditText mFront;
    @BindView(R.id.et_backcard)
    EditText mBack;
    @BindView(R.id.b_cancel)
    Button mCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_card);

        ButterKnife.bind(this);
        mCancel.setOnClickListener(v->finish());

        Intent intent = getIntent();

        cardId = intent.getIntExtra("CARD_ID", -1);
        Log.d("SS", ""+cardId);

        presenter = new EditingCardPresenter(this, this, cardId);
    }


    @OnClick(R.id.b_confirm)
    public void changeCard(){
       presenter.updateCard(mFront.getText().toString(), mBack.getText().toString());
    }

    @Override
    public void setFront(String front) {
        mFront.setText(front);
    }

    @Override
    public void setBack(String back) {
        mBack.setText(back);
    }

    @Override
    public void onSuccess() {
        finish();
    }
}
