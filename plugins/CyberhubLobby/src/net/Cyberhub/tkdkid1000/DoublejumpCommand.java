package net.Cyberhub.tkdkid1000;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class DoublejumpCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			CyberhubLobby.candbj.putIfAbsent(player, true);
			if (CyberhubLobby.candbj.get(player)) {
				CyberhubLobby.candbj.replace(player, false);
				player.sendMessage(ChatColor.GREEN + "Disabling double jump.");
				player.setAllowFlight(false);
			} else if (!CyberhubLobby.candbj.get(player)) {
				CyberhubLobby.candbj.replace(player, true);
				player.sendMessage(ChatColor.GREEN + "Enabling double jump.");
			}
		}
		return true;
	}
}