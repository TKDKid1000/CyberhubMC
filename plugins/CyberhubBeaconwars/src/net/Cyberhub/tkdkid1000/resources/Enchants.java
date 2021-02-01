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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class Enchants implements Listener, CommandExecutor {


	Inventory inv = Bukkit.createInventory(null, 54, "§bEnchants");
	private FileConfiguration config;
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!event.getInventory().getName().equals(inv.getName())) return;
		if (!(event.getWhoClicked() instanceof Player)) return;
		if (event.getCurrentItem() == null) return;
		Player player = (Player) event.getWhoClicked();
		Inventory playerinv = player.getInventory();
		ItemStack item = event.getCurrentItem();
		String enchantname = item.getItemMeta().getDisplayName();
		if (player.getInventory().getItemInHand() == null) {
			player.sendMessage(ChatColor.GREEN + "You need an item to enchant!");
			event.setCancelled(true);
			return;
		}
		ItemStack hand = player.getInventory().getItemInHand();
		for (String itemsection : config.getConfigurationSection("gui").getKeys(false)) {
			ConfigurationSection section = config.getConfigurationSection("gui."+itemsection);
			if (section.getString("enchant").equalsIgnoreCase(enchantname)) {
				int count = section.getInt("cost");
				ItemMeta meta = hand.getItemMeta();
				if (!meta.addEnchant(Enchantment.getByName(enchantname), section.getInt("level"), false)) {
					player.sendMessage(ChatColor.GREEN + "That item can't use that enchantment.");
					event.setCancelled(true);
					return;
				}
				if (playerinv.contains(Material.DIAMOND, count)) {
					if (count <= 0) return;
			        int size = playerinv.getSize();
			        for (int slot = 0; slot < size; slot++) {
			            ItemStack is = playerinv.getItem(slot);
			            if (is == null) continue;
			            if (Material.DIAMOND == is.getType()) {
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
			        hand.setItemMeta(meta);
			        player.getInventory().setItemInHand(hand);
					player.sendMessage(ChatColor.GREEN + "Successfully purchased " + item.getType().toString());
				} else {
					player.sendMessage(ChatColor.GREEN + "You cannot afford that item! You need " + count + " diamonds!");
				}
			}
		}
		event.setCancelled(true);
	}
	
	public Enchants(FileConfiguration config) {
		this.config = config;
	}
	
	public void openinv(Player player) {
		for (String itemsection : config.getConfigurationSection("gui").getKeys(false)) {
			ConfigurationSection section = config.getConfigurationSection("gui."+itemsection);
			int pos = section.getInt("pos");
			ItemStack item = new ItemStack(Material.BOOK, section.getInt("level"));
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(section.getString("enchant"));
			List<String> lore = new ArrayList<String>();
			String cost = ChatColor.WHITE + "" + section.getInt("cost") + " diamonds.";
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
