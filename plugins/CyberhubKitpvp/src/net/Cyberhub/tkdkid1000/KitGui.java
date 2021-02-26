package net.Cyberhub.tkdkid1000;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.Kit;
import com.earth2me.essentials.User;

import net.ess3.api.IEssentials;

public class KitGui implements Listener {

	Inventory inv = Bukkit.createInventory(null, 54, "Kits");
	
	@EventHandler
	public void onClick(InventoryClickEvent event) throws Exception {
		if (!(event.getWhoClicked() instanceof Player)) return;
		if (event.getCurrentItem() != null);
		ItemStack clicked = event.getCurrentItem();
		ItemMeta meta = clicked.getItemMeta();
		Player player = (Player) event.getWhoClicked();
		IEssentials ess = CyberhubKitpvp.getPlugin(Essentials.class);
		if (event.getInventory().equals(inv)) {
			User user = ess.getUser(player);
			Kit kit = new Kit(meta.getDisplayName(), ess);
			kit.expandItems(user);
		}
	}
	
	public void openInv(Player p) {
		
	}
}
