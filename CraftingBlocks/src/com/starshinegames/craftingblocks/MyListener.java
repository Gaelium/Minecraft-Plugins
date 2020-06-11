package com.starshinegames.craftingblocks;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;

public class MyListener implements Listener {
	Main main;

	public MyListener(Main main) {
		this.main = main;
	}

	// Make sure land that has a coral block on it does not go fallow. This is used
	// for custom crops
	@EventHandler
	public void Fallow(BlockFadeEvent b) {
		Location l = b.getBlock().getLocation();
		if (!l.getWorld().getBlockAt(b.getBlock().getX(), b.getBlock().getY() + 1, b.getBlock().getZ()).isEmpty()) {
			if (l.getWorld().getBlockAt(b.getBlock().getX(), b.getBlock().getY() + 1, b.getBlock().getZ()).getType()
					.toString().contains("CORAL")
					|| l.getWorld().getBlockAt(b.getBlock().getX(), b.getBlock().getY() + 1, b.getBlock().getZ())
							.getType().toString().contains("coral")) {
				b.setCancelled(true);
			}
		}
	}
}