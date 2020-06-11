package com.starshinegames.craftingblocks;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class AbstractClass {
	private Main main;
	private File file;
	protected FileConfiguration config;

	// The whole point of this class is to create tables.yml (the config) It takes 2
	// restarts for the config to be filled
	public AbstractClass(Main main, String filename) {
		this.main = main;
		this.file = new File(main.getDataFolder(), filename);
		if (!file.exists()) {
			try {
				file.createNewFile();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.config = YamlConfiguration.loadConfiguration(file);
	}

	public void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
