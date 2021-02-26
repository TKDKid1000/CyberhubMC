package net.Cyberhub.tkdkid1000;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import net.Cyberhub.tkdkid1000.utils.ItemBuilder;
import net.Cyberhub.tkdkid1000.utils.YamlConfig;
import net.luckperms.api.LuckPerms;
import net.md_5.bungee.api.ChatColor;

public class CyberhubLobby extends JavaPlugin implements Listener {
	
	private LuckPerms luckperms;
	public static HashMap<Player, Boolean> candbj = new HashMap<Player, Boolean>();
	private static CyberhubLobby instance;
	private PlayerDataUpdate pdu;
	HashMap<Player, Boolean> dbjcooldown = new HashMap<Player, Boolean>();
	YamlConfig playerdata = new YamlConfig(getDataFolder(), "playerdata");
	@Override
	public void onEnable() {
		getCommand("doublejump").setExecutor(new DoublejumpCommand());
		this.luckperms = getServer().getServicesManager().load(LuckPerms.class);
		new GadgetShop(this, this.luckperms).setup();
		saveDefaultConfig();
		playerdata.createConfig();
		getServer().getPluginManager().registerEvents(this, this);
		new GadgetGUI(this).setup();
		new ServerGui(this).setup();
		new HotbarItems(this).setup();
		new Trails(this).register();
	}
	@Override
	public void onDisable() {
	}
	
	public static CyberhubLobby getInstance() {
		return instance;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Location loc = p.getLocation();
		if (loc.getBlock().getTypeId() == 147) {
        	Location ploc = p.getLocation();
        	Vector dir = ploc.getDirection().normalize();
        	dir.multiply(3);
        	p.setVelocity(dir);
		}
		candbj.putIfAbsent(p, true);
		if (!candbj.get(p)) {
			return;
		}
		dbjcooldown.putIfAbsent(p, true);
		if (p.isOnGround()) {
			dbjcooldown.replace(p, true);
		}
		if (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) {
			return;
		} else {
			if (dbjcooldown.get(p) || p.hasPermission("cyberhublobby.doublejump")) {
				p.setAllowFlight(true);
				if (p.isFlying()) {
    	        	Location ploc = p.getLocation();
    	        	Vector dir = ploc.getDirection().normalize();
    	        	dir.multiply(2);
    	        	p.setVelocity(dir);
    				p.setFlying(false);
    				dbjcooldown.replace(p, false);
    			}
			} else {
				p.setAllowFlight(false);
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		pdu = new PlayerDataUpdate(this);
		pdu.updatePlayer(p);
		p.getInventory().setItem(4, new ItemBuilder(Material.COMPASS, 1)
				.setName(ChatColor.GREEN + "Server Switcher")
				.build());
		p.getInventory().setItem(2, new ItemBuilder(Material.REDSTONE, 1)
				.setName(ChatColor.GREEN + "Gadgets")
				.build());
		p.getInventory().setItem(6, new ItemBuilder(Material.DIAMOND, 1)
				.setName(ChatColor.GREEN + "Donate")
				.build());
	}
}