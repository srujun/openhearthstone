package com.srujun.openhearthstone.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.srujun.openhearthstone.OHGame;

public class MenuScreen implements Screen {
    private Stage stage;
    private Skin skin;
    private TextButton playButton;
    private TextButton decksButton;

    public MenuScreen() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        playButton = new TextButton("Play", skin);
        playButton.setSize(OHGame.WIDTH * 2/5, OHGame.HEIGHT / 15);
        playButton.setPosition(OHGame.WIDTH/2 - playButton.getWidth()/2, OHGame.HEIGHT/2 + playButton.getHeight());

        decksButton = new TextButton("Decks", skin);
        decksButton.setSize(OHGame.WIDTH * 2/5, OHGame.HEIGHT / 15);
        decksButton.setPosition(OHGame.WIDTH/2 - decksButton.getWidth()/2, OHGame.HEIGHT/2 - playButton.getHeight());
        decksButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                OHGame.changeScreen(new DeckManagerScreen());
            }
        });
    }

    @Override
    public void render(float delta) {
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        stage.addActor(playButton);
        stage.addActor(decksButton);
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
        stage.dispose();
    }
}
