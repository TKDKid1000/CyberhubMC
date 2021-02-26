package net.Cyberhub.tkdkid1000.utils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.Cyberhub.tkdkid1000.CyberhubUHC;

public class LeaveJoinSaving implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		CyberhubUHC.getMain().loadData(e.getPlayer());
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		CyberhubUHC.getMain().saveData(e.getPlayer());
	}
}
