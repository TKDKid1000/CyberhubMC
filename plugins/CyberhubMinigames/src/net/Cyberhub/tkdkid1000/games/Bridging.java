package net.Cyberhub.tkdkid1000.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import net.Cyberhub.tkdkid1000.CyberhubMinigames;
import net.Cyberhub.tkdkid1000.utils.YamlConfig;
import net.md_5.bungee.api.ChatColor;

public class Bridging implements Listener {

	private static HashMap<Player, Long> sessions = new HashMap<Player, Long>();
	private static HashMap<Player, Integer> blocks = new HashMap<Player, Integer>();
	private FileConfiguration config;
	private CyberhubMinigames minigames;
	private YamlConfig data;
	
	public Bridging(CyberhubMinigames minigames, FileConfiguration config, YamlConfig data) {
		this.minigames = minigames;
		this.config = config;
		this.data = data;
	}
	
	public void setup() {
		data.createConfig();
		minigames.getServer().getPluginManager().registerEvents(this, minigames);
	}
	
	public void startRun(Player player) {
		if (!data.getConfig().contains("bridging"+player.getUniqueId().toString())) {
			List<Long> basetime = new ArrayList<Long>();
			basetime.add((long) 10000);
			data.getConfig().set("bridging."+player.getUniqueId().toString(), basetime);
			data.save();
			data.reload();
		}
		sessions.put(player, System.currentTimeMillis());
		blocks.put(player, 0);
		player.sendMessage(ChatColor.GREEN + "Bridge as fast as you can!");
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (sessions.containsKey(player)) {
			if (player.isOnGround()) {
				if (player.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.GOLD_BLOCK) {
					long time = Math.round((System.currentTimeMillis() - sessions.get(player))/1000);
					player.sendMessage(ChatColor.GREEN + "Your bridging time was " + time + " seconds! You placed " + blocks.get(player) + " blocks.");
					List<Long> runs = data.getConfig().getLongList("bridging."+player.getUniqueId().toString());
					if (Collections.min(runs) > time) {
						player.sendMessage(ChatColor.GREEN + "You beat your best time! GG!");
					} else {
						player.sendMessage(ChatColor.RED + "You didn't beat any of your records.");
					}
					runs.add(time);
					data.getConfig().set("bridging."+player.getUniqueId().toString(), runs);
					data.save();
					data.reload();
					sessions.remove(player);
					blocks.remove(player);
					player.performCommand("spawn");
				}
			}
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (player.hasPermission("cyberhubminigames.override")) return;
		if (!player.getWorld().getName().equalsIgnoreCase(config.getString("bridging.world"))) return;
		if (event.getBlockAgainst().getType() == Material.DIAMOND_BLOCK || event.getBlockAgainst().getType() == Material.WOOL) {
			if (!sessions.containsKey(player)) {
				startRun(player);
			} else {
				blocks.replace(player, blocks.get(player)+1);
			}
		} else {
			player.sendMessage(ChatColor.RED + "You can only place blocks on diamond and wool!");
			event.setCancelled(true);
		}
	}
}
