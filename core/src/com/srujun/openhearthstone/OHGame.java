package com.srujun.openhearthstone;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.kryonet.Client;
import com.srujun.openhearthstone.assets.NetFileResolver;
import com.srujun.openhearthstone.entities.GameObjects;
import com.srujun.openhearthstone.screens.MenuScreen;
import com.srujun.openhearthstone.servercomm.KryoPackets;
import com.srujun.openhearthstone.servercomm.LoginListener;

import java.util.Random;

public class OHGame extends Game {
    public static final int PORT = 6391;
    public static int WIDTH = 540;
    public static int HEIGHT = 960;
    public static OHGame instance;

    public Random random;
    public SpriteBatch batch;
    public BitmapFont belwe32Font;
    public Preferences prefs;
    public Client client;
    public AssetManager netAssetManager;

    public GameObjects gameObjects;

    private String username;

    public static void log(String message) {
        Gdx.app.log("OH", message);
    }

    public static void changeScreen(Screen newScreen) {
        if(instance.getScreen() == newScreen)
            return;

        OHGame.log("Setting screen to: " + newScreen.getClass().getSimpleName());
        instance.setScreen(newScreen);
    }

    public String getUsername() {
        return username;
    }

    public OHGame() {
        instance = this;
        random = new Random();
        netAssetManager = new AssetManager(new NetFileResolver());
        gameObjects = new GameObjects();

        client = new Client();
        KryoPackets.registerPacketClasses(client);
        client.start();

        // Add Listeners here.
        client.addListener(new LoginListener(prefs));
    }
	
	@Override
	public void create () {
        this.batch = new SpriteBatch();
        this.belwe32Font = new BitmapFont(Gdx.files.internal("fonts/belwe-32-bold.fnt"));
        this.prefs = Gdx.app.getPreferences("open-hearthstone");
        username = prefs.getString("username");

        /*
        try {
            client.connect(5000, "oh-server.srujun.com", PORT);
        } catch (IOException e) {
            // ADD FAILED CONNECTION DIALOG HERE
            e.printStackTrace();
        }
        */

        OHGame.changeScreen(new MenuScreen());
	}

	@Override
	public void render () {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
	}
}
