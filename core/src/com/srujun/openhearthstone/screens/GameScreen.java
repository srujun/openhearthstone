package com.srujun.openhearthstone.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.srujun.openhearthstone.OHGame;
import com.srujun.openhearthstone.entities.Card;
import com.srujun.openhearthstone.entities.Hand;
import com.srujun.openhearthstone.entities.Hero;
import com.srujun.openhearthstone.json.CardTemplate;
import com.srujun.openhearthstone.json.JsonUtility;
import com.srujun.openhearthstone.utils.MusicSequencer;

public class GameScreen implements Screen {
    private enum GameStates {
        ANNOUNCE, PLAYER_TURN, ENEMY_TURN
    }

    // Start with ANNOUNCE state
    private GameStates currentState = GameStates.ANNOUNCE;

    private MusicSequencer announceSq;
    private OrthographicCamera camera;
    private Viewport viewport;

    private Hero player;
    private Array<Card> playerDeck = new Array<Card>();
    private Hand playerHand;

    private Hero enemy;
    private Array<Card> enemyDeck  = new Array<Card>();
    private Hand enemyHand;

    private final boolean playerFirstTurn;

    public GameScreen() {
        this.player = OHGame.instance.gameObjects.player;
        this.playerHand = new Hand();
        this.enemy = OHGame.instance.gameObjects.enemy;
        this.enemyHand = new Hand();
        this.announceSq = new MusicSequencer();

        // Create announcer sequence
        announceSq.addMusic(Gdx.files.internal("sounds/announcer/" + announcerRandomMusicFile(player.id(), player.name())));
        announceSq.addMusic(Gdx.files.internal("sounds/announcer/announcer_versus_" + OHGame.instance.random.nextInt(2) + ".ogg"));
        announceSq.addMusic(Gdx.files.internal("sounds/announcer/" + announcerRandomMusicFile(enemy.id(), enemy.name())));

        // Determine who plays first
        playerFirstTurn = OHGame.instance.random.nextBoolean();

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, OHGame.WIDTH, OHGame.HEIGHT);
        this.viewport = new ExtendViewport(OHGame.WIDTH, OHGame.HEIGHT, camera);
    }

    /**
     * Generate the filename for the announcer sound for a hero's name
     * @param heroId The ID of the hero
     * @param heroName The name of the hero
     * @return The filename of the sound that can be loaded
     */
    private String announcerRandomMusicFile(int heroId, String heroName) {
        // Random extension, special case for Gul'dan
        String rand = "_" + (heroName.equals("Gul'dan") ? OHGame.instance.random.nextInt(2) : OHGame.instance.random.nextInt(3));

        if(heroName.contains(" ")) {
            heroName = heroName.substring(0, heroName.indexOf(" "));
        }
        return "announcer_" + heroId + "_" + heroName.replace("'", "").toLowerCase() + rand + ".ogg";
    }

    @Override
    public void render(float delta) {
        camera.update();
        OHGame.instance.batch.setProjectionMatrix(camera.combined);
        OHGame.instance.batch.begin();

        // Draw heroes
        OHGame.instance.batch.draw(player.image(), player.box().getX(), player.box().getY(), player.box().getWidth(), player.box().getHeight());
        OHGame.instance.batch.draw(enemy.image(), enemy.box().getX(), enemy.box().getY(), enemy.box().getWidth(), enemy.box().getHeight());

        // Draw player hand
        Card selectedCard = null;
        for(Card c : playerHand) {
            if(c.box().contains(Gdx.input.getX(), OHGame.HEIGHT - Gdx.input.getY())) { // If mouse hover over card
                selectedCard = c;
                continue;
            }
            OHGame.instance.batch.draw(c.cardTexRegion(), c.box().getX(), c.box().getY(), c.box().getWidth(), c.box().getHeight());
        }
        if(selectedCard != null) {
            OHGame.instance.batch.draw(selectedCard.cardTexRegion(), selectedCard.box().getX(), 0f,
                    selectedCard.box().getWidth(), selectedCard.box().getHeight());
        }

        // Game control state machine
        switch(currentState) {
            case ANNOUNCE:
                if(!announceSq.hasStarted())
                    announceSq.play();

                if(announceSq.isComplete()) {
                    currentState = playerFirstTurn ? GameStates.PLAYER_TURN : GameStates.ENEMY_TURN;
                    announceSq.dispose();
                    OHGame.log(playerFirstTurn ? "PLAYER plays first!" : "ENEMY plays first!");
                }
                break;

            case PLAYER_TURN:

                break;

            case ENEMY_TURN:
                break;
        }

        OHGame.instance.batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        // Draw player's hand of 3 cards (4 cards if enemy plays first)
        for(int i = 0; i < (playerFirstTurn ? 3 : 4); i++) {
            int rand = OHGame.instance.random.nextInt(playerDeck.size);
            Card drawCard = playerDeck.removeIndex(rand);
            playerHand.add(drawCard);
            playerDeck.shrink();
        }

        /*
        // Add The Coin (1746) to the hand of whoever is not going first
        for(CardTemplate card : jsonCards.cards) {
            if(card.id == 1746) {
                if(playerFirstTurn)
                    enemyHand.add(new Card.SpellCard(card));
                else
                    playerHand.add(new Card.SpellCard(card));
                break;
            }
        }
        */

        // DIM: 120f, x: Center, y: 250 away from bottom border
        player.setBox(OHGame.WIDTH/2 - 120f/2, 250f, 120f);
        // DIM: 120f, x: Center, y: 100 away from top border
        enemy.setBox(OHGame.WIDTH/2 - 120f/2, OHGame.HEIGHT - (120f + 100f), 120f);

        // Define card rectangles
        float spaces = (OHGame.WIDTH / playerHand.size());
        for(int i = 0; i < playerHand.size(); i++) {
            float drawWidth = 240f;
            float drawHeight = Card.TEXTURE_ASPECT_RATIO * drawWidth;
            float drawX = (spaces / 2) + (i * spaces / 2);
            float drawY = -100f;
            playerHand.getCard(i).setBox(drawX, drawY, drawWidth, drawHeight);
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
        for(Card card : playerDeck) {
            card.image().dispose();
        }
    }
}
