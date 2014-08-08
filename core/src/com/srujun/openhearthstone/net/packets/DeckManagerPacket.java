package com.srujun.openhearthstone.net.packets;

import java.util.List;

public class DeckManagerPacket {
    static public class GetDecks {
        public GetDecks() {}
    }

    static public class GetClasses {
        public GetClasses() {}
    }

    static public class NewDeck {
        public Deck deck;

        public NewDeck() {}
        public NewDeck(String deckName, int heroId) {
            this.deck = new Deck();
            this.deck.name = deckName;
            this.deck.heroId = heroId;
        }
    }

    static public class Deck {
        public String name;
        public int heroId;
        public String hero;
        public List<Integer> cards;
    }

    static public class Classs {
        public String name;
        public int heroId;
        public int classId;
        public String heroImage;
    }
}
