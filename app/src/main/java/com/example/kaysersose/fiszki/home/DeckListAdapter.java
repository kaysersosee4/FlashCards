package com.example.kaysersose.fiszki.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaysersose.fiszki.editingdeck.EditingDeckActivity;
import com.example.kaysersose.fiszki.learning.LearningFromDeckActivity;
import com.example.kaysersose.fiszki.R;
import com.example.kaysersose.fiszki.database.FiszkiDatabase;
import com.example.kaysersose.fiszki.database.entities.Deck;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DeckListAdapter extends RecyclerView.Adapter<DeckListAdapter.MyViewHolder> {
    private List<Deck> decks;


    static class MyViewHolder extends RecyclerView.ViewHolder{
        public Deck deck;
        public DeckListAdapter adapter;

        @BindView(R.id.tv_deckname)
        TextView deckName;
        @BindView(R.id.tv_new_count)
        TextView newCount;
        @BindView(R.id.tv_repeat_count)
        TextView repeatCount;
        @BindView(R.id.b_delete)
        Button bDelete;
        @BindView(R.id.b_edit)
        Button bEdit;


        public MyViewHolder(View view, DeckListAdapter adapter){
            super(view);

            deck = null;
            this.adapter = adapter;

            ButterKnife.bind(this,view);
        }


        @OnClick(R.id.tv_deckname)
        public void startLearningActivity(View v){
            Intent intent = new Intent(v.getContext(), LearningFromDeckActivity.class);
            intent.putExtra("DECK_ID", deck.getId());

            v.getContext().startActivity(intent);
        }

        @OnClick(R.id.b_delete)
        public void showDeleteAlertDialog(View v){
            AlertDialog ad = getDeleteAlertDialog(v.getContext(), deck);
            ad.show();
        }

        @OnClick(R.id.b_edit)
        public void startEditDeckActivity(View v){
            Intent intent = new Intent(v.getContext(), EditingDeckActivity.class);
            intent.putExtra("DECK_ID", deck.getId());

            v.getContext().startActivity(intent);
        }


        public TextView getNewCount() {return newCount;}
        public TextView getRepeatCount() {return repeatCount;}




        private AlertDialog getDeleteAlertDialog(Context context, Deck deck){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

            alertDialogBuilder.setTitle(R.string.delete_confirmation);

            alertDialogBuilder
                    .setMessage("Do you want to delete "+deck.getName()+" deck?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (DialogInterface dialog, int id) ->{
                        FiszkiDatabase db = FiszkiDatabase.getAppDatabase(context);


                        Completable.fromAction(() -> db.getDeckDao().deleteDeck(deck))
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new CompletableObserver() {
                                    @Override
                                    public void onSubscribe(Disposable d) {}

                                    @Override
                                    public void onComplete() {
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(context, "error", Toast.LENGTH_LONG).show();
                                    }
                                });
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        dialog.cancel();
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            return alertDialog;
        }
    }

    public DeckListAdapter(List<Deck> decks){
        this.decks = decks;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deck_list_item, parent, false);

        return new MyViewHolder(itemView, this);
    }



    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        FiszkiDatabase db = FiszkiDatabase.getAppDatabase(context);
        holder.deck = decks.get(position);
        holder.deckName.setText(holder.deck.getName());
        holder.deckName.append(String.valueOf(holder.deck.getId()));


        db.getCardDao().getNewCount(holder.deck.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x-> holder.getNewCount().setText(String.valueOf(x)));

        db.getCardDao().getRepeatCount(holder.deck.getId(), new Date())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x-> holder.getRepeatCount().setText(String.valueOf(x)));
    }

    @Override
    public int getItemCount() {
        return decks.size();
    }

    public List<Deck> getDecks() {
        return decks;
    }

    public void setDecks(List<Deck> decks) {
        this.decks = decks;
    }
}
