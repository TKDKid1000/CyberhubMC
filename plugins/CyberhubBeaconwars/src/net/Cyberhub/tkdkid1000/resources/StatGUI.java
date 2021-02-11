package net.Cyberhub.tkdkid1000.resources;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.UserDoesNotExistException;

import net.Cyberhub.tkdkid1000.CyberhubBeaconwars;
import net.Cyberhub.tkdkid1000.utils.ItemBuilder;
import net.md_5.bungee.api.ChatColor;

public class StatGUI implements Listener, CommandExecutor {

	private CyberhubBeaconwars beaconwars;
	private Essentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
	private Inventory gui = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Stats");
	
	public StatGUI(CyberhubBeaconwars beaconwars) {
		this.beaconwars = beaconwars;
	}
	
	public void setup() {
		beaconwars.getServer().getPluginManager().registerEvents(this, beaconwars);
		beaconwars.getCommand("stat").setExecutor(this);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getClickedInventory().getName().equals(gui.getName())) event.setCancelled(true);;
	}
	
	@SuppressWarnings("deprecation")
	public void openGui(Player player) {
		gui.clear();
		gui.setItem(0, new ItemBuilder(Material.DIAMOND_SWORD, 1)
				.setName(ChatColor.GOLD + "Kills: " + ChatColor.GRAY + beaconwars.playerdata.getConfig().getString("playerdata."+player.getUniqueId().toString()+".kills"))
				.build());
		gui.setItem(4, new ItemBuilder(Material.BEACON, 1)
				.setName(ChatColor.GOLD + "Wins: " + ChatColor.GRAY + beaconwars.playerdata.getConfig().getString("playerdata."+player.getUniqueId().toString()+".wins"))
				.build());
		try {
			gui.setItem(2, new ItemBuilder(Material.GOLD_INGOT, 1)
					.setName(ChatColor.GOLD + "Coins: " + ChatColor.GRAY + Economy.getMoney(player.getName()))
					.build());
		} catch (UserDoesNotExistException e) {
			e.printStackTrace();
		}
		gui.setItem(8, new ItemBuilder(Material.REDSTONE, 1)
				.setName(ChatColor.GOLD + "Deaths: " + ChatColor.GRAY + beaconwars.playerdata.getConfig().getString("playerdata."+player.getUniqueId().toString()+".deaths"))
				.build());
		gui.setItem(6, new ItemBuilder(Material.DIAMOND, 1)
				.setName(ChatColor.GOLD + "Rank: " + ChatColor.GRAY + ess.getUser(player).getGroup().substring(0, 1).toUpperCase() + ess.getUser(player).getGroup().substring(1))
				.build());
		player.openInventory(gui);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			openGui(player);
		}
		return true;
	}
}