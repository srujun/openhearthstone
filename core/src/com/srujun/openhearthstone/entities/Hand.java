package com.srujun.openhearthstone.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.srujun.openhearthstone.OHGame;

import java.util.Iterator;

public class Hand implements Iterable<Card> {
    private Array<Card> cards = new Array<Card>();
    private Array<Rectangle> renderedBox = new Array<Rectangle>();

    public Hand() {

    }

    public Hand(Array<Card> initCards) {
        this.cards = initCards;
    }

    public void update() {
        float renderSpace = OHGame.WIDTH / cards.size;
    }

    public void add(Card newCard) {
        this.cards.add(newCard);
    }

    public int size() {
        return cards.size;
    }

    public Card getCard(int index) {
        return cards.get(index);
    }

    @Override
    public Iterator<Card> iterator() {
        return cards.iterator();
    }
}
