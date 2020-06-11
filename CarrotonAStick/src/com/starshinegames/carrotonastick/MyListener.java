package com.starshinegames.carrotonastick;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
//import org.bukkit.event.inventory.InventoryAction;
//import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.angeschossen.lands.api.integration.LandsIntegration;


public class MyListener implements Listener {
	private final LandsIntegration landsAddon;
	// CoreProtectAPI cp;

	public MyListener(Plugin p) {
		// Initializing the CoreProtectAPI and LandsIntegration API. Technically, CPAPI
		// is not used here, and can be removed. But it might be used in future
		// versions.

		// cp = new CoreProtectAPI();
		landsAddon = new LandsIntegration(p, false);
	}

	@EventHandler
	public void carrotHeld(PlayerItemHeldEvent event) {
		// This gives players two potion effects when they switch to the given item.
		// This is for the gun textures on the server to work.
		// Getting the new slot allows the potion effects to be applied when the player
		// holds the carrot on a stick instead of when they switch off it
		Player player = event.getPlayer();
		int itemId = event.getNewSlot();
		if (itemId == event.getNewSlot() && player.getInventory().getItem(itemId) != null) {
			Material getObject = player.getInventory().getItem(itemId).getType();

			if (getObject == Material.CARROT_ON_A_STICK) {
				event.getPlayer()
						.addPotionEffect((new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 4)));
				event.getPlayer()
						.addPotionEffect((new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 9)));
			} else {
				event.getPlayer().removePotionEffect(PotionEffectType.FAST_DIGGING);
				event.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
			}
		} else {
			event.getPlayer().removePotionEffect(PotionEffectType.FAST_DIGGING);
			event.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
		}
		

	}

	

	public String rpGetPlayerDirection(Player playerSelf) {
		// This is for the hammer. It provides the Hammer event with what direction the
		// player is facing
		String dir = "";
		float y = playerSelf.getLocation().getYaw();
		if (y < 0) {
			y += 360;
		}
		y %= 360;
		int i = (int) ((y + 8) / 22.5);
		if (i == 0) {
			dir = "west";
		} else if (i == 1) {
			dir = "west northwest";
		} else if (i == 2) {
			dir = "northwest";
		} else if (i == 3) {
			dir = "north northwest";
		} else if (i == 4) {
			dir = "north";
		} else if (i == 5) {
			dir = "north northeast";
		} else if (i == 6) {
			dir = "northeast";
		} else if (i == 7) {
			dir = "east northeast";
		} else if (i == 8) {
			dir = "east";
		} else if (i == 9) {
			dir = "east southeast";
		} else if (i == 10) {
			dir = "southeast";
		} else if (i == 11) {
			dir = "south southeast";
		} else if (i == 12) {
			dir = "south";
		} else if (i == 13) {
			dir = "south southwest";
		} else if (i == 14) {
			dir = "southwest";
		} else if (i == 15) {
			dir = "west southwest";
		} else {
			dir = "west";
		}
		return dir;
	}

	@EventHandler
	public void Hammer(BlockBreakEvent e) {
		// String to be used for the command to give players custom class experience
		String s;

		Player p = e.getPlayer();
		// Prevents an error resulting from null being returned. (No console clog)
		if (!p.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
			if (p.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
				if (p.getInventory().getItemInMainHand().getItemMeta().getLore().size() >= 5) {
					// For mmoitems, the lore starts in line 5
					if (p.getInventory().getItemInMainHand().getItemMeta().getLore().get(4).contains("Hammer")) {
						// Custom ores use mushroom blocks and are dropped from a command, so this
						// prevents mushrooms from being dropped
						if (e.getBlock().getType().equals(Material.BROWN_MUSHROOM_BLOCK)
								|| e.getBlock().getType().equals(Material.RED_MUSHROOM_BLOCK)
								|| e.getBlock().getType().equals(Material.MUSHROOM_STEM)) {
							e.setDropItems(false);
							// These are the blocks the hammer effect is applied to
						} else if (e.getBlock().getType().equals(Material.STONE)
								|| e.getBlock().getType().equals(Material.ANDESITE)
								|| e.getBlock().getType().equals(Material.GRANITE)
								|| e.getBlock().getType().equals(Material.SANDSTONE)
								|| e.getBlock().getType().equals(Material.RED_SANDSTONE)
								|| e.getBlock().getType().equals(Material.COAL_ORE)
								|| e.getBlock().getType().equals(Material.IRON_ORE)
								|| e.getBlock().getType().equals(Material.GOLD_ORE)
								|| e.getBlock().getType().equals(Material.DIAMOND_ORE)
								|| e.getBlock().getType().equals(Material.COBBLESTONE)
								|| e.getBlock().getType().equals(Material.DIORITE)
								|| e.getBlock().getType().equals(Material.NETHERRACK)
								|| e.getBlock().getType().equals(Material.END_STONE)
								|| e.getBlock().getType().equals(Material.NETHER_QUARTZ_ORE)
								|| e.getBlock().getType().equals(Material.LAPIS_ORE)
								|| e.getBlock().getType().equals(Material.NETHER_BRICK)
								|| e.getBlock().getType().equals(Material.COBBLESTONE)
								|| e.getBlock().getType().equals(Material.REDSTONE_ORE)
								|| e.getBlock().getType().toString().toUpperCase().contains("CLAY")
								|| e.getBlock().getType().toString().toUpperCase().contains("TERRACOTTA")
								|| e.getBlock().getType().toString().toUpperCase().contains("ICE"))
						{
							// Get the player direction to determine which blocks to break.
							String dir = rpGetPlayerDirection(p);
							// If the player is looking down, break a 3x1x3 hole (x, y, z)
							if ((Math.abs((int) p.getLocation().getPitch() % 360) <= 110
									&& Math.abs((int) p.getLocation().getPitch() % 360) >= 70)
									|| (Math.abs((int) p.getLocation().getPitch() % 360) >= 250
											&& Math.abs((int) p.getLocation().getPitch() % 360) <= 290)) {
								Location loc = e.getBlock().getLocation();
								for (int x = loc.getBlockX() - 1; x <= loc.getBlockX() + 1; x++) {
									for (int y = loc.getBlockY(); y <= loc.getBlockY(); y++) {
										for (int z = loc.getBlockZ() - 1; z <= loc.getBlockZ() + 1; z++) {
											Block b = loc.getWorld().getBlockAt(x, y, z);
											Location bLoc = b.getLocation();
											// Make sure the player has permission to break the selected block
											// Null check
											if (landsAddon.getLand(bLoc) != null) {
												if (landsAddon.getLand(bLoc).isTrusted(p.getUniqueId()) && !landsAddon.getLand(bLoc).isInWar()) {
													if (!b.getType().equals(Material.BROWN_MUSHROOM_BLOCK)
															&& !b.getType().equals(Material.RED_MUSHROOM_BLOCK)
															&& !b.getType().equals(Material.MUSHROOM_STEM)) {
														// Don't want players to break their way to the void now do we
														if (!b.getType().equals(Material.BEDROCK) && (b.getType().equals(Material.STONE)
																|| b.getType().equals(Material.ANDESITE)
																|| b.getType().equals(Material.GRANITE)
																|| b.getType().equals(Material.SANDSTONE)
																|| b.getType().equals(Material.RED_SANDSTONE)
																|| b.getType().equals(Material.COAL_ORE)
																|| b.getType().equals(Material.IRON_ORE)
																|| b.getType().equals(Material.GOLD_ORE)
																|| b.getType().equals(Material.DIAMOND_ORE)
																|| b.getType().equals(Material.COBBLESTONE)
																|| b.getType().equals(Material.DIORITE)
																|| b.getType().equals(Material.NETHERRACK)
																|| b.getType().equals(Material.END_STONE)
																|| b.getType().equals(Material.NETHER_QUARTZ_ORE)
																|| b.getType().equals(Material.LAPIS_ORE)
																|| b.getType().equals(Material.NETHER_BRICK)
																|| b.getType().equals(Material.COBBLESTONE)
																|| b.getType().equals(Material.REDSTONE_ORE) || b.getType().equals(Material.OBSIDIAN)
																|| b.getType().toString().toUpperCase().contains("CLAY")
																|| b.getType().toString().toUpperCase().contains("TERRACOTTA")
																|| b.getType().toString().toUpperCase().contains("ICE"))) {
															// The original block is broken normally
															if (!e.getBlock().getLocation().equals(bLoc)) {
																for (ItemStack drops : b.getDrops()) {
																	e.getPlayer().getInventory()
																			.addItem(new ItemStack[] { drops });
																}
																e.setDropItems(false);
																// Give the player vanilla and class exp if they are
																// breaking an ore, but only give class exp if they are
																// a blacksmith or engineer
																if (b.getType().equals(Material.COAL_ORE)) {
																	s = "mmocore admin exp give %player% main 1"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(1);
																} else if (b.getType().equals(Material.IRON_ORE)) {
																	s = "mmocore admin exp give %player% main 3"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}

																} else if (b.getType().equals(Material.GOLD_ORE)) {
																	s = "mmocore admin exp give %player% main 6"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																} else if (b.getType().equals(Material.EMERALD_ORE)) {
																	s = "mmocore admin exp give %player% main 8"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(5);
																} else if (b.getType().equals(Material.DIAMOND_ORE)) {
																	s = "mmocore admin exp give %player% main 10"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(5);
																} else if (b.getType()
																		.equals(Material.NETHER_QUARTZ_ORE)) {
																	s = "mmocore admin exp give %player% main 2"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(4);
																} else if (b.getType().equals(Material.LAPIS_ORE)) {
																	s = "mmocore admin exp give %player% main 5"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(7);
																} else if (b.getType().equals(Material.REDSTONE_ORE)) {
																	s = "mmocore admin exp give %player% main 3"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(4);
																}
																b.breakNaturally(e.getPlayer().getInventory()
																		.getItemInMainHand());
															}
														}
													}
												}
											} else {
												// Runs the second lands check to see if it is wilderness
												if (!landsAddon.isClaimed(bLoc)) {
													if (!b.getType().equals(Material.BROWN_MUSHROOM_BLOCK)
															&& !b.getType().equals(Material.RED_MUSHROOM_BLOCK)
															&& !b.getType().equals(Material.MUSHROOM_STEM)) {

														if (!b.getType().equals(Material.BEDROCK) && (b.getType().equals(Material.STONE)
																|| b.getType().equals(Material.ANDESITE)
																|| b.getType().equals(Material.GRANITE)
																|| b.getType().equals(Material.SANDSTONE)
																|| b.getType().equals(Material.RED_SANDSTONE)
																|| b.getType().equals(Material.COAL_ORE)
																|| b.getType().equals(Material.IRON_ORE)
																|| b.getType().equals(Material.GOLD_ORE)
																|| b.getType().equals(Material.DIAMOND_ORE)
																|| b.getType().equals(Material.COBBLESTONE)
																|| b.getType().equals(Material.DIORITE)
																|| b.getType().equals(Material.NETHERRACK)
																|| b.getType().equals(Material.END_STONE)
																|| b.getType().equals(Material.NETHER_QUARTZ_ORE)
																|| b.getType().equals(Material.LAPIS_ORE)
																|| b.getType().equals(Material.NETHER_BRICK)
																|| b.getType().equals(Material.COBBLESTONE)
																|| b.getType().equals(Material.REDSTONE_ORE) || b.getType().equals(Material.OBSIDIAN)
																|| b.getType().toString().toUpperCase().contains("CLAY")
																|| b.getType().toString().toUpperCase().contains("TERRACOTTA")
																|| b.getType().toString().toUpperCase().contains("ICE"))) {
															if (!e.getBlock().getLocation().equals(bLoc)) {
																for (ItemStack drops : b.getDrops()) {
																	e.getPlayer().getInventory()
																			.addItem(new ItemStack[] { drops });
																}
																e.setDropItems(false);

																if (b.getType().equals(Material.COAL_ORE)) {
																	s = "mmocore admin exp give %player% main 1"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(1);
																} else if (b.getType().equals(Material.IRON_ORE)) {
																	s = "mmocore admin exp give %player% main 3"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}

																} else if (b.getType().equals(Material.GOLD_ORE)) {
																	s = "mmocore admin exp give %player% main 6"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																} else if (b.getType().equals(Material.EMERALD_ORE)) {
																	s = "mmocore admin exp give %player% main 8"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(5);
																} else if (b.getType().equals(Material.DIAMOND_ORE)) {
																	s = "mmocore admin exp give %player% main 10"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(5);
																} else if (b.getType()
																		.equals(Material.NETHER_QUARTZ_ORE)) {
																	s = "mmocore admin exp give %player% main 2"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(4);
																} else if (b.getType().equals(Material.LAPIS_ORE)) {
																	s = "mmocore admin exp give %player% main 5"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(7);
																} else if (b.getType().equals(Material.REDSTONE_ORE)) {
																	s = "mmocore admin exp give %player% main 3"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(4);
																}
																b.breakNaturally(e.getPlayer().getInventory()
																		.getItemInMainHand());
															}
														}
													}
												}
											}
										}
									}
								}
								// Breaks a 1 x 3 x 3
							} else if (dir.equals("south") || dir.equals("north") || dir.equals("south southwest")
									|| dir.equals("south") || dir.equals("north northwest")
									|| dir.equals("south southeast") || dir.equals("north northeast")) {
								Location loc = e.getBlock().getLocation();
								for (int x = loc.getBlockX(); x <= loc.getBlockX(); x++) {
									for (int y = loc.getBlockY() - 1; y <= loc.getBlockY() + 1; y++) {
										for (int z = loc.getBlockZ() - 1; z <= loc.getBlockZ() + 1; z++) {
											Block b = loc.getWorld().getBlockAt(x, y, z);
											Location bLoc = b.getLocation();

											if (landsAddon.getLand(bLoc) != null) {
												if (landsAddon.getLand(bLoc).isTrusted(p.getUniqueId())&& !landsAddon.getLand(bLoc).isInWar()) {
													if (!b.getType().equals(Material.BROWN_MUSHROOM_BLOCK)
															&& !b.getType().equals(Material.RED_MUSHROOM_BLOCK)
															&& !b.getType().equals(Material.MUSHROOM_STEM)) {

														if (!b.getType().equals(Material.BEDROCK) && (b.getType().equals(Material.STONE)
																|| b.getType().equals(Material.ANDESITE)
																|| b.getType().equals(Material.GRANITE)
																|| b.getType().equals(Material.SANDSTONE)
																|| b.getType().equals(Material.RED_SANDSTONE)
																|| b.getType().equals(Material.COAL_ORE)
																|| b.getType().equals(Material.IRON_ORE)
																|| b.getType().equals(Material.GOLD_ORE)
																|| b.getType().equals(Material.DIAMOND_ORE)
																|| b.getType().equals(Material.COBBLESTONE)
																|| b.getType().equals(Material.DIORITE)
																|| b.getType().equals(Material.NETHERRACK)
																|| b.getType().equals(Material.END_STONE)
																|| b.getType().equals(Material.NETHER_QUARTZ_ORE)
																|| b.getType().equals(Material.LAPIS_ORE)
																|| b.getType().equals(Material.NETHER_BRICK)
																|| b.getType().equals(Material.COBBLESTONE)
																|| b.getType().equals(Material.REDSTONE_ORE) || b.getType().equals(Material.OBSIDIAN)
																|| b.getType().toString().toUpperCase().contains("CLAY")
																|| b.getType().toString().toUpperCase().contains("TERRACOTTA")
																|| b.getType().toString().toUpperCase().contains("ICE"))) {
															// e.setCancelled(true);
															if (!e.getBlock().getLocation().equals(bLoc)) {
																for (ItemStack drops : b.getDrops()) {
																	e.getPlayer().getInventory()
																			.addItem(new ItemStack[] { drops });
																}
																// e.setCancelled(true);
																e.setDropItems(false);

																if (b.getType().equals(Material.COAL_ORE)) {
																	s = "mmocore admin exp give %player% main 1"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(1);
																} else if (b.getType().equals(Material.IRON_ORE)) {
																	s = "mmocore admin exp give %player% main 3"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}

																} else if (b.getType().equals(Material.GOLD_ORE)) {
																	s = "mmocore admin exp give %player% main 6"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																} else if (b.getType().equals(Material.EMERALD_ORE)) {
																	s = "mmocore admin exp give %player% main 8"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(5);
																} else if (b.getType().equals(Material.DIAMOND_ORE)) {
																	s = "mmocore admin exp give %player% main 10"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(5);
																} else if (b.getType()
																		.equals(Material.NETHER_QUARTZ_ORE)) {
																	s = "mmocore admin exp give %player% main 2"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(4);
																} else if (b.getType().equals(Material.LAPIS_ORE)) {
																	s = "mmocore admin exp give %player% main 5"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(7);
																} else if (b.getType().equals(Material.REDSTONE_ORE)) {
																	s = "mmocore admin exp give %player% main 3"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(4);
																}
																b.breakNaturally(e.getPlayer().getInventory()
																		.getItemInMainHand());
															}
														}
													}
												}
											} else {
												if (!landsAddon.isClaimed(bLoc)) {
													if (!b.getType().equals(Material.BROWN_MUSHROOM_BLOCK)
															&& !b.getType().equals(Material.RED_MUSHROOM_BLOCK)
															&& !b.getType().equals(Material.MUSHROOM_STEM)) {

														if (!b.getType().equals(Material.BEDROCK) && (b.getType().equals(Material.STONE)
																|| b.getType().equals(Material.ANDESITE)
																|| b.getType().equals(Material.GRANITE)
																|| b.getType().equals(Material.SANDSTONE)
																|| b.getType().equals(Material.RED_SANDSTONE)
																|| b.getType().equals(Material.COAL_ORE)
																|| b.getType().equals(Material.IRON_ORE)
																|| b.getType().equals(Material.GOLD_ORE)
																|| b.getType().equals(Material.DIAMOND_ORE)
																|| b.getType().equals(Material.COBBLESTONE)
																|| b.getType().equals(Material.DIORITE)
																|| b.getType().equals(Material.NETHERRACK)
																|| b.getType().equals(Material.END_STONE)
																|| b.getType().equals(Material.NETHER_QUARTZ_ORE)
																|| b.getType().equals(Material.LAPIS_ORE)
																|| b.getType().equals(Material.NETHER_BRICK)
																|| b.getType().equals(Material.COBBLESTONE)
																|| b.getType().equals(Material.REDSTONE_ORE) || b.getType().equals(Material.OBSIDIAN)
																|| b.getType().toString().toUpperCase().contains("CLAY")
																|| b.getType().toString().toUpperCase().contains("TERRACOTTA")
																|| b.getType().toString().toUpperCase().contains("ICE"))) {
															// e.setCancelled(true);
															if (!e.getBlock().getLocation().equals(bLoc)) {
																for (ItemStack drops : b.getDrops()) {
																	e.getPlayer().getInventory()
																			.addItem(new ItemStack[] { drops });
																}
																// e.setCancelled(true);
																e.setDropItems(false);

																if (b.getType().equals(Material.COAL_ORE)) {
																	s = "mmocore admin exp give %player% main 1"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(1);
																} else if (b.getType().equals(Material.IRON_ORE)) {
																	s = "mmocore admin exp give %player% main 3"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}

																} else if (b.getType().equals(Material.GOLD_ORE)) {
																	s = "mmocore admin exp give %player% main 6"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																} else if (b.getType().equals(Material.EMERALD_ORE)) {
																	s = "mmocore admin exp give %player% main 8"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(5);
																} else if (b.getType().equals(Material.DIAMOND_ORE)) {
																	s = "mmocore admin exp give %player% main 10"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(5);
																} else if (b.getType()
																		.equals(Material.NETHER_QUARTZ_ORE)) {
																	s = "mmocore admin exp give %player% main 2"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(4);
																} else if (b.getType().equals(Material.LAPIS_ORE)) {
																	s = "mmocore admin exp give %player% main 5"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(7);
																} else if (b.getType().equals(Material.REDSTONE_ORE)) {
																	s = "mmocore admin exp give %player% main 3"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(4);
																}
																b.breakNaturally(e.getPlayer().getInventory()
																		.getItemInMainHand());
															}
														}
													}
												}
											}
										}
									}
								}

							}
							// Breaks a 3 x 3 x 1
							else {

								Location loc = e.getBlock().getLocation();
								for (int x = loc.getBlockX() - 1; x <= loc.getBlockX() + 1; x++) {
									for (int y = loc.getBlockY() - 1; y <= loc.getBlockY() + 1; y++) {
										for (int z = loc.getBlockZ(); z <= loc.getBlockZ(); z++) {
											Block b = loc.getWorld().getBlockAt(x, y, z);
											Location bLoc = b.getLocation();
											if (landsAddon.getLand(bLoc) != null) {
												if (landsAddon.getLand(bLoc).isTrusted(p.getUniqueId()) && !landsAddon.getLand(bLoc).isInWar()) {
													if (!b.getType().equals(Material.BROWN_MUSHROOM_BLOCK)
															&& !b.getType().equals(Material.RED_MUSHROOM_BLOCK)
															&& !b.getType().equals(Material.MUSHROOM_STEM)) {

														if (!b.getType().equals(Material.BEDROCK) && (b.getType().equals(Material.STONE)
																|| b.getType().equals(Material.ANDESITE)
																|| b.getType().equals(Material.GRANITE)
																|| b.getType().equals(Material.SANDSTONE)
																|| b.getType().equals(Material.RED_SANDSTONE)
																|| b.getType().equals(Material.COAL_ORE)
																|| b.getType().equals(Material.IRON_ORE)
																|| b.getType().equals(Material.GOLD_ORE)
																|| b.getType().equals(Material.DIAMOND_ORE)
																|| b.getType().equals(Material.COBBLESTONE)
																|| b.getType().equals(Material.DIORITE)
																|| b.getType().equals(Material.NETHERRACK)
																|| b.getType().equals(Material.END_STONE)
																|| b.getType().equals(Material.NETHER_QUARTZ_ORE)
																|| b.getType().equals(Material.LAPIS_ORE)
																|| b.getType().equals(Material.NETHER_BRICK)
																|| b.getType().equals(Material.COBBLESTONE)
																|| b.getType().equals(Material.REDSTONE_ORE)
																|| b.getType().toString().toUpperCase().contains("CLAY") || b.getType().equals(Material.OBSIDIAN)
																|| b.getType().toString().toUpperCase().contains("TERRACOTTA")
																|| b.getType().toString().toUpperCase().contains("ICE"))) {
															// e.setCancelled(true);
															if (!e.getBlock().getLocation().equals(bLoc)) {
																for (ItemStack drops : b.getDrops()) {
																	e.getPlayer().getInventory()
																			.addItem(new ItemStack[] { drops });
																}
																// e.setCancelled(true);

																if (b.getType().equals(Material.COAL_ORE)) {
																	s = "mmocore admin exp give %player% main 1"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(1);
																} else if (b.getType().equals(Material.IRON_ORE)) {
																	s = "mmocore admin exp give %player% main 3"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}

																} else if (b.getType().equals(Material.GOLD_ORE)) {
																	s = "mmocore admin exp give %player% main 6"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																} else if (b.getType().equals(Material.EMERALD_ORE)) {
																	s = "mmocore admin exp give %player% main 8"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(5);
																} else if (b.getType().equals(Material.DIAMOND_ORE)) {
																	s = "mmocore admin exp give %player% main 10"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(5);
																} else if (b.getType()
																		.equals(Material.NETHER_QUARTZ_ORE)) {
																	s = "mmocore admin exp give %player% main 2"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(4);
																} else if (b.getType().equals(Material.LAPIS_ORE)) {
																	s = "mmocore admin exp give %player% main 5"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(7);
																} else if (b.getType().equals(Material.REDSTONE_ORE)) {
																	s = "mmocore admin exp give %player% main 3"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(4);
																}
																b.breakNaturally(e.getPlayer().getInventory()
																		.getItemInMainHand());
															}
														}
													}
												}
											} else {
												if (!landsAddon.isClaimed(bLoc)) {
													if (!b.getType().equals(Material.BROWN_MUSHROOM_BLOCK)
															&& !b.getType().equals(Material.RED_MUSHROOM_BLOCK)
															&& !b.getType().equals(Material.MUSHROOM_STEM)) {

														if (!b.getType().equals(Material.BEDROCK) && (b.getType().equals(Material.STONE)
																|| b.getType().equals(Material.ANDESITE)
																|| b.getType().equals(Material.GRANITE)
																|| b.getType().equals(Material.SANDSTONE)
																|| b.getType().equals(Material.RED_SANDSTONE)
																|| b.getType().equals(Material.COAL_ORE)
																|| b.getType().equals(Material.IRON_ORE)
																|| b.getType().equals(Material.GOLD_ORE)
																|| b.getType().equals(Material.DIAMOND_ORE)
																|| b.getType().equals(Material.COBBLESTONE)
																|| b.getType().equals(Material.DIORITE)
																|| b.getType().equals(Material.NETHERRACK)
																|| b.getType().equals(Material.END_STONE)
																|| b.getType().equals(Material.NETHER_QUARTZ_ORE)
																|| b.getType().equals(Material.LAPIS_ORE)
																|| b.getType().equals(Material.NETHER_BRICK)
																|| b.getType().equals(Material.COBBLESTONE)
																|| b.getType().equals(Material.REDSTONE_ORE) || b.getType().equals(Material.OBSIDIAN)
																|| b.getType().toString().toUpperCase().contains("CLAY")
																|| b.getType().toString().toUpperCase().contains("TERRACOTTA")
																|| b.getType().toString().toUpperCase().contains("ICE"))) {
															// e.setCancelled(true);
															if (!e.getBlock().getLocation().equals(bLoc)) {
																for (ItemStack drops : b.getDrops()) {
																	e.getPlayer().getInventory()
																			.addItem(new ItemStack[] { drops });
																}
																// e.setCancelled(true);
																e.setDropItems(false);

																if (b.getType().equals(Material.COAL_ORE)) {
																	s = "mmocore admin exp give %player% main 1"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(1);
																} else if (b.getType().equals(Material.IRON_ORE)) {
																	s = "mmocore admin exp give %player% main 3"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}

																} else if (b.getType().equals(Material.GOLD_ORE)) {
																	s = "mmocore admin exp give %player% main 6"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																} else if (b.getType().equals(Material.EMERALD_ORE)) {
																	s = "mmocore admin exp give %player% main 8"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(5);
																} else if (b.getType().equals(Material.DIAMOND_ORE)) {
																	s = "mmocore admin exp give %player% main 10"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(5);
																} else if (b.getType()
																		.equals(Material.NETHER_QUARTZ_ORE)) {
																	s = "mmocore admin exp give %player% main 2"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(4);
																} else if (b.getType().equals(Material.LAPIS_ORE)) {
																	s = "mmocore admin exp give %player% main 5"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(7);
																} else if (b.getType().equals(Material.REDSTONE_ORE)) {
																	s = "mmocore admin exp give %player% main 3"
																			.replace("%player%", p.getName());
																	if (p.hasPermission("mmocore.blacksmith")
																			|| p.hasPermission("mmocore.engineer")) {
																		Bukkit.dispatchCommand(
																				Bukkit.getConsoleSender(), s);
																	}
																	p.giveExp(4);
																}
																b.breakNaturally(e.getPlayer().getInventory()
																		.getItemInMainHand());
															}
														}
													}
												}
											}
										}
									}
								}
							}
						} // The main block is broken directly by the player, with no interference from
							// the plugin
						if (landsAddon.getLand(e.getBlock().getLocation()) != null)
						{
						if (landsAddon.getLand(e.getBlock().getLocation()).isTrusted(e.getPlayer().getUniqueId()))
						{
							//e.getPlayer().sendMessage("You are trusted");
							e.setCancelled(false);
						}
						} else if (!landsAddon.isClaimed(e.getBlock().getLocation()))
						{
							//e.getPlayer().sendMessage("You are in wilderness");
							e.setCancelled(false);	
						} else
						{
							//e.getPlayer().sendMessage("No");
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}
}
