package com.srujun.openhearthstone.servercomm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.srujun.openhearthstone.OHGame;
import com.srujun.openhearthstone.screens.MenuScreen;

public class DeckManagerListener extends Listener {

    public DeckManagerListener() {
    }

    @Override
    public void received(final Connection connection, Object object) {
        super.received(connection, object);

    }
}
