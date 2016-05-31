package com.eviolette.flashcard;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.eviolette.flashcard.model.Card;
import com.eviolette.flashcard.model.Deck;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by eviolette on 5/19/15.
 */
public class DeckStore {
    private final DbManager helper;
    private SQLiteDatabase database;

    public DeckStore(Context context) {
        helper = new DbManager(context);
    }

    public void open() throws SQLException {
        database = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }


    public List<Deck> findDecks() {
        Map<String, Deck.Builder> decks = new HashMap<String, Deck.Builder>();
        Cursor cursor = database.query("deck", null, null, null, null, null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Deck.Builder deck = deckBuilderFromCursor(cursor);
                decks.put(deck.getName(), deck);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        List<Deck> list = new LinkedList<>();
        for (Deck.Builder builder : decks.values()) {
            Deck built = builder.build();
            if (!TextUtils.isEmpty(built.getName())) {
                list.add(built);
            }
        }
        return list;
    }

    private Deck.Builder deckBuilderFromCursor(Cursor cursor) {
        Deck.Builder builder = Deck.builder();
        builder.name(cursor.getString(cursor.getColumnIndex("name")));
        return builder;
    }

    public void saveDeck(Deck deck) {
        database.execSQL("delete from deck where name = ?", new Object[] {deck.getName()});
        database.execSQL("delete from card where deck_name = ?", new Object[] {deck.getName()});

        for (Card card : deck.getCards()) {
            database.execSQL("insert into card (answer, question, deck_name) values (?, ?, ?)", new Object[] {card.getAnswer(), card.getQuestion(), deck.getName()});
        }
        database.execSQL("insert into deck (name) values (?)", new Object[] { deck.getName()});
    }

    public Deck findByName(String name) {
        Cursor cursor = database.query("card", null, "deck_name = ?", new String[] {name }, null, null, null);
        Deck.Builder deck = Deck.builder();
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Card card = cardFromCursor(cursor);
                deck.addCard(card);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return deck.build();
    }

    private Card cardFromCursor(Cursor cursor) {
        String question = cursor.getString(cursor.getColumnIndex("question"));
        String answer = cursor.getString(cursor.getColumnIndex("answer"));
        return new Card(question, answer);
    }

    public void deleteAll() {
        database.execSQL("delete from deck");
    }
}
