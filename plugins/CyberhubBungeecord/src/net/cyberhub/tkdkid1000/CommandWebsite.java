package net.cyberhub.tkdkid1000;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class CommandWebsite extends Command {

	private Main main;

	public CommandWebsite(Main main) {
		super("website");
		this.main = main;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage(new ComponentBuilder(main.getConfig().getString("website")).color(ChatColor.DARK_PURPLE).create());
	}

}
