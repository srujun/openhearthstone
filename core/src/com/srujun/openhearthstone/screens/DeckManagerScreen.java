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

    private Group viewDecksGroup;
    private TextButton newDeckButton;
    private Table newDeckClassesTable;
    private Window newDeckNameWindow;

    public DeckManagerScreen() {
        this.decksMap = new HashMap<TextButton, DeckManagerPacket.Deck>(9);
        this.classesMap = new HashMap<Table, DeckManagerPacket.Classs>(9);

        this.deckManagerUIGroup = new Group();
        deckManagerUIGroup.setName("DeckManagerUIGroup");
        OHGame.instance.stage.addActor(deckManagerUIGroup);

        // Decks Buttons group
        this.viewDecksGroup = new Group();
        viewDecksGroup.setName("ViewDecksGroup");
        deckManagerUIGroup.addActor(viewDecksGroup);

        // New Deck Button
        this.newDeckButton = new TextButton("New Deck", OHGame.instance.skin);
        newDeckButton.setName("NewDeckButton");
        newDeckButton.setSize(480f, 120f);
        newDeckButton.setPosition(OHGame.WIDTH / 2 - newDeckButton.getWidth() / 2, 15f);
        newDeckButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Request server for classes
                DeckManagerPacket.GetClasses classesRequest = new DeckManagerPacket.GetClasses();
                OHGame.instance.client.sendTCP(classesRequest);

                changeState(States.NEW_DECK_CLASS);
            }
        });
        viewDecksGroup.addActor(newDeckButton);

        // New Deck Classes table
        this.newDeckClassesTable = new Table(OHGame.instance.skin);
        newDeckClassesTable.setName("ClassesTable");
        newDeckClassesTable.setPosition(OHGame.WIDTH / 2, OHGame.HEIGHT / 2);
        newDeckClassesTable.setVisible(false);
        deckManagerUIGroup.addActor(newDeckClassesTable);

        // New Deck name window
        this.newDeckNameWindow = new Window("", OHGame.instance.skin);

        // --------------------------------- End Scene2d.ui Setup ---------------------------------------

        // Request server for decks
        DeckManagerPacket.GetDecks decksRequest = new DeckManagerPacket.GetDecks();
        OHGame.instance.client.sendTCP(decksRequest);

        changeState(States.VIEW_DECKS);
    }

    public void setViewDecksUI(ArrayList<DeckManagerPacket.Deck> decks) {
        // User Decks Table
        Table decksTable = new Table(OHGame.instance.skin);
        decksTable.setSize(350f, 810f);
        decksTable.setPosition(OHGame.WIDTH/2 - decksTable.getWidth()/2, 140f);
        decksTable.top();
        decksTable.defaults().fillX();
        viewDecksGroup.addActor(decksTable);

        // Buttons of user's decks.
        for(final DeckManagerPacket.Deck deck : decks) {
            TextButton deckButton = new TextButton(deck.classs.name + ": " + deck.name, OHGame.instance.skin);
            deckButton.setName("DeckButton:" + deck.name);
            deckButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Send server Edit Deck request with the name of the deck to edit.
                    DeckManagerPacket.EditDeck request = new DeckManagerPacket.EditDeck(deck.name, deck.classs);
                    OHGame.instance.client.sendTCP(request);
                    changeState(States.EDIT_DECK);
                }
            });
            decksTable.add(deckButton).size(350f, 80f).padTop(5f).padBottom(5f);
            decksTable.row();
            decksMap.put(deckButton, deck);
        }

        // Hide new deck button if user already has 9 or more decks.
        if(decks.size() >= 9) {
            newDeckButton.setVisible(false);
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
            Image classImage = new Image(image);
            classImage.setName("ClassImage:" + classs.name);
            classNestedTable.add(classImage).size(160f, 160f).pad(25f, 7.5f, 4f, 7.5f);

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
        newDeckNameWindow.setPosition(OHGame.WIDTH/2 - newDeckNameWindow.getWidth()/2, OHGame.HEIGHT/2 - newDeckNameWindow.getHeight()/2);

        // Create a TextField where the user will enter the name of the deck
        final TextField deckNameField = new TextField("", OHGame.instance.skin);
        deckNameField.setName("NewDeckNameField");
        deckNameField.setMessageText("Custom " + newDeckClass.name);
        newDeckNameWindow.add(deckNameField);

        // Create a TextButton to submit the name of the deck
        TextButton createDeckButton = new TextButton("Create!", OHGame.instance.skin);
        createDeckButton.setName("CreateNewDeckButton");
        createDeckButton.padLeft(10f);
        createDeckButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Hide the onScreenKeyboard.
                deckNameField.getOnscreenKeyboard().show(false);
                // Find out if the entered deck name already exists.
                boolean deckNameExists = false;
                for(DeckManagerPacket.Deck deck : decksMap.values()) {
                    if(deck.name.equals(deckNameField.getText())) {
                        deckNameExists = true;
                        break;
                    }
                }
                // Deal with invalid deck names...
                if (deckNameField.getText().isEmpty()) {
                    newDeckNameWindow.setTitle("New " + newDeckClass.name + " Deck: invalid name!");
                } else if(deckNameExists) {
                    newDeckNameWindow.setTitle("New " + newDeckClass.name + " Deck: name exists!");
                } else {
                    // Send server a New Deck Request
                    DeckManagerPacket.NewDeck newDeckRequest = new DeckManagerPacket.NewDeck(deckNameField.getText().trim(),
                            newDeckClass);
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
                newDeckClassesTable.setVisible(false);
                newDeckNameWindow.setVisible(false);
                viewDecksGroup.setVisible(true);
                break;
            case NEW_DECK_CLASS:
                viewDecksGroup.setVisible(false);
                newDeckNameWindow.setVisible(false);
                newDeckClassesTable.setVisible(true);
                break;
            case NEW_DECK_NAME:
                newDeckClassesTable.setVisible(false);
                viewDecksGroup.setVisible(false);
                newDeckNameWindow.setVisible(true);
                break;
            case EDIT_DECK:
                viewDecksGroup.setVisible(false);
                newDeckClassesTable.setVisible(false);
                newDeckNameWindow.setVisible(false);

                dispose();
                OHGame.instance.changeScreen(new EditDeckScreen());
                break;
        }
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.BACK) || Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            dispose();
            OHGame.instance.changeScreen(new MenuScreen());
        }
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
