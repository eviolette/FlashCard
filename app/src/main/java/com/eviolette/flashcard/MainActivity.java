package com.eviolette.flashcard;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.eviolette.flashcard.model.Deck;
import com.eviolette.flashcard.model.Decks;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    public static final String FLASHCARD = "FLASHCARD";
    private ArrayAdapter<String> adapter;
    private DeckStore deckStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(FLASHCARD, "CREATE");
        setContentView(R.layout.activity_main);
        //Create listView of decks to display
        ListView listView = (ListView) findViewById(R.id.decks);
        //Create deckStore instance for database
        //Set adapter to the list of deck names
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, new LinkedList());
        deckStore = new DeckStore(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        Log.d(FLASHCARD, "Resume");
        adapter.clear();
        try {
            deckStore.open();
            for (Deck deck : deckStore.findDecks()) {
                adapter.add(deck.getName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            deckStore.close();
        }
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.d(FLASHCARD, "On click" + item.toString());

        //two menu options: add deck and delete deck; when clicked switches to respective screen
        if (id == R.id.action_add_deck) {
            startAddDeck();
            return true;
        }else if (id == R.id.action_delete_deck){
            try {
                deckStore.open();
                deckStore.deleteAll();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                deckStore.close();
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void startAddDeck() {
        Intent intent = new Intent(this, EditDeckActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //When deck clicked, use adapter to check which deck clicked, then use intents to switch to CardActivity
        String deckName = adapter.getItem(position);
        Log.d(FLASHCARD, "On Click " + deckName);
        Intent intent = new Intent(this, CardActivity.class);
        intent.putExtra("deckName", deckName);
        startActivity(intent);
    }

}
