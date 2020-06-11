package com.starshinegames.mmoexpboost;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.Indyuce.mmocore.api.event.PlayerExperienceGainEvent;

public class MyListener implements Listener {

	@EventHandler
	public void expboost(PlayerExperienceGainEvent p) {
		//By using a permissions plugin that allows for temporary permissions, you can just temporarily give them one of these permissions in buycraft. This stacks with the global exp boost already built into MMOCore and MMOitems.
		if (p.getPlayer().hasPermission("xpboost.3.0")) {
			p.setExperience((int) (p.getExperience() * 3));
		} else if (p.getPlayer().hasPermission("xpboost.2.0")) {
			p.setExperience((int) (p.getExperience() * 2));
		} else if (p.getPlayer().hasPermission("xpboost.1.5")) {
			p.setExperience((int) (p.getExperience() * 1.5));
		}
	}
}
