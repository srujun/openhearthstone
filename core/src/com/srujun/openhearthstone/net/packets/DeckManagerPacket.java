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

    static public class EditDeck {
        public String name;

        public Classs classs;
        public Deck deck;
        public List<Card> cards;

        public EditDeck() {}
        public EditDeck(String deckName) {
            this.name = deckName;
        }
    }

    static public class Deck {
        public String name;
        public int heroId;
        public String hero;
        public List<Integer> cardsIds;

        public Deck() {}
    }

    static public class Classs {
        public String name;
        public int heroId;
        public int classs;
        public String heroImage;

        public Classs() {}
    }

    static public class Card {
        public enum CardType {
            MINION, SPELL, WEAPON
        }

        public int id;
        public String image;
        public int type;
        public int quality;
        public String name;
        public String description;
        public int classs;
        public int race;
        public int durability;
        public int cost;
        public int attack;
        public int health;

        public Card() {}
        public Card(CardType type) {
            switch(type) {
                case MINION:
                    this.type = 4;
                    this.durability = -1;
                    break;
                case SPELL:
                    this.type = 5;
                    this.durability = -1;
                    this.race = -1;
                    this.attack = -1;
                    this.health = -1;
                    break;
                case WEAPON:
                    this.type = 7;
                    this.race = -1;
                    this.health = -1;
                    break;
            }
        }
    }
}
