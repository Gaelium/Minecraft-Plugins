package com.starshinegames.craftingblocks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.role.enums.RoleSetting;

public class Tables extends AbstractClass implements Listener {
	private final LandsIntegration landsAddon;

	public Tables(Main main, Plugin p) {
		// Initialize the config yml
		super(main, "tables.yml");
		landsAddon = new LandsIntegration(p, false);
		// Run a Try/Catch to see if the yml is empty, as to not override what the user
		// has put in it.
		try {
			if (config.getString("Tables.Number").isEmpty()) {
				config.set("Tables.Number", 1);

			}
		} catch (NullPointerException n) {
			// Fill the yml if it was empty.
			config.set("Tables.Number", 1);
			config.set("Tables.One.BlockState",
					"minecraft:brown_mushroom_block[down=true,east=false,north=false,south=true,up=false,west=true]");
			config.set("Tables.One.Command", "mmoitems stations open blacksmith-table");
			config.set("Tables.One.CommandTwo", "mmoitems stations open blacksmith-table");
			config.set("Tables.One.Perm", "tables.Farmer");
			config.set("Coral.Number", 1);
			config.set("Coral.1.Pick", true);
			config.set("Coral.1.BlockState", "minecraft:dead_brain_coral_wall_fan[facing=north,waterlogged=false]");
			config.set("Coral.1.Command",
					"mmoitems drop <type> <item-id> %world% %x% %y% %z% <drop chance> <[min]-[max]> 0");
			config.set("Coral.1.CommandTwo", "none");
			config.set("Coral.1.CommandThree", "none");
			config.set("Coral.1.Perm", "tables.Farmer");
		}
	}

	// The Listener for the custom crafting tables.
	@EventHandler
	public void CustomTable(PlayerInteractEvent p) {
		// Makes sure the action was a right click, so players can still break the
		// custom crafting tables
		if (p.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (p.hasBlock()) {
				// No null pointer exception
				if (!p.getClickedBlock().isEmpty()) {
					// The tables use mushroom block states
					if (p.getClickedBlock().getType().equals(Material.BROWN_MUSHROOM_BLOCK)
							|| p.getClickedBlock().getType().equals(Material.MUSHROOM_STEM)
							|| p.getClickedBlock().getType().equals(Material.RED_MUSHROOM_BLOCK)) {

						Block b = p.getClickedBlock();
						// How many tables the owner has configured.
						int i = config.getInt("Tables.Number");
						String t = config.getString("Tables");
						// This is how the table names are formatted. It allows for 9 custom tables
						// (CCEarth only has 4)
						for (int j = 1; j <= i; j++) {
							String s;
							if (j == 1)
								s = "One";
							else if (j == 2)
								s = "Two";
							else if (j == 3)
								s = "Three";
							else if (j == 4)
								s = "Four";
							else if (j == 5)
								s = "Five";
							else if (j == 6)
								s = "Six";
							else if (j == 7)
								s = "Seven";
							else if (j == 8)
								s = "Eight";
							else if (j == 9)
								s = "Nine";
							else
								break;
							// Run through each table in the config and check if the block state matches
							String stwo = config.getString("Tables." + s + ".BlockState");
							if (stwo.equals(b.getState().getBlockData().getAsString())) {
								// Check if the player is the class corresponding to the table.
								if (p.getPlayer().hasPermission(config.getString("Tables." + s + ".Perm"))
										|| config.getString("Tables." + s + ".Perm").equals("none")) {
									String sthree = config.getString("Tables." + s + ".Command");
									// Replace the placeholders
									String sfour = sthree.replace("%player%", p.getPlayer().getName() + "")
											.replace("'", "").replace("%x%", b.getLocation().getBlockX() + "")
											.replace("%y%", b.getLocation().getBlockY() + "")
											.replace("%z%", b.getLocation().getBlockZ() + "")
											.replace("%world%", b.getLocation().getWorld().getName() + "");
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), sfour);
									// 2nd command for optional quests
									if (!config.getString("Tables." + s + ".CommandTwo").equals("none")) {
										String sfive = config.getString("Tables." + s + ".CommandTwo");
										String ssix = sfive.replace("%player%", p.getPlayer().getName() + "")
												.replace("'", "").replace("%x%", b.getLocation().getBlockX() + "")
												.replace("%y%", b.getLocation().getBlockY() + "")
												.replace("%z%", b.getLocation().getBlockZ() + "")
												.replace("%world%", b.getLocation().getWorld().getName() + "");
										; // Ah yes, the random semicolon I left in here that I am now too scared to
											// remove in case it were to break everything
										Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ssix);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void CoralDrops(BlockBreakEvent b) {
		Block block = b.getBlock();
		int i = config.getInt("Coral.Number");
		String t = config.getString("Tables");

		for (int j = 1; j <= i; j++) {
			String s = j + "";

			String stwo = config.getString("Coral." + s + ".BlockState");
			if (stwo.equals(block.getState().getBlockData().getAsString())) {

				if (landsAddon.getLand(block.getLocation()) != null) {
					if (landsAddon.getLand(block.getLocation()).isTrusted(b.getPlayer().getUniqueId())
							&& landsAddon.getLand(block.getLocation()).getRole(b.getPlayer().getUniqueId())
									.hasSetting(RoleSetting.BLOCK_BREAK)) {
						if ((config.getBoolean("Coral." + s + ".Pick") && (b.getPlayer().getInventory()
								.getItemInMainHand().getType().equals(Material.DIAMOND_PICKAXE)
								|| b.getPlayer().getInventory().getItemInMainHand().getType()
										.equals(Material.IRON_PICKAXE)))
								|| !config.getBoolean("Coral." + s + ".Pick")) {
							String sthree = config.getString("Coral." + s + ".Command");
							String sfour = sthree.replace("%player%", b.getPlayer().getName() + "").replace("'", "")
									.replace("%x%", block.getLocation().getBlockX() + "")
									.replace("%y%", block.getLocation().getBlockY() + "")
									.replace("%z%", block.getLocation().getBlockZ() + "")
									.replace("%world%", block.getLocation().getWorld().getName() + "");
							b.setDropItems(false);
							if (!config.getString("Coral." + s + ".Command").equals("none")) {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), sfour);
							}
							// The second command only runs if the player has the correct permission.
							if (b.getPlayer().hasPermission(config.getString("Coral." + s + ".Perm"))
									|| config.getString("Coral." + s + ".Perm").equals("none")) {
								if (!config.getString("Coral." + s + ".CommandTwo").equals("none")) {
									String sfive = config.getString("Coral." + s + ".CommandTwo");
									String ssix = sfive.replace("%player%", b.getPlayer().getName() + "")
											.replace("'", "").replace("%x%", block.getLocation().getBlockX() + "")
											.replace("%y%", block.getLocation().getBlockY() + "")
											.replace("%z%", block.getLocation().getBlockZ() + "")
											.replace("%world%", block.getLocation().getWorld().getName() + "");
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ssix);
								}
							}
							if (!config.getString("Coral." + s + ".CommandThree").equals("none")) {
								String sseven = config.getString("Coral." + s + ".CommandThree");
								String seight = sseven.replace("%player%", b.getPlayer().getName() + "")
										.replace("'", "").replace("%x%", block.getLocation().getBlockX() + "")
										.replace("%y%", block.getLocation().getBlockY() + "")
										.replace("%z%", block.getLocation().getBlockZ() + "")
										.replace("%world%", block.getLocation().getWorld().getName() + "");
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), seight);
							}
						} else {
							b.setCancelled(true);
						}
					}
				}
				if (!landsAddon.isClaimed(block.getLocation())) {
					if ((config.getBoolean("Coral." + s + ".Pick") && (b.getPlayer().getInventory().getItemInMainHand()
							.getType().equals(Material.DIAMOND_PICKAXE)
							|| b.getPlayer().getInventory().getItemInMainHand().getType()
									.equals(Material.IRON_PICKAXE)))
							|| !config.getBoolean("Coral." + s + ".Pick")) {
						String sthree = config.getString("Coral." + s + ".Command");
						String sfour = sthree.replace("%player%", b.getPlayer().getName() + "").replace("'", "")
								.replace("%x%", block.getLocation().getBlockX() + "")
								.replace("%y%", block.getLocation().getBlockY() + "")
								.replace("%z%", block.getLocation().getBlockZ() + "")
								.replace("%world%", block.getLocation().getWorld().getName() + "");
						b.setDropItems(false);
						if (!config.getString("Coral." + s + ".Command").equals("none")) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), sfour);
						}
						// The second command only runs if the player has the correct permission.
						if (b.getPlayer().hasPermission(config.getString("Coral." + s + ".Perm"))
								|| config.getString("Coral." + s + ".Perm").equals("none")) {
							if (!config.getString("Coral." + s + ".CommandTwo").equals("none")) {
								String sfive = config.getString("Coral." + s + ".CommandTwo");
								String ssix = sfive.replace("%player%", b.getPlayer().getName() + "").replace("'", "")
										.replace("%x%", block.getLocation().getBlockX() + "")
										.replace("%y%", block.getLocation().getBlockY() + "")
										.replace("%z%", block.getLocation().getBlockZ() + "")
										.replace("%world%", block.getLocation().getWorld().getName() + "");
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ssix);
							}
						}
						if (!config.getString("Coral." + s + ".CommandThree").equals("none")) {
							String sseven = config.getString("Coral." + s + ".CommandThree");
							String seight = sseven.replace("%player%", b.getPlayer().getName() + "").replace("'", "")
									.replace("%x%", block.getLocation().getBlockX() + "")
									.replace("%y%", block.getLocation().getBlockY() + "")
									.replace("%z%", block.getLocation().getBlockZ() + "")
									.replace("%world%", block.getLocation().getWorld().getName() + "");
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), seight);
						}
					} else {
						b.setCancelled(true);
					}
				}
			}
		}
	}
}
