package net.Cyberhub.tkdkid1000.gamemaker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;

import net.Cyberhub.tkdkid1000.CyberhubMinigames;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class GameMakerCommand implements CommandExecutor, TabExecutor {

	
	private CyberhubMinigames cyberhubminigames;
	public GameMakerCommand(CyberhubMinigames cyberhubminigames) {
		this.cyberhubminigames = cyberhubminigames;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args[0].equalsIgnoreCase("list")) {
			sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Cyberhub Minigames");
    		for (Object game : new GameMaker(cyberhubminigames).getGames().keySet()) {
    			TextComponent text = new TextComponent(game.toString());
    			text.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/game join " + game.toString()));
    			sender.sendMessage(text.getText());
    		}
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();

        if (args.length == 1) {
        	commands.add("create");
        	commands.add("list");
        	commands.add("join");
        	commands.add("modify");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        } else if (args.length == 2) {
        	if (args[0].equalsIgnoreCase("join")) {
        		for (Object game : new GameMaker(cyberhubminigames).getGames().keySet()) {
        			commands.add(game.toString());
        		}
        	}
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        Collections.sort(completions);
        return completions;
	}	
}
