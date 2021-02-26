package net.cyberhub.tkdkid1000;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class CommandAdminHelp extends Command {

	public CommandAdminHelp() {
		super("adminhelp", "cyberhubbungeecord.adminhelp", new String[] {"ahelp", "admincmds", "admincommands"});
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		String msg = ChatColor.translateAlternateColorCodes('&', "&6To any admin that may need help. From TKDKid1000\n"
				+ "&6Ok. You might not know all the commands for ranks and stuff, &6this letter will help you.\n"
				+ "&6The first command is simply &4/lp&6. This is the base for all ranks &6commands.\n"
				+ "&6If you simply want a list of them, do &4/lp help&6 and you will get &6every command.\n"
				+ "&6These might not be useful to you tho, you just want to set a &6players rank. I'll get to that.\n"
				+ "&6The base command to set a users anything is &4/lp user <user>&6. &6This is all the user stuff.\n"
				+ "&6To List user commands do &4/lp user&6\n"
				+ "&6Now to add permissions (to players). Say you want to exit &6admin mode in beaconwars, you need a negative permission &4for that.\n"
				+ "&6To assign a permission to anyone, do &4/lp user <user> &4permission set <permission> <true|false>\n"
				+ "&6For example, to leave admin mode you can do &4/lp user &4<yourname> permission set cyberhubbeaconwars.override &4false&6\n"
				+ "&6That assigns the negative permission overriding your ranks &6positive one. To remove this simply do &4/lp user <yourname> &4permission unset cyberhubbeaconwars.override&6\n"
				+ "&6You can also assign positive permissions with by using true.\n"
				+ "&6Now to add ranks. This uses &4/lp user <user> parent&6.\n"
				+ "&6To set a players rank you can do &4/lp user <user> parent set &4<rankname>&6\n"
				+ "&6To get a players rank do &4/lp user <user> parent info&6\n"
				+ "&6Dm me on discord for more info and stuff.");
		sender.sendMessage(new ComponentBuilder(msg).create());
	}

	
}
