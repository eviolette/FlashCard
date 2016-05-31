package com.eviolette.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eviolette.flashcard.model.Card;
import com.eviolette.flashcard.model.Deck;

import java.sql.SQLException;


public class EditCardActivity extends ActionBarActivity implements View.OnClickListener {
    private DeckStore deckStore;
    private Deck.Builder deckBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deckStore = new DeckStore(this);
        setContentView(R.layout.activity_edit_card);
        Intent intent = getIntent();
        deckBuilder = Deck.builder();
        deckBuilder.name(intent.getStringExtra("deckName"));
        Button b = (Button) findViewById(R.id.saveDeckButton);
        b.setOnClickListener(this);
        b = (Button) findViewById(R.id.addAnotherCardButton);
        b.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_card, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.saveDeckButton) {
            Deck deck = deckBuilder.build();
            try {
                deckStore.open();
                deckStore.saveDeck(deck);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            deckStore.close();
            finish();
        } else {
            // other button click
            //sets question and answer
            TextView question = (TextView)findViewById(R.id.editCardQuestion);
            TextView answer = (TextView)findViewById(R.id.editCardAnswer);
            deckBuilder.addCard(new Card(question.getText().toString(), answer.getText().toString()));
            question.clearComposingText();
            answer.clearComposingText();
            question.setText("");
            answer.setText("");
        }
    }
}
