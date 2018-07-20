package com.example.kaysersose.fiszki.editingdeck;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kaysersose.fiszki.R;
import com.example.kaysersose.fiszki.editingcard.EditingCardActivity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.WordViewHolder> {
    private CardListPresenter presenter;

    public CardListAdapter(CardListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.word_list_item, parent, false);

        return new CardListAdapter.WordViewHolder(itemView,this,presenter);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        presenter.onBindWordItemViewAtPosition(position, holder);
    }

    @Override
    public int getItemCount() {
        return presenter.getWordsItemsCount();
    }

    public class WordViewHolder extends RecyclerView.ViewHolder implements IWordItemView{
        private final CardListPresenter presenter;
        private final CardListAdapter adapter;

        @BindView(R.id.tv_front)
        TextView mFront;
        @BindView(R.id.tv_back)
        TextView mBack;




        public WordViewHolder(View itemView, CardListAdapter adapter, CardListPresenter presenter) {
            super(itemView);
            this.adapter = adapter;
            this.presenter = presenter;
            ButterKnife.bind(this, itemView);


        }

        @OnClick(R.id.tv_edit)
        public void startEditingCardActivity(View v){
            Intent intent = new Intent(v.getContext(), EditingCardActivity.class);

            intent.putExtra("CARD_ID", presenter.getCardId(getAdapterPosition()));

            v.getContext().startActivity(intent);
        }

        @OnClick(R.id.tv_delete)
        public void delete(){
            presenter.deleteCard(getAdapterPosition(), this);
        }

        @Override
        public void setCardFront(String front) {
            mFront.setText(front);
        }

        @Override
        public void setCardBack(String back) {
            mBack.setText(back);
        }

        @Override
        public void refreshCardsList() {
            adapter.notifyDataSetChanged();
        }
    }
}
