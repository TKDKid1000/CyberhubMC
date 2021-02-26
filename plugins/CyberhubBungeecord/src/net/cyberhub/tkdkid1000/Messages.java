package net.cyberhub.tkdkid1000;

import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

public class Messages {

	private Configuration config;
	private Main main;

	public Messages(Main main, Configuration config) {
		this.main = main;
		this.config = config;
	}
	
	public void start() {
		Configuration messages = config.getSection("messages");
		for (String key : messages.getKeys()) {
			main.getProxy().getScheduler().schedule(main, new Runnable() {

				@Override
				public void run() {
					for (ProxiedPlayer player : main.getProxy().getPlayers()) {
						player.sendMessage(new ComponentBuilder(
								ChatColor.translateAlternateColorCodes('&', messages.getString(key+".message")))
								.event(new ClickEvent(ClickEvent.Action.OPEN_URL, messages.getString(key+".link")))
								.create());
					}
				}
				
			}, messages.getLong(key+".time"), messages.getLong(key+".time"), TimeUnit.MINUTES);
		}
	}
}
