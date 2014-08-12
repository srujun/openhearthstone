package com.srujun.openhearthstone.net.listeners;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.srujun.openhearthstone.OHGame;
import com.srujun.openhearthstone.net.packets.DeckManagerPacket;
import com.srujun.openhearthstone.screens.DeckManagerScreen;

import java.util.ArrayList;
import java.util.List;

public class DeckManagerListener extends Listener {

    @Override
    @SuppressWarnings("unchecked")
    public void received(Connection connection, Object object) {
        // If the packet sent is a list of something
        if(object instanceof List<?>) {
            List genericList = (ArrayList) object;

            // Make sure our list is not empty if we want to mess with it.
            if(!genericList.isEmpty()) {
                // If list is a list of decks
                if(genericList.get(0).getClass().equals(DeckManagerPacket.Deck.class)) {
                    // Cast the list to a list of Deck objects
                    final ArrayList<DeckManagerPacket.Deck> decks = (ArrayList<DeckManagerPacket.Deck>) genericList;
                    OHGame.log("Received " + decks.size() + " decks.");

                    // Ignore setting decks if the current screen isn't DeckManagerScreen.
                    if(OHGame.instance.getScreen().getClass().getSimpleName().equals(DeckManagerScreen.class.getSimpleName())) {
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                ((DeckManagerScreen) OHGame.instance.getScreen()).setViewDecksUI(decks);
                            }
                        });
                    }
                }

                // If list is a list of classes
                if(genericList.get(0).getClass().equals(DeckManagerPacket.Classs.class)) {
                    // Cast the list to a list of Classs objects
                    final ArrayList<DeckManagerPacket.Classs> classes = (ArrayList<DeckManagerPacket.Classs>) genericList;
                    OHGame.log("Received " + classes.size() + " classes.");

                    // Ignore setting decks if the current screen isn't DeckManagerScreen.
                    if(OHGame.instance.getScreen().getClass().getSimpleName().equals(DeckManagerScreen.class.getSimpleName())) {
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                ((DeckManagerScreen) OHGame.instance.getScreen()).setNewDeckClassesUI(classes);
                            }
                        });
                    }
                }
            }
        }

        // If the packet sent is a response to an EditDeck request
        if(object instanceof DeckManagerPacket.EditDeck) {
            final DeckManagerPacket.Classs classs = ((DeckManagerPacket.EditDeck) object).classs;
            final DeckManagerPacket.Deck deck = ((DeckManagerPacket.EditDeck) object).deck;
            final List<DeckManagerPacket.Card> cards = ((DeckManagerPacket.EditDeck) object).cards;

            // Ignore setting EditDeckUI if the current screen isn't DeckManagerScreen.
            if(OHGame.instance.getScreen().getClass().getSimpleName().equals(DeckManagerScreen.class.getSimpleName())) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        ((DeckManagerScreen) OHGame.instance.getScreen()).setEditDeckUI(classs, deck, cards);
                    }
                });
            }
        }
    }
}
