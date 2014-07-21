package com.srujun.openhearthstone.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.srujun.openhearthstone.OHGame;
import com.srujun.openhearthstone.entities.GameObjects;
import com.srujun.openhearthstone.entities.Hero;
import com.srujun.openhearthstone.json.HeroTemplate;
import com.srujun.openhearthstone.json.JsonUtility;

public class HeroSelectionScreen implements Screen {
    static class HeroesArray {
        public Array<HeroTemplate> heroes;
    }

    private enum SelectionStates {
        HERO_SELECT, DECK_SELECT
    }

    private SelectionStates currentState = SelectionStates.HERO_SELECT;

    private OHGame ohGame;

    private OrthographicCamera camera;
    private Viewport viewport;

    private Array<HeroTemplate> heroes;
    private Array<Texture> heroesImages;
    private Array<Rectangle> heroBoxes;

    private String text = "Choose Your Hero";
    private float textWidth;

    public HeroSelectionScreen(OHGame ohGame) {
        this.ohGame = ohGame;

        this.heroes = new Array<HeroTemplate>();
        this.heroesImages = new Array<Texture>();
        this.heroBoxes = new Array<Rectangle>();

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, OHGame.WIDTH, OHGame.HEIGHT);
        this.viewport = new ExtendViewport(OHGame.WIDTH, OHGame.HEIGHT, camera);
        textWidth = ohGame.belwe32Font.getBounds(text).width;
    }

    @Override
    public void render(float delta) {
        camera.update();
        ohGame.batch.setProjectionMatrix(camera.combined);
        ohGame.batch.begin();

        ohGame.belwe32Font.draw(ohGame.batch, text, OHGame.WIDTH / 2 - textWidth / 2, OHGame.HEIGHT - 70);

        if(currentState == SelectionStates.HERO_SELECT) {
            // Render hero boxes
            for (int i = 0; i < heroes.size; i++) {
                Texture hero = heroesImages.get(i);
                Rectangle box = heroBoxes.get(i);
                ohGame.batch.draw(hero, box.getX(), box.getY(), box.getWidth(), box.getHeight());
            }

            if (Gdx.input.justTouched()) {
                Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
                camera.unproject(touchPos);
                for (int i = 0; i < heroBoxes.size; i++) {
                    if (heroBoxes.get(i).contains(touchPos.x, touchPos.y)) {
                        ohGame.gameObjects.player = new Hero(heroes.get(i));
                        // Random enemy
                        ohGame.gameObjects.enemy = new Hero(heroes.get(ohGame.random.nextInt(9)));
                        OHGame.changeScreen(new GameScreen());
                        break;
                    }
                }
            }
        }

        if(currentState == SelectionStates.DECK_SELECT) {
            if (Gdx.input.justTouched()) {
                Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
                camera.unproject(touchPos);
            }
        }

        ohGame.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        HeroesArray jsonHeroes = JsonUtility.read(Gdx.files.internal("json/hearthhead_heroes.json"), HeroesArray.class);
        for(HeroTemplate hero : jsonHeroes.heroes) {
            if(hero.collectible == 1) {
                heroes.add(hero);
                heroesImages.add(new Texture(Gdx.files.internal("textures/heroes_full/" + hero.image + ".png")));
            }
        }

        // Set filter for textures
        for(Texture heroImage : heroesImages) {
            heroImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        for(int i = 0;i < 3;i++) {
            for (int j = 0; j < 3; j++) {
                int dim = 160;
                int x = 15 + (dim + 15)*j;
                int y = OHGame.HEIGHT - (350 + (dim + 60)*i);
                heroBoxes.add(new Rectangle(x, y, dim, dim));
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
        for(Texture heroesImage : heroesImages) {
            heroesImage.dispose();
        }
    }
}
