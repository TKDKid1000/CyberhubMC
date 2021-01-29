package net.Cyberhub.tkdkid1000.resources;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class Gui implements Listener, CommandExecutor {


	Inventory inv = Bukkit.createInventory(null, 54, "§6Shop");
	private FileConfiguration config;
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!event.getInventory().getName().equals(inv.getName())) return;
		if (!(event.getWhoClicked() instanceof Player)) return;
		if (event.getCurrentItem() == null) return;
		Player player = (Player) event.getWhoClicked();
		Inventory playerinv = player.getInventory();
		ItemStack item = event.getCurrentItem();
		String materialname = item.getType().toString();
		for (String itemsection : config.getConfigurationSection("gui").getKeys(false)) {
			ConfigurationSection section = config.getConfigurationSection("gui."+itemsection);
			if (section.getString("item").equalsIgnoreCase(materialname)) {
				String location = (String) section.getConfigurationSection("cost").getKeys(false).toArray()[0];
				Material itemtype = Material.matchMaterial(location);
				int count = section.getInt("cost."+location);
				if (playerinv.contains(itemtype, count)) {
					if (count <= 0) return;
			        int size = playerinv.getSize();
			        for (int slot = 0; slot < size; slot++) {
			            ItemStack is = playerinv.getItem(slot);
			            if (is == null) continue;
			            if (itemtype == is.getType()) {
			                int newAmount = is.getAmount() - count;
			                if (newAmount > 0) {
			                    is.setAmount(newAmount);
			                    break;
			                } else {
			                    playerinv.clear(slot);
			                    count = -newAmount;
			                    if (count == 0) break;
			                }
			            }
			        }
					player.getInventory().addItem(new ItemStack(item));
					player.sendMessage(ChatColor.GREEN + "Successfully purchased " + item.getType().toString());
				} else {
					player.sendMessage(ChatColor.GREEN + "You cannot afford that item! You need " + count + " " + itemtype.toString());
				}
			}
		}
		event.setCancelled(true);
	}
	
	public Gui(FileConfiguration config) {
		this.config = config;
	}
	
	public void openinv(Player player) {
		for (String itemsection : config.getConfigurationSection("gui").getKeys(false)) {
			ConfigurationSection section = config.getConfigurationSection("gui."+itemsection);
			int pos = section.getInt("pos");
			ItemStack item = new ItemStack(Material.matchMaterial(section.getString("item")), section.getInt("count"));
			ItemMeta meta = item.getItemMeta();
			List<String> lore = new ArrayList<String>();
			String location = (String) section.getConfigurationSection("cost").getKeys(false).toArray()[0];
			String cost = ChatColor.WHITE + location + " " + section.getInt("cost."+location);
			lore.add(cost);
			meta.setLore(lore);
			item.setItemMeta(meta);
			inv.setItem(pos, item);
		}
		player.openInventory(inv);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			openinv(player);
		}
		return true;
	}
}
