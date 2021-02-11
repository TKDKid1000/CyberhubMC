package net.cyberhub.tkdkid1000;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandHub extends Command {
    public CommandHub() {
        super("hub");
    }
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new ComponentBuilder("This command can only be run by a player!").color(ChatColor.RED).create());
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (player.getServer().getInfo().getName().equalsIgnoreCase("lobby")) {
            player.sendMessage(new ComponentBuilder("You are already connected to this server!").color(ChatColor.RED).create());
            return;
        }
        else {
            ServerInfo target = ProxyServer.getInstance().getServerInfo("Lobby");
            player.connect(target);
        }
    }
}