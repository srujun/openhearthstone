package com.srujun.openhearthstone.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;
import com.srujun.openhearthstone.OHGame;
import com.srujun.openhearthstone.entities.Card;
import com.srujun.openhearthstone.json.CardTemplate;
import com.srujun.openhearthstone.json.JsonUtility;

public class LoadingScreen implements Screen {
    static class CardsArray {
        public Array<CardTemplate> cards;
    }

    public LoadingScreen() {

    }

    @Override
    public void render(float delta) {
        if(OHGame.instance.netAssetManager.update()) {
            OHGame.changeScreen(new GameScreen());
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        // Parse JSON cards file
        CardsArray jsonCards = JsonUtility.read(Gdx.files.internal("json/hearthhead_cards.json"), CardsArray.class);

        // Populate player's deck - HEAVY!!!
        int[] miracleRogue = {180, 180, 1158, 1158, 365, 365, 268, 268, 990, 990, 459, 459, 1064, 904, 904, 461, 461,
                573, 573, 306, 667, 667, 1117, 1117, 421, 749, 1659, 559, 932, 932};
        for(int id : miracleRogue) {
            for(CardTemplate card : jsonCards.cards) {
                if(card.id == id) {
                    switch(Card.Type.parseType(card.type)) {
                        case MINION:
                            OHGame.instance.gameObjects.playerDeck.add(new Card.MinionCard(card)); break;
                        case SPELL:
                            OHGame.instance.gameObjects.playerDeck.add(new Card.SpellCard(card)); break;
                        case WEAPON:
                            OHGame.instance.gameObjects.playerDeck.add(new Card.WeaponCard(card)); break;
                    }
                    break; // Found card! Changes iteration count from 14000 to 4000! :D
                }
            }
        }
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
