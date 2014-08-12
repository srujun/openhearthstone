package com.srujun.openhearthstone.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.srujun.openhearthstone.OHGame;

public class MenuScreen implements Screen {
    private Group menuScreenUIGroup;

    public MenuScreen() {
        this.menuScreenUIGroup = new Group();
        menuScreenUIGroup.setName("MenuScreenUIGroup");

        TextButton playButton = new TextButton("Play", OHGame.instance.skin);
        playButton.setName("PlayButton");
        playButton.setSize(400f, 160f);
        playButton.setPosition(OHGame.WIDTH/2 - playButton.getWidth()/2, OHGame.HEIGHT/2 + playButton.getHeight());
        menuScreenUIGroup.addActor(playButton);

        TextButton decksButton = new TextButton("Decks", OHGame.instance.skin);
        playButton.setName("DecksButton");
        decksButton.setSize(400f, 160f);
        decksButton.setPosition(OHGame.WIDTH/2 - decksButton.getWidth()/2, OHGame.HEIGHT/2 - decksButton.getHeight());
        decksButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                OHGame.instance.changeScreen(new DeckManagerScreen());
                dispose();
            }
        });
        menuScreenUIGroup.addActor(decksButton);

        OHGame.instance.stage.addActor(menuScreenUIGroup);
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        menuScreenUIGroup.setVisible(true);
    }

    @Override
    public void hide() {
        menuScreenUIGroup.setVisible(false);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        menuScreenUIGroup.clear();
    }
}
