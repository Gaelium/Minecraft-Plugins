package com.starshinegames.mmoexpboost;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		// Initialize the listener

		getServer().getPluginManager().registerEvents(new MyListener(), (Plugin) this);

	}

	@Override
	public void onDisable() {

	}
}
