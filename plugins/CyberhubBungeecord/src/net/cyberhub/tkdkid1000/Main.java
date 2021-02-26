package net.cyberhub.tkdkid1000;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Main extends Plugin {
	Configuration config;
	Plugin plugin = this;
    @Override
    public void onEnable() {
    	getProxy().getPluginManager().registerCommand(this, new CommandHub());
    	getProxy().getPluginManager().registerCommand(this, new CommandAdminHelp());
    	getProxy().getPluginManager().registerCommand(this, new CommandBackup(this));
    	getProxy().getPluginManager().registerCommand(this, new CommandFakeplayers());
    	getProxy().getPluginManager().registerListener(this, new CommandFakeplayers());
    	getProxy().getPluginManager().registerCommand(this, new CommandDiscord(this));
    	getProxy().getPluginManager().registerCommand(this, new CommandWebsite(this));
    	getProxy().getPluginManager().registerCommand(this, new CommandStore(this));
        getLogger().info("CyberhubBungeecord Successfully Loaded!");
        new Messages(this, getConfig()).start();
        getProxy().getPluginManager().registerListener(this, new ChatDelay(this, config.getInt("chatdelay")));
        getProxy().getScheduler().schedule(this, new Runnable() {
        	@Override
        	public void run() {
        		ProxyServer.getInstance().getScheduler().runAsync(plugin, 
        				new Runnable() {
        			@Override
        			public void run() {
        				try {
        					if (CMD.isLinux()) {
        						CMD.executeCommand("backup.sh", getDataFolder());
        						ProxyServer.getInstance().getLogger().info("Backing up...");
        					}
        					if (CMD.isWindows()) {
        						CMD.executeCommand("backup.bat", getDataFolder());
        						ProxyServer.getInstance().getLogger().info("Backing up...");
        					}
        				} catch (IOException e) {
        					ProxyServer.getInstance().getLogger().severe("The recent backup has failed.");
        					e.printStackTrace();
        				}
        			}
        		});
        	}
        }, 1, getConfig().getLong("backuptime", 30), TimeUnit.MINUTES);
    }
    private static Main instance;
    public static Main getInstance() {
    	return instance;
    }
    
    public Configuration getConfig() {
    	try {
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
			return config;
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
    }
}