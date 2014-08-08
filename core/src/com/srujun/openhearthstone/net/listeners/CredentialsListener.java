package com.srujun.openhearthstone.net.listeners;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.srujun.openhearthstone.OHGame;
import com.srujun.openhearthstone.net.packets.CredentialsPacket;
import com.srujun.openhearthstone.screens.MenuScreen;

public class CredentialsListener extends Listener {
    @Override
    public void connected(Connection connection) {
    }

    @Override
    public void received(Connection connection, Object object) {
        // If user gave an invalid new username.
        if(object instanceof CredentialsPacket.LoginResponse) {
            CredentialsPacket.LoginResponse response = (CredentialsPacket.LoginResponse) object;
            handleLoginResponse(response);
        }
    }

    @Override
    public void disconnected(Connection connection) {
        OHGame.instance.loggedIn = false;
    }

    private void handleLoginResponse(final CredentialsPacket.LoginResponse response) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                switch(response.message) {
                    case INVALID:
                        OHGame.instance.askNewUsername("Invalid! Enter new username:");
                        break;
                    case ALREADY_EXISTS:
                        OHGame.instance.askNewUsername("Already exists! Enter new username:");
                        break;
                    case NOT_FOUND:
                        OHGame.instance.askNewUsername("Not found! Enter new username:");
                        break;
                    case SUCCESSFUL:
                        OHGame.instance.loggedIn = true;
                        OHGame.instance.loginUIGroup.setVisible(false);
                        OHGame.log("User \"" + OHGame.instance.getUsername() + "\" has logged in!");
                        OHGame.instance.changeScreen(new MenuScreen());
                        break;
                }
            }
        });
    }
}
