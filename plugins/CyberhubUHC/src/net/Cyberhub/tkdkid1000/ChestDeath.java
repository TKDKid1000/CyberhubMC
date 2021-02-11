package net.Cyberhub.tkdkid1000;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Chest.Type;
import org.bukkit.block.data.type.Chest;

public class ChestDeath implements Listener {
	
	@EventHandler
	public void onDie(PlayerDeathEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		Player player = event.getEntity();
		Location loc = player.getLocation();
		Location blockLocation = loc;
		Location blockLocation2 = loc.clone().add(1, 0, 0);

		Block block = blockLocation.getBlock();
		Block block2 = blockLocation2.getBlock();

		block.setType( Material.CHEST );
		block2.setType( Material.CHEST );
		org.bukkit.block.Chest chest1 = (org.bukkit.block.Chest) block.getState();
		org.bukkit.block.Chest chest2 = (org.bukkit.block.Chest) block2.getState();
		
		Chest chestBlockState1 = (Chest) block.getBlockData();
		chestBlockState1.setType(Type.LEFT);
		block.setBlockData((BlockData) chestBlockState1, true);
		                       
		Chest chestBlockState2 = (Chest) block.getBlockData();
		chestBlockState2.setType( Type.RIGHT );
		block2.setBlockData((BlockData) chestBlockState2, true);
		List<ItemStack> items = event.getDrops();
		int inchest = 0;
		for (int x=0; x<items.size(); x++) {
			if (items.get(x) != null && items.get(x).getType() != Material.AIR) {
				inchest++;
				if (inchest < 27) {
					chest1.getBlockInventory().addItem(items.get(x));
				} else if (inchest < 54) {
					chest2.getBlockInventory().addItem(items.get(x));
					inchest++;
				}
			}
		}
		event.getDrops().clear();
		player.getInventory().clear();
	}
	
}
