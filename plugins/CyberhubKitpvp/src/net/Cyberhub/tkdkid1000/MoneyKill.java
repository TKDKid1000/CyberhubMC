package net.Cyberhub.tkdkid1000;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

import net.ess3.api.Economy;
import net.ess3.api.MaxMoneyException;
import net.md_5.bungee.api.ChatColor;

public class MoneyKill implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onKill(PlayerDeathEvent event) throws MaxMoneyException, NoLoanPermittedException, UserDoesNotExistException {
		Player player = event.getEntity();
		if (player.getKiller() != null) {
			if (player.getKiller() instanceof Player) {
				Player killer = player.getKiller();
				Economy.add(killer.getName(), 10);
				killer.sendMessage(ChatColor.GREEN + "You killed " + player.getName() + " for 10$!");
			}
		}
	}
}
