package com.starshinegames.craftingblocks;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	// private File tables = new File(getDataFolder() + "tables.yml");

	// public FileConfiguration tablesConfig =
	// YamlConfiguration.loadConfiguration(tables);

	private Tables tables;

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new MyListener(this), (Plugin) this);
		getServer().getPluginManager().registerEvents(new Tables(this, (Plugin) this), (Plugin) this);
		if (!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}
		this.tables = new Tables(this, (Plugin) this);
	}

	@Override
	public void onDisable() {
		tables.save();
	}

	/*
	 * public FileConfiguration getTablesConfig() { return tablesConfig; } public
	 * File getTables() { return tables; }
	 */
}
