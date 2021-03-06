package com.srujun.openhearthstone.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.srujun.openhearthstone.OHGame;

public class DesktopLauncher {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "OpenHearthstone";
        config.height = 960;
        config.width = 600;
		new LwjglApplication(new OHGame(), config);
	}
}
