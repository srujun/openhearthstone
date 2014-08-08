package com.srujun.openhearthstone.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.srujun.openhearthstone.OHGame;
import com.srujun.openhearthstone.net.packets.DeckManagerPacket;

import java.util.ArrayList;
import java.util.HashMap;

public class DeckManagerScreen implements Screen {
    private enum States {
        VIEW_DECKS, NEW_DECK_CLASS, NEW_DECK_NAME, EDIT_DECK
    }

    private HashMap<TextButton, DeckManagerPacket.Deck> decksMap;
    private HashMap<Table, DeckManagerPacket.Classs> classesMap;

    private Group deckManagerUIGroup;

    private Group decksButtonsGroup;
    private TextButton newDeckButton;
    private Table newDeckClassesTable;
    private Window newDeckNameWindow;
    private Group editDeckGroup;

    public DeckManagerScreen() {
        this.decksMap = new HashMap<TextButton, DeckManagerPacket.Deck>(9);
        this.classesMap = new HashMap<Table, DeckManagerPacket.Classs>(9);

        this.deckManagerUIGroup = new Group();
        deckManagerUIGroup.setName("DeckManagerUIGroup");
        OHGame.instance.stage.addActor(deckManagerUIGroup);

        // Decks Buttons group
        this.decksButtonsGroup = new Group();
        decksButtonsGroup.setName("DecksButtonsGroup");
        deckManagerUIGroup.addActor(decksButtonsGroup);

        // New Deck Button
        this.newDeckButton = new TextButton("New Deck", OHGame.instance.skin);
        newDeckButton.setName("NewDeckButton");
        newDeckButton.setSize(480f, 120f);
        newDeckButton.setPosition(OHGame.WIDTH / 2 - newDeckButton.getWidth() / 2, 20f);
        newDeckButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeState(States.NEW_DECK_CLASS);
            }
        });
        deckManagerUIGroup.addActor(newDeckButton);

        // New Deck Classes table
        this.newDeckClassesTable = new Table(OHGame.instance.skin);
        newDeckClassesTable.setName("ClassesTable");
        newDeckClassesTable.setPosition(OHGame.WIDTH / 2, OHGame.HEIGHT / 2);
        newDeckClassesTable.setVisible(false);
        deckManagerUIGroup.addActor(newDeckClassesTable);

        // New Deck name window
        this.newDeckNameWindow = new Window("", OHGame.instance.skin);

        // Edit Deck Group
        this.editDeckGroup = new Group();
        editDeckGroup.setName("EditDeckGroup");

        changeState(States.VIEW_DECKS);
    }

    public void setViewDecksUI(ArrayList<DeckManagerPacket.Deck> decks) {
        // Clear the existing deck buttons group
        decksButtonsGroup.clear();

        for(int i = 0; i < decks.size(); i++) {
            DeckManagerPacket.Deck deck = decks.get(i);

            TextButton button = new TextButton(deck.hero + ": " + deck.name, OHGame.instance.skin);
            button.setName("ClassButton:" + deck.name);
            button.setSize(355f, 64f);
            button.setPosition(OHGame.WIDTH/2 - button.getWidth()/2, OHGame.HEIGHT - (100f + 86f*i));
            decksButtonsGroup.addActor(button);
            decksMap.put(button, deck);
        }
    }

    public void setNewDeckClassesUI(ArrayList<DeckManagerPacket.Classs> classes) {
        for(int i = 0; i < 9; i++) {
            DeckManagerPacket.Classs classs = classes.get(i);

            // Create a nested table for each class
            Table classNestedTable = new Table(OHGame.instance.skin);
            classNestedTable.setName("ClassNestedTable:" + classs.name);
            classNestedTable.defaults().expand().fill();
            // Add a click listener for the nested table
            classNestedTable.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // User wants to create a deck of this class!
                    changeState(States.NEW_DECK_NAME);
                    setNewDeckNameUI(classesMap.get(event.getListenerActor()));
                }
            });

            // Create the class's ImageButton
            Drawable image = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(
                    "textures/heroes_full/" + classs.heroImage + ".png"))));
            ImageButton classButton = new ImageButton(image);
            classButton.setName("ClassButton:" + classs.name);
            classNestedTable.add(classButton).size(160f, 160f).pad(25f, 7.5f, 4f, 7.5f);

            classNestedTable.row();

            // Create the class's name label
            Label nameLabel = new Label(classs.name, OHGame.instance.skin);
            nameLabel.setName("ClassNameLabel:" + classs.name);
            classNestedTable.add(nameLabel).pad(0f, 10f, 15f, 10f).center(); // 10f for top and down is arbitrary

            // Add the nested table to the main Classes table, and the map
            newDeckClassesTable.add(classNestedTable);
            classesMap.put(classNestedTable, classs);

            if((i+1)%3 == 0) { // Create a row after every 3rd classNestedTable
                newDeckClassesTable.row();
            }
        }
    }

    private void setNewDeckNameUI(final DeckManagerPacket.Classs newDeckClass) {
        // Create a Window for the name of the new deck.
        newDeckNameWindow = new Window("New " + newDeckClass.name + " Deck", OHGame.instance.skin);
        newDeckNameWindow.setName("NewDeckNameWindow");
        newDeckNameWindow.setSize(400f, 64f);
        newDeckNameWindow.padLeft(15f).padRight(15f);
        newDeckNameWindow.setPosition(OHGame.WIDTH / 2 - newDeckNameWindow.getWidth() / 2, OHGame.HEIGHT / 2 - newDeckNameWindow.getHeight() / 2);

        // Create a TextField where the user will enter the name of the deck
        final TextField deckNameField = new TextField("", OHGame.instance.skin);
        deckNameField.setName("NewDeckNameField");
        deckNameField.setMessageText("Enter deck name...");
        newDeckNameWindow.add(deckNameField);

        // Create a TextButton to submit the name of the deck
        TextButton createDeckButton = new TextButton("Create!", OHGame.instance.skin);
        createDeckButton.setName("CreateNewDeckButton");
        createDeckButton.padLeft(10f);
        createDeckButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                deckNameField.getOnscreenKeyboard().show(false);

                if (deckNameField.getText().isEmpty()) {
                    newDeckNameWindow.setTitle("New " + newDeckClass.name + " Deck: invalid name!");
                } else {
                    // Send server a New Deck Request
                    DeckManagerPacket.NewDeck newDeckRequest = new DeckManagerPacket.NewDeck(deckNameField.getText().trim(),
                            newDeckClass.heroId);
                    OHGame.instance.client.sendTCP(newDeckRequest);
                    changeState(States.EDIT_DECK);
                }
            }
        });
        newDeckNameWindow.add(createDeckButton);

        deckManagerUIGroup.addActor(newDeckNameWindow);
    }

    private void changeState(States newState) {
        switch(newState) {
            case VIEW_DECKS:
                // Request server for decks
                DeckManagerPacket.GetDecks decksRequest = new DeckManagerPacket.GetDecks();
                OHGame.instance.client.sendTCP(decksRequest);

                newDeckClassesTable.setVisible(false);
                decksButtonsGroup.setVisible(true);
                newDeckButton.setVisible(true);
                break;
            case NEW_DECK_CLASS:
                // Request server for classes
                DeckManagerPacket.GetClasses classesRequest = new DeckManagerPacket.GetClasses();
                OHGame.instance.client.sendTCP(classesRequest);

                decksButtonsGroup.setVisible(false);
                newDeckButton.setVisible(false);
                newDeckClassesTable.setVisible(true);
                break;
            case NEW_DECK_NAME:
                newDeckClassesTable.setVisible(false);
                decksButtonsGroup.setVisible(false);
                newDeckButton.setVisible(false);
                newDeckNameWindow.setVisible(true);
                break;
            case EDIT_DECK:
                decksButtonsGroup.setVisible(false);
                newDeckButton.setVisible(false);
                newDeckClassesTable.setVisible(false);
                newDeckNameWindow.setVisible(false);
                break;
        }
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.BACK) || Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            dispose();
            OHGame.instance.changeScreen(new MenuScreen());
        }

        Table.drawDebug(OHGame.instance.stage);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        deckManagerUIGroup.setVisible(true);
    }

    @Override
    public void hide() {
        deckManagerUIGroup.setVisible(false);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        deckManagerUIGroup.clear();
    }
}
