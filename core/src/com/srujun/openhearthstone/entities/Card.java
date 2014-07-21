package com.srujun.openhearthstone.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.srujun.openhearthstone.json.CardTemplate;

public abstract class Card {
    // ----- ENUMS -----
    public enum Quality {
        BASIC, COMMON, UNCORRECTABLE, RARE, EPIC, LEGENDARY;
        public static Quality parseQuality(int id) {
            switch(id) {
                case 0: return Quality.BASIC;
                case 1: return Quality.COMMON;
                case 2: return Quality.UNCORRECTABLE;
                case 3: return Quality.RARE;
                case 4: return Quality.EPIC;
                case 5: return Quality.LEGENDARY;
            }
            throw new IllegalArgumentException("Could not parse card quality: " + id + ".");
        }
    }

    public enum Type {
        MINION, SPELL, WEAPON;
        public static Type parseType(int id) {
            switch(id) {
                case 4: return Type.MINION;
                case 5: return Type.SPELL;
                case 7: return Type.WEAPON;
            }
            throw new IllegalArgumentException("Could not parse card type: " + id + ".");
        }
    }

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
            throw new IllegalArgumentException("Could not parse card classs: " + id + ".");
        }
    }

    public enum Race {
        NEUTRAL, MURLOC, DEMON, BEAST, TOTEM, PIRATE, DRAGON;
        public static Race parseRace(int id) {
            switch(id) {
                case 0: return Race.NEUTRAL;
                case 14: return Race.MURLOC;
                case 15: return Race.DEMON;
                case 20: return Race.BEAST;
                case 21: return Race.TOTEM;
                case 23: return Race.PIRATE;
                case 24: return Race.DRAGON;
            }
            throw new IllegalArgumentException("Could not parse card race: " + id + ".");
        }
    }
    // -----------------

    // - COMMON PROPS --
    public static final float TEXTURE_ASPECT_RATIO = 410f / 307f;

    protected int id;
    protected String name;
    protected String description;
    protected Quality quality;
    protected Texture image;
    protected TextureRegion cardTextureRegion;
    protected Rectangle box;
    // -----------------


    // ---- GETTERS ----
    public abstract Type type();

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public Quality quality() {
        return quality;
    }

    public Texture image() {
        return image;
    }

    public TextureRegion cardTexRegion() {
        return cardTextureRegion;
    }

    public Rectangle box() {
        return box;
    }
    // -----------------


    // ---- SETTERS ----
    public void setBox(float x, float y, float width, float height) {
        this.box = new Rectangle(x, y, width, height);
    }
    // -----------------


    // - CONSTRUCTORS --
    public Card(CardTemplate template) {
        this.id = template.id;
        this.name = template.name;
        this.description = template.description;
        this.quality = Quality.parseQuality(template.quality);
    }
    // -----------------

    public static class MinionCard extends Card {
        private Classs classs;
        private Race race;
        private int cost;
        private int attack;
        private int health;

        public MinionCard(CardTemplate template) {
            super(template);

            this.image = new Texture(Gdx.files.internal("textures/minions/" + id + "_" +
                    name.replace(" ", "").replace(":","") + ".png"));
            image.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            this.cardTextureRegion = new TextureRegion(image, 0, 33, 307, 410);

            this.classs = Classs.parseClasss(template.classs);
            this.race = Race.parseRace(template.race);
            this.cost = template.cost;
            this.attack = template.attack;
            this.health = template.health;
        }

        public Classs classs() {
            return classs;
        }

        public Race race() {
            return race;
        }

        public int cost() {
            return cost;
        }

        public int attack() {
            return attack;
        }

        public int health() {
            return health;
        }

        // --- OVERRIDES ---
        @Override
        public Type type() {
            return Type.MINION;
        }
        // -----------------
    }

    public static class SpellCard extends Card {
        private Classs classs;
        private int cost;

        public SpellCard(CardTemplate template) {
            super(template);

            this.image = new Texture(Gdx.files.internal("textures/spells/" + id + "_" +
                    name.replace(" ", "").replace(":","") + ".png"));
            image.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            this.cardTextureRegion = new TextureRegion(image, 0, 33, 307, 410);

            this.classs = Classs.parseClasss(template.classs);
            this.cost = template.cost;
        }

        public Classs classs() {
            return classs;
        }

        public int cost() {
            return cost;
        }

        // --- OVERRIDES ---
        @Override
        public Type type() {
            return Type.SPELL;
        }
        // -----------------
    }

    public static class WeaponCard extends Card {
        private int durability;

        public WeaponCard(CardTemplate template) {
            super(template);

            this.image = new Texture(Gdx.files.internal("textures/weapons/" + id + "_" +
                    name.replace(" ", "").replace(":","") + ".png"));
            image.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            this.cardTextureRegion = new TextureRegion(image, 0, 33, 307, 410);

            this.durability = template.durability;
        }

        public int durability() {
            return durability;
        }

        // --- OVERRIDES ---
        @Override
        public Type type() {
            return Type.WEAPON;
        }
        // -----------------
    }
}
