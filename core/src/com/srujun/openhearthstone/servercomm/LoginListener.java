package com.srujun.openhearthstone.servercomm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.srujun.openhearthstone.OHGame;
import com.srujun.openhearthstone.screens.MenuScreen;

public class LoginListener extends Listener {
    private Preferences prefs;

    public LoginListener(Preferences prefs) {
        this.prefs = prefs;
    }

    // Reusable method to ask for new username
    private void askNewUsername(final Connection connection, String message) {
        // Ask user to enter another username
        // Username has not been set, so generate a random one.
        final String randomUsername = "user" + (OHGame.instance.random.nextInt((99999-10000)+1) + 10000);
        Gdx.input.getTextInput(new Input.TextInputListener() {
            @Override
            public void input(String text) {
                // Save this username to preferences.
                prefs.putString("username", text);
                prefs.flush();
                // Send this username to the server!
                KryoPackets.Credentials cred = new KryoPackets.Credentials();
                cred.username = text;
                cred.isNewUsername = true;
                connection.sendTCP(cred);
            }

            @Override
            public void canceled() {
                // User wants to keep random username!
                // Save this username to preferences.
                prefs.putString("username", randomUsername);
                prefs.flush();
                // Send this username to the server!
                KryoPackets.Credentials cred = new KryoPackets.Credentials();
                cred.username = randomUsername;
                cred.isNewUsername = true;
                connection.sendTCP(cred);
            }
        }, message, randomUsername);
    }

    @Override
    public void connected(final Connection connection) {
        super.connected(connection);
        // Load username from preferences.
        String username = prefs.getString("username", "");

        // Ask for new username if preferences is empty.
        if(username.equals("")) {
            askNewUsername(connection, "Enter new username:");
        } else {
            // Send the loaded username to the server.
            KryoPackets.Credentials cred = new KryoPackets.Credentials();
            cred.username = username;
            cred.isNewUsername = false;
            connection.sendTCP(cred);
        }
    }

    @Override
    public void received(final Connection connection, Object object) {
        super.received(connection, object);
        // Deal with invalid new usernames.
        if(object instanceof KryoPackets.Credentials.InvalidUsername) {
            askNewUsername(connection, "Invalid! Enter new username:");
            return;
        }

        // If new user supplied an existing username.
        if(object instanceof KryoPackets.Credentials.UserExists) {
            askNewUsername(connection, "Already exists! Enter new username:");
            return;
        }

        // If the loaded username was not found.
        if(object instanceof KryoPackets.Credentials.UsernameNotFound) {
            askNewUsername(connection, "Can't find you on server! Enter new username:");
            return;
        }

        // User has successfully logged in.
        if(object instanceof KryoPackets.Credentials.LoggedIn) {
            System.out.println("User \"" + prefs.getString("username") + "\" has logged in!");
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    OHGame.changeScreen(new MenuScreen());
                }
            });
            return;
        }
    }
}
