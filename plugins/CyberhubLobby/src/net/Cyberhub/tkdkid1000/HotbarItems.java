package net.Cyberhub.tkdkid1000;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class HotbarItems implements Listener {

	private CyberhubLobby cyberhublobby;

	public HotbarItems(CyberhubLobby cyberhublobby) {
		this.cyberhublobby = cyberhublobby;
	}
	
	public void setup() {
		cyberhublobby.getServer().getPluginManager().registerEvents(this, cyberhublobby);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getItem() == null) return;
		if (event.getItem().getType() == Material.DIAMOND
				&& event.getItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN  + "Donate")) {
			event.getPlayer().chat("/buy");
			event.getPlayer().spigot().sendMessage(new ComponentBuilder("Click to visit our store!")
					.color(ChatColor.GOLD)
					.event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://cyberhubnet.tebex.io/"))
					.create());
		}
	}
}
