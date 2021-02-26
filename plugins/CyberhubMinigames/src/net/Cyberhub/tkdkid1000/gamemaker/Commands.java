package net.Cyberhub.tkdkid1000.gamemaker;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.Cyberhub.tkdkid1000.utils.ArrayCombine;
import net.Cyberhub.tkdkid1000.utils.Selector;

public class Commands implements CommandExecutor, TabExecutor {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
		if (cmd.getName().equalsIgnoreCase("exec")) {
			if (args.length == 0) {
				commands.add("as");
				commands.add("at");
				commands.add("if");
				commands.add("run");
				StringUtil.copyPartialMatches(args[0], commands, completions);
			} else {
				if (args[0].equalsIgnoreCase("as")) {
					commands.add("@a");
					commands.add("@e");
					commands.add("@p");
					commands.add("@r");
					commands.add("@s");
					for (Player p : Bukkit.getOnlinePlayers()) {
						commands.add(p.getName());
					}
				}
			}
		}
		return completions;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args[0].equalsIgnoreCase("as")) {
			Entity[] ents = Selector.getTargets(sender, args[1]);
			if (args[2].equalsIgnoreCase("run")) {
				for (Entity ent : ents) {
					if (ent instanceof Player) {
						((Player) ent).performCommand(ArrayCombine.combine(args, 3));
					}
				}
			}
		} else if (args[0].equalsIgnoreCase("at")) {
			Entity[] ents = Selector.getTargets(sender, args[1]);
			if (args[2].equalsIgnoreCase("run")) {
				for (Entity ent : ents) {
					Material type = ent.getLocation().getBlock().getType();
					short data = ent.getLocation().getBlock().getData();
					ent.getLocation().getBlock().setType(Material.COMMAND);
					CommandBlock block = (CommandBlock) ent.getLocation().getBlock();
					block.setCommand(ArrayCombine.combine(args, 3));
					ent.getLocation().getBlock().setType(type);
					ent.getLocation().getBlock().setData((byte) data);
				}
			}
		}
		return true;
	}
}
