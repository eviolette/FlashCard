package com.eviolette.flashcard.model;

/**
 * Created by lab228 on 4/30/2015.
 */
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by eviolette on 4/20/15.
 */
public class Deck implements Serializable {
    private String name;
    private List<Card> cards;

    private Deck(String name, List<Card> cards) {
        this.cards = cards;
        this.name = name;
    }

    public List<Card> getCards() {
        return cards;
    }

    public String getName() {
        return name;
    }

    public int total() {
        return cards.size();
    }

    public static class Builder implements Serializable {
        private String name;
        private List<Card> cards = new LinkedList();

        public Builder() {}

        public String getName() {
            return name;
        }
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder addCard(Card card) {
            cards.add(card);
            return this;
        }

        public Deck build() {
            return new Deck(name, cards);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
