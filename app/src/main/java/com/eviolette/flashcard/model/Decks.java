package com.eviolette.flashcard.model;

/**
 * Created by lab228 on 5/11/2015.
 */
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

public class Decks {

    private static Map<String, Deck> decks = new HashMap<String, Deck>();

    static {

        decks.put("Chinese", Deck.builder()
                .name("Chinese")
                .addCard(new Card("What is your name", "a"))
                .addCard(new Card("What is foo", "b"))
                .addCard(new Card("What is bar", "c"))
                .build());
    }

    public static Collection<Deck> findAll() {
        return decks.values();
    }

    public static Deck findByName(String deckName) {
        return decks.get(deckName);
    }
}
