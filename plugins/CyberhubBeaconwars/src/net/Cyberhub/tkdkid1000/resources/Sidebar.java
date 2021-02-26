package net.Cyberhub.tkdkid1000.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.UserDoesNotExistException;

import me.lucko.helper.Schedulers;
import me.lucko.helper.Services;
import me.lucko.helper.metadata.Metadata;
import me.lucko.helper.metadata.MetadataKey;
import me.lucko.helper.metadata.MetadataMap;
import me.lucko.helper.scoreboard.Scoreboard;
import me.lucko.helper.scoreboard.ScoreboardObjective;
import me.lucko.helper.scoreboard.ScoreboardProvider;
import net.Cyberhub.tkdkid1000.CyberhubBeaconwars;
import net.Cyberhub.tkdkid1000.utils.YamlConfig;
import net.md_5.bungee.api.ChatColor;

public class Sidebar {
	
	private YamlConfig playerdata;
	private Essentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
	
	public Sidebar(YamlConfig playerdata) {
		this.playerdata = playerdata;
	}
	
	public void subscribe() {
		MetadataKey<ScoreboardObjective> SCOREBOARD_KEY = MetadataKey.create("beaconwars", ScoreboardObjective.class);
		@SuppressWarnings("deprecation")
		BiConsumer<Player, ScoreboardObjective> updater = (p, obj) -> {
		    obj.setDisplayName("&6&lBeaconwars");
		    List<String> lines = new ArrayList<String>();
		    lines.add("&7" + new java.sql.Date(System.currentTimeMillis()).toString().replace("-", "/"));
		    lines.add("");
		    if (CyberhubBeaconwars.enabled && CyberhubBeaconwars.players.contains(p.getUniqueId())) {
		    	for (int x=0; x<8; x++) {
			    	boolean alive = (boolean) CyberhubBeaconwars.teamlist.get(x).get("beaconalive");
					if (alive) {
						lines.add(ChatColor.GOLD + CyberhubBeaconwars.colors.get(x) + ": " + ChatColor.GREEN + "Alive");
					} else {
						if (CyberhubBeaconwars.playerlist.get(x).size() == 0) {
							lines.add(ChatColor.GOLD + CyberhubBeaconwars.colors.get(x) + ": " + ChatColor.DARK_RED + "Shattered");
						} else {
							lines.add(ChatColor.GOLD + CyberhubBeaconwars.colors.get(x) + ": " + ChatColor.RED + CyberhubBeaconwars.playerlist.get(x).size());
						}
					}	
			    }
		    } else {
		    	lines.add(ChatColor.GOLD + "Rank: " + ChatColor.GRAY + ess.getUser(p).getGroup().substring(0, 1).toUpperCase() + ess.getUser(p).getGroup().substring(1));
		    	lines.add(ChatColor.GOLD + "Wins: " + ChatColor.GRAY + playerdata.getConfig().getInt("playerdata."+p.getUniqueId().toString()+".wins"));
		    	lines.add(ChatColor.GOLD + "Kills: " + ChatColor.GRAY + playerdata.getConfig().getInt("playerdata."+p.getUniqueId().toString()+".kills"));
		    	lines.add(ChatColor.GOLD + "Deaths: " + ChatColor.GRAY + playerdata.getConfig().getInt("playerdata."+p.getUniqueId().toString()+".deaths"));
		    	try {
					lines.add(ChatColor.GOLD + "Coins: " + ChatColor.GRAY + Economy.getMoney(p.getName()));
				} catch (UserDoesNotExistException e1) {
					e1.printStackTrace();
				}
		    }
		    obj.applyLines(
		    	lines	
		    );
		};

		Scoreboard sb = Services.load(ScoreboardProvider.class).getScoreboard();

		me.lucko.helper.Events.subscribe(PlayerJoinEvent.class)
		        .handler(e -> {
		            ScoreboardObjective obj = sb.createPlayerObjective(e.getPlayer(), "null", DisplaySlot.SIDEBAR);
		            Metadata.provideForPlayer(e.getPlayer()).put(SCOREBOARD_KEY, obj);
		            updater.accept(e.getPlayer(), obj);
		        });

		Schedulers.async().runRepeating(() -> {
		    for (Player player : Bukkit.getOnlinePlayers()) {
		        MetadataMap metadata = Metadata.provideForPlayer(player);
		        ScoreboardObjective obj = metadata.getOrNull(SCOREBOARD_KEY);
		        if (obj != null) {
		            updater.accept(player, obj);
		            updater.accept(player, obj);
		        }
		    }
		}, 3L, 3L);
	}
}
