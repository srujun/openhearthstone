package com.srujun.openhearthstone.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.srujun.openhearthstone.json.HeroTemplate;

public class Hero {
    public enum Classs {
        NEUTRAL, WARRIOR, PALADIN, HUNTER, ROGUE, PRIEST, SHAMAN, MAGE, WARLOCK, DRUID;
        public static Classs parseClasss(int id) {
            switch(id) {
                case 0: return Classs.NEUTRAL;
                case 1: return Classs.WARRIOR;
                case 2: return Classs.PALADIN;
                case 3: return Classs.HUNTER;
                case 4: return Classs.ROGUE;
                case 5: return Classs.PRIEST;
                case 7: return Classs.SHAMAN;
                case 8: return Classs.MAGE;
                case 9: return Classs.WARLOCK;
                case 11: return Classs.DRUID;
            }
            throw new IllegalArgumentException("Could not parse card classs.");
        }
    }

    private int id;
    private String name;
    private Classs classs;
    private int health;

    private Texture image;
    private Rectangle box;

    public Hero(HeroTemplate template) {
        this.id = template.id;
        this.name = template.name;
        this.classs = Classs.parseClasss(template.classs);
        this.health = template.health;
        this.image = new Texture(Gdx.files.internal("textures/heroes_full/" + template.image + ".png"));
        this.image.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    // ---- SETTERS ----
    public void setBox(float x, float y, float dimensions) {
        box = new Rectangle(x, y, dimensions, dimensions);
    }
    // -----------------

    // ---- GETTERS ----
    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Classs classs() {
        return classs;
    }

    public int health() {
        return health;
    }

    public Texture image() {
        return image;
    }

    public Rectangle box() {
        return box;
    }
    // -----------------
}
