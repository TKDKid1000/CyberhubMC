package net.Cyberhub.tkdkid1000;

import java.math.BigDecimal;

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
import org.bukkit.inventory.ItemStack;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

import net.Cyberhub.tkdkid1000.utils.ItemBuilder;
import net.ess3.api.MaxMoneyException;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.node.Node;
import net.md_5.bungee.api.ChatColor;

public class GadgetShop implements Listener, CommandExecutor {

	private LuckPerms luckperms;
	private CyberhubLobby cyberhublobby;

	public GadgetShop(CyberhubLobby cyberhublobby, LuckPerms luckperms) {
		this.cyberhublobby = cyberhublobby;
		this.luckperms = luckperms;
	}
	
	public void setup() {
		cyberhublobby.getServer().getPluginManager().registerEvents(this, cyberhublobby);
		cyberhublobby.getCommand("gadgetshop").setExecutor(this);
	}
	
	Inventory gui = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Gadget Shop");
	Essentials ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) throws UserDoesNotExistException, NoLoanPermittedException {
		if (!(event.getWhoClicked() instanceof Player)) return;
		if (!event.getInventory().getName().equals(gui.getName())) return;
		Player player = (Player) event.getWhoClicked();
		if (event.getCurrentItem() == null) return;
		ItemStack clicked = event.getCurrentItem();
		event.setCancelled(true);
		if (clicked.getType() == Material.GLASS) return;
		if (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Grappling Hook")
				&& clicked.getType() == Material.FISHING_ROD) {
			buyPermission(player, 1000, "cyberhublobby.gadget.fishingrod", 
					ChatColor.RED + "You already own that gadget!",
					ChatColor.RED + "You cannot afford that item! (1000 coins)", 
					ChatColor.RED + "Your purchase failed. Refunding  you.", 
					ChatColor.GREEN + "Successfully bought grappling hook!");
		} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Firework Gun")
				&& clicked.getType() == Material.BLAZE_ROD) {
			buyPermission(player, 2000, "cyberhublobby.gadget.fireworkgun", 
					ChatColor.RED + "You already own that gadget!",
					ChatColor.RED + "You cannot afford that item! (2000 coins)", 
					ChatColor.RED + "Your purchase failed. Refunding  you.", 
					ChatColor.GREEN + "Successfully bought firework gun!");
		} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Player Saddle")
				&& clicked.getType() == Material.SADDLE) {
			buyPermission(player, 2000, "cyberhublobby.gadget.playersaddle", 
					ChatColor.RED + "You already own that gadget!",
					ChatColor.RED + "You cannot afford that item! (2000 coins)", 
					ChatColor.RED + "Your purchase failed. Refunding  you.", 
					ChatColor.GREEN + "Successfully bought player saddle!");
		} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Back to Gadgets")
				&& clicked.getType() == Material.REDSTONE) {
			player.performCommand("gadget");
		}
	}
	
	public void createGui(Player player) {
		gui.clear();
		gui.setItem(9, new ItemBuilder(Material.BLAZE_ROD, 1)
				.setName(ChatColor.RED + "Firework Gun")
				.build());
		gui.setItem(11, new ItemBuilder(Material.FISHING_ROD, 1)
				.setName(ChatColor.RED + "Grappling Hook")
				.setUnbreakable(true)
				.build());
		gui.setItem(13, new ItemBuilder(Material.SADDLE, 1)
				.setName(ChatColor.RED + "Player Saddle")
				.build());
		gui.setItem(49, new ItemBuilder(Material.REDSTONE, 1)
				.setName(ChatColor.RED + "Back to Gadgets")
				.build());
		for (int x=0; x<gui.getSize(); x++) {
			if (gui.getItem(x) == null) {
				gui.setItem(x, new ItemBuilder(Material.STAINED_GLASS_PANE, 1)
						.setName(" ")
						.build());
			}
		}
		player.openInventory(gui);
	}
	
	public void buyPermission(Player player, int price, String permission, String owned, String cannotafford, String failed, String success) {
		net.luckperms.api.model.user.User u = luckperms.getPlayerAdapter(Player.class).getUser(player);
		if (u.getNodes().contains(Node.builder(permission).build())) {
			player.sendMessage(owned);
			return;
		}
		if (ess.getUser(player).getMoney().intValue() >= price) {
			ess.getUser(player).takeMoney(new BigDecimal(price));
			Node node = Node.builder(permission).build();
			this.luckperms.getUserManager().modifyUser(player.getUniqueId(), (net.luckperms.api.model.user.User user) -> {
				DataMutateResult result = user.data().add(node);
				if (result.wasSuccessful()) {
					player.sendMessage(success);
				} else {
					player.sendMessage(failed);
					try {
						ess.getUser(player).giveMoney(new BigDecimal(price));
					} catch (MaxMoneyException e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			player.sendMessage(cannotafford);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			createGui(player);
		}
		return true;
	}
}
