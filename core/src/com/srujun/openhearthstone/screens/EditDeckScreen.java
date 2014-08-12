package com.srujun.openhearthstone.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.srujun.openhearthstone.OHGame;
import com.srujun.openhearthstone.net.packets.DeckManagerPacket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EditDeckScreen implements Screen {
    private ArrayList<DeckManagerPacket.Card> deckCardsList;
    private Comparator<DeckManagerPacket.Card> deckCardsComparator;

    private Group editDeckUIGroup;
    private Table editDeckCardsListTable;

    public EditDeckScreen() {
        this.deckCardsList = new ArrayList<DeckManagerPacket.Card>(30);
        this.deckCardsComparator = new Comparator<DeckManagerPacket.Card>() {
            @Override
            public int compare(DeckManagerPacket.Card card1, DeckManagerPacket.Card card2) {
                if(card1.cost > card2.cost)
                    return 1;
                else if(card1.cost < card2.cost)
                    return -1;
                else
                    return card1.name.compareTo(card2.name);
            }
        };

        // Edit Deck Group
        this.editDeckUIGroup = new Group();
        editDeckUIGroup.setName("EditDeckUIGroup");
        OHGame.instance.stage.addActor(editDeckUIGroup);
    }

    public void setEditDeckUI(DeckManagerPacket.Deck deck) {
        OHGame.log("EditDeckUI: Class=" + deck.classs.name + ", Name=" + deck.name + ", Cards=" + deck.cards.size());

        // editDeckCardsListTable
        editDeckCardsListTable = new Table(OHGame.instance.skin);
        editDeckCardsListTable.setName("DeckCardsListTable");
        editDeckCardsListTable.setWidth(260f);
        editDeckCardsListTable.padLeft(25f);
        editDeckCardsListTable.defaults().expand();

        // Populate the card list array.
        deckCardsList = (ArrayList<DeckManagerPacket.Card>) deck.cards;
        refreshEditDeckList();

        // Create a ScrollPane for editDeckCardsList
        final ScrollPane deckCardsListPane = new ScrollPane(editDeckCardsListTable, OHGame.instance.skin);
        deckCardsListPane.setName("DeckCardsListPane");
        deckCardsListPane.getStyle().background = new BaseDrawable();
        deckCardsListPane.setClamp(true);
        deckCardsListPane.setFadeScrollBars(true);
        deckCardsListPane.setFlickScroll(true);
        deckCardsListPane.setSize(310f, 855f);
        deckCardsListPane.setPosition(285f, 20f);
        deckCardsListPane.setVisible(false);
        editDeckUIGroup.addActor(deckCardsListPane);

        // Create a button for the deck info.
        TextButton deckInfoButton = new TextButton(deck.classs.name + ": " + deck.name, OHGame.instance.skin);
        deckInfoButton.setName("DeckInfoButton");
        deckInfoButton.setSize(310f, 64f);
        deckInfoButton.setPosition(285f, OHGame.HEIGHT - 74f);
        deckInfoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                deckCardsListPane.setVisible(!deckCardsListPane.isVisible());
            }
        });
        editDeckUIGroup.addActor(deckInfoButton);

        // Create a ScrollPane for possible cards that can be added.
    }

    private void refreshEditDeckList() {
        // Sort deckCardsList.
        Collections.sort(deckCardsList, deckCardsComparator);

        for(DeckManagerPacket.Card card : deckCardsList) {
            // Search through editDeckCardsListTable and see if this card is already in it.
            boolean isCardAlreadyInTable = false;
            for(Actor cardContainerActor : editDeckCardsListTable.getChildren()) {
                if(cardContainerActor.getName().equals("CardButton:\"" + card.name + "\"")) {
                    isCardAlreadyInTable = true;
                    TextButton cardButton = (TextButton) cardContainerActor;
                    cardButton.setText(cardButton.getText() + " x2");
                    break;
                }
            }

            if(!isCardAlreadyInTable) {
                Label costLabel = new Label("" + card.cost, OHGame.instance.skin);
                costLabel.setName("CardCostLabel:\"" + card.name + "\"");
                editDeckCardsListTable.add(costLabel).size(10f, 70f).padRight(5f);

                TextButton cardButton = new TextButton(card.name, OHGame.instance.skin);
                cardButton.setName("CardButton:\"" + card.name + "\"");
                editDeckCardsListTable.add(cardButton).size(245f, 70f).pad(5f, 0f, 5f, 0f);
                editDeckCardsListTable.row();
            }
        }
    }

    @Override
    public void render(float delta) {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
