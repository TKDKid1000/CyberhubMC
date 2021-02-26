package net.cyberhub.tkdkid1000;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class CommandFakeplayers extends Command implements Listener {

	public static int players = 0;
	public static String motd = "                §6Cyberhub Network §4[§a1.8-1.16§4]§r§r                 ";
	public CommandFakeplayers() {
		super("fake", "cyberhubbungee.fakeplayers");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(new ComponentBuilder(ChatColor.BLUE + "Specify a command. /fake players <count> /fake motd <string>").create());
		} else {
			if (args[0].equalsIgnoreCase("motd") || args[0] == "motd") {
				if (args.length == 1) {
					sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', "The motd is: " + motd)).create());
				} else {
					String base = "                §6Cyberhub Network §4[§a1.8-1.16§4]§r§r                 ";
					motd = base+longarg(args, 1);
					sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', "Set the motd to: " + motd)).create());
				}
			}
			if (args[0].equalsIgnoreCase("players") || args[0] == "players") {
				if (args.length == 1) {
					sender.sendMessage(new ComponentBuilder("The players is: " + ChatColor.BLUE + players).create());
				} else {
					players = Integer.parseInt(args[1]);
					sender.sendMessage(new ComponentBuilder("Set the players to: " + ChatColor.BLUE + players).create());
				}
			}
		}
	}
	
	@EventHandler
	public void onPing(ProxyPingEvent event) {
		ServerPing ping = event.getResponse();
        ping.setPlayers(new ServerPing.Players(ping.getPlayers().getMax(), ping.getPlayers().getOnline() + players, ping.getPlayers().getSample()));
        final ServerPing.Protocol prot = new ServerPing.Protocol("", ping.getVersion().getProtocol());
        ping.setVersion(prot);
		ping.setDescriptionComponent(new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', motd)).getCurrentComponent());
		event.setResponse(ping);
	}
	
	public static String longarg(String[] args, int start) {
		StringBuilder sb = new StringBuilder();
		for (int x=start; x<args.length; x++) {
			sb.append(args[x]);
			sb.append(" ");
		}
		return sb.toString();
	}

}
