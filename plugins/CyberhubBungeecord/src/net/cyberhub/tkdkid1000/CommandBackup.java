package net.cyberhub.tkdkid1000;

import java.io.IOException;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;

import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

public class CommandBackup extends Command {

	private Plugin plugin;

	public CommandBackup(Plugin plugin) {
		super("proxybackup", "cyberhubbungee.proxybackup");
		this.plugin = plugin;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "Starting a backup. Check the console for updates").create());
		backup();
	}
	
	public void backup() {
		ProxyServer.getInstance().getScheduler().runAsync(plugin, 
				new Runnable() {
			@Override
			public void run() {
				try {
					if (CMD.isLinux()) {
						CMD.executeCommand("backup.sh", plugin.getDataFolder());
						ProxyServer.getInstance().getLogger().info("Backing up...");
					}
					if (CMD.isWindows()) {
						CMD.executeCommand("backup.bat", plugin.getDataFolder());
						ProxyServer.getInstance().getLogger().info("Backing up...");
					}
				} catch (IOException e) {
					ProxyServer.getInstance().getLogger().severe("The recent backup has failed.");
					e.printStackTrace();
				}
			}
		});
	}

}
