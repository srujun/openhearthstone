package com.srujun.openhearthstone;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.esotericsoftware.kryonet.Client;
import com.srujun.openhearthstone.entities.GameObjects;
import com.srujun.openhearthstone.net.NetFileResolver;
import com.srujun.openhearthstone.net.listeners.CredentialsListener;
import com.srujun.openhearthstone.net.listeners.DeckManagerListener;
import com.srujun.openhearthstone.net.packets.CredentialsPacket;
import com.srujun.openhearthstone.net.packets.KryoPackets;

import java.io.IOException;
import java.util.Random;

public class OHGame extends Game {
    public static final int PORT = 6391;
    public static int WIDTH = 600;
    public static int HEIGHT = 960;
    public static OHGame instance;

    public Random random;
    public Client client;

    public SpriteBatch batch;
    public Preferences prefs;
    public Stage stage;
    public Skin skin;

    public boolean loggedIn = false;
    public AssetManager netAssetManager;
    public GameObjects gameObjects;

    public Group loginUIGroup;
    private Label connectionDetailsLabel;
    private Window window;
    private TextField usernameField;

    public OHGame() {
        instance = this;

        this.random = new Random();
        this.netAssetManager = new AssetManager(new NetFileResolver());
        this.gameObjects = new GameObjects();

        this.client = new Client(8192, 3072);
        KryoPackets.registerPacketClasses(client);
        this.client.start();

        // Add Listeners here.
        this.client.addListener(new CredentialsListener());
        this.client.addListener(new DeckManagerListener());
    }

    public static void log(String message) {
        Gdx.app.log("OH", message);
    }

    public void changeScreen(Screen newScreen) {
        if(newScreen == null)
            return;
        if(newScreen.equals(instance.getScreen()))
            return;

        OHGame.log("Setting screen to: " + newScreen.getClass().getSimpleName());
        instance.setScreen(newScreen);
    }

    public String getUsername() {
        return prefs.getString("username");
    }
	
	@Override
	public void create () {
        batch = new SpriteBatch();
        prefs = Gdx.app.getPreferences("open-hearthstone");
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        stage = new Stage(new FitViewport(OHGame.WIDTH, OHGame.HEIGHT), batch);

        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(stage);

        // Connect Details Label
        connectionDetailsLabel = new Label("Could not connect to server.", skin);
        connectionDetailsLabel.setName("ConnectionDetailsLabel");
        connectionDetailsLabel.setPosition(OHGame.WIDTH/2 - connectionDetailsLabel.getWidth()/2, 0f);

        // Login Window children
        usernameField = new TextField("", skin);
        usernameField.setMessageText("");
        usernameField.setName("UsernameField");

        TextButton loginButton = new TextButton("Log In", skin);
        loginButton.setName("LoginButton");
        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = usernameField.getText().trim();
                usernameField.getOnscreenKeyboard().show(false);

                CredentialsPacket cred = new CredentialsPacket(true, username);
                client.sendTCP(cred);
            }
        });

        // Login Window
        window = new Window("", skin);
        window.setName("LoginWindow");
        window.setSize(400f, 64f);
        window.padLeft(15f).padRight(15f);
        window.setPosition(OHGame.WIDTH/2 - window.getWidth()/2, OHGame.HEIGHT/2 - window.getHeight()/2);
        window.add(usernameField);
        window.add(loginButton);

        // Add Login Window to the Group, and the Group to the Stage
        loginUIGroup = new Group();
        loginUIGroup.setName("LoginUIGroup");
        loginUIGroup.addActor(window);
        stage.addActor(loginUIGroup);

        try {
            client.connect(5000, "oh-server.srujun.com", PORT);
        } catch (IOException e) {
            OHGame.log("Could not connect to server.");
            loggedIn = false;
        } finally {
            // Ask for new username if preferences is empty, else send loaded username to the server.
            if(getUsername().isEmpty()) {
                askNewUsername("Enter new username:");
            } else {
                CredentialsPacket cred = new CredentialsPacket(false, OHGame.instance.getUsername());
                client.sendTCP(cred);
            }
        }
	}

	@Override
	public void render () {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Show not connected error!
        if(!client.isConnected()) {
            batch.begin();
            connectionDetailsLabel.draw(batch, 1f);
            batch.end();
        }

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1f / 30f));
        stage.draw();
        super.render();
	}

    @Override
    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height);
        super.resize(width, height);
    }

    /**
     * Reusable method to ask for new username
     * @param message The message to show.
     */
    public void askNewUsername(String message) {
        window.setTitle(message);
        usernameField.setMessageText("username");
    }
}
