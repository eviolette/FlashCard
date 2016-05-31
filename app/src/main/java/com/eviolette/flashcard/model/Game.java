package com.eviolette.flashcard.model;

import java.util.Iterator;

/**
 * Created by lab228 on 4/30/2015.
 */
public class Game {
    private int numCorrect, position;
    private Deck deck;
    private Iterator nextCard;
    private Card card;

    public Game(Deck deck) {
        this.numCorrect = numCorrect;
        this.position = position;
        this.deck = deck;
        nextCard = deck.getCards().iterator();
        card = null;
    }

    public Card nextCard() {
        if (nextCard.hasNext()) {
            card = (Card) nextCard.next();
            return card;
        }
        return null;
    }

    public int numberCorrect() {
        return numCorrect;
    }

    public void incrementScore() {
        numCorrect++;
    }

    public Card currentCard() {
        return card;
    }

    public boolean hasNextCard() {
        return nextCard.hasNext();
    }

    public int numberofcards() {
        return deck.getCards().size();
    }
}
