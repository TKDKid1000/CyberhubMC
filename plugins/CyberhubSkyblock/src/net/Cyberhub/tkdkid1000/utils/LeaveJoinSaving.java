package net.Cyberhub.tkdkid1000.utils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.Cyberhub.tkdkid1000.CyberhubSkyblock;

public class LeaveJoinSaving implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		CyberhubSkyblock.getMain().loadIslands(e.getPlayer());
		CyberhubSkyblock.getMain().getLogger().info("Loading files and adding " + e.getPlayer().getName());
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		CyberhubSkyblock.getMain().saveIslands(e.getPlayer());
		CyberhubSkyblock.getMain().getLogger().info("Saving files and removing " + e.getPlayer().getName());
	}
}
