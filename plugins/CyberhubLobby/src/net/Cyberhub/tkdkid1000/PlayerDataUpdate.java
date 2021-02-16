package net.Cyberhub.tkdkid1000;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.Cyberhub.tkdkid1000.utils.YamlConfig;

public class PlayerDataUpdate {

	private FileConfiguration playerdata;
	private YamlConfig playerdataConfig;
	public PlayerDataUpdate(CyberhubLobby cyberhubLobby) {
		this.playerdata = cyberhubLobby.playerdata.getConfig();
		this.playerdataConfig = cyberhubLobby.playerdata;
	}
	
	public void updatePlayer(Player player) {
		String uuid = player.getUniqueId().toString();
		if (playerdata.contains(uuid)) {
			playerdata.set(uuid+".joincount", playerdata.getInt(uuid+".joincount")+1);
			if (!playerdata.getStringList(uuid+".ips").contains(player.getAddress().getAddress().toString())) {
				List<String> ips = playerdata.getStringList(uuid+".ips");
				ips.add(player.getAddress().getAddress().toString());
				playerdata.set(uuid+".ips", ips);
			}
			playerdataConfig.save();
		} else {
			playerdata.set(uuid+".joindate", new java.sql.Date(System.currentTimeMillis()).toString());
			playerdata.set(uuid+".joincount", 1);
			playerdata.set(uuid+".ips", new ArrayList<String>()
					.add(player.getAddress().getAddress().toString()));
		}
	}
}
