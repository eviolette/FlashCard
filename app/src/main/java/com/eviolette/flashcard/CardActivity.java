package com.eviolette.flashcard;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eviolette.flashcard.model.Card;
import com.eviolette.flashcard.model.Deck;
import com.eviolette.flashcard.model.Decks;
import com.eviolette.flashcard.model.Game;

import java.sql.SQLException;


public class CardActivity extends ActionBarActivity implements View.OnClickListener {
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //receives intent and sets the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        Intent intent = getIntent();
        Log.d(MainActivity.FLASHCARD, "Foo: " + intent.getStringExtra("deckName"));
        //stores correct incorrect and allows user to go to next card
        DeckStore deckStore = new DeckStore(this);
        try {
            deckStore.open();
            Deck deck = deckStore.findByName(intent.getStringExtra("deckName"));
            game = new Game(deck);
            if (game.hasNextCard()) {
                fillFromCard(game.nextCard());
            } else {
                gotoScore();
                finish();
            }
            Button b = (Button) findViewById(R.id.nextButton);
            b.setOnClickListener(this);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            deckStore.close();
        }
    }

    private void fillFromCard(Card card) {
        //fills card
        TextView textView = (TextView) findViewById(R.id.questionText);
        textView.setText(card.getQuestion());
        TextView view = (TextView) findViewById(R.id.answerEdit);
        view.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        TextView view = (TextView) findViewById(R.id.answerEdit);
        if (game.currentCard().matches(view.getText().toString())) {
            game.incrementScore();
        }
        if (game.hasNextCard()) {
            fillFromCard(game.nextCard());
        } else {
            game.nextCard();
            gotoScore();
        }
    }

    private void gotoScore() {
        //sends data to score activity
        Intent intent = new Intent(this, ScoreActivity.class);
        intent.putExtra("correct", game.numberCorrect());
        intent.putExtra("total", game.numberofcards());
        startActivity(intent);
        finish();
    }

    private void flash(String s) {
        TextView view = (TextView)findViewById(R.id.answerEdit);
        view.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_900));
        Log.d("FOO", s);
    }
}
