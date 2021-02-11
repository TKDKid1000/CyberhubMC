package net.Cyberhub.tkdkid1000.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class AntiAfk implements Listener {

	public static List<Block> chests = new ArrayList<Block>();
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (chests.contains(block)) {
			if (!block.getType().equals(Material.CHEST)) {
				chests.remove(block);
				return;
			}
			Chest chest = (Chest) block.getState();
			if (isEmpty(chest)) {
				player.sendMessage(ChatColor.GREEN + "GG on not being afk!");
			}
			player.sendMessage(ChatColor.RED + "You have to take the item out of the chest!");
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		Action action = event.getAction();
		Block block = event.getClickedBlock();
		if (action.equals(Action.LEFT_CLICK_BLOCK)) {
			if (block.getType().equals(Material.CHEST) && chests.contains(block) && isEmpty((Chest) block.getState())) {
				block.setType(Material.AIR);
				chests.remove(block);
			}
		}
	}
	
	public boolean isEmpty(Chest chest) {
		int air = 0;
		for (ItemStack item : chest.getBlockInventory().getContents()) {
			if (item == null || item.getType() == Material.AIR) {
				air++;
			}
		}
		if (air > 26 || chest.getBlockInventory().getContents().length == 0) {
			return true;
		}
		 return false;
	}
}
