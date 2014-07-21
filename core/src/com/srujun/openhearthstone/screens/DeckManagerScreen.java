package com.srujun.openhearthstone.screens;

import com.badlogic.gdx.Screen;
import com.srujun.openhearthstone.OHGame;
import com.srujun.openhearthstone.servercomm.KryoPackets;

public class DeckManagerScreen implements Screen {

    public DeckManagerScreen() {
        // Request server for decks
        KryoPackets.DeckManager.GetDecks decksRequest = new KryoPackets.DeckManager.GetDecks();
        decksRequest.username = OHGame.instance.getUsername();
        OHGame.instance.client.sendTCP(decksRequest);
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
