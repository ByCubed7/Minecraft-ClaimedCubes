package io.github.bycubed7.claimedcubes.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.claimedcubes.managers.PlotManager;
import io.github.bycubed7.claimedcubes.plot.Plot;
import io.github.bycubed7.claimedcubes.plot.PlotPermission;
import io.github.bycubed7.corecubes.managers.Tell;

public class OnAttackListener implements Listener {

	public OnAttackListener(JavaPlugin plugin) {
		Bukkit.getServer().getPluginManager().registerEvents((Listener) this, plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onAttack(EntityDamageByEntityEvent event) {

		// Is the attacker a player?
		if (!(event.getDamager() instanceof Player))
			return;

		// Does a plot exist here?
		Chunk chunk = event.getEntity().getLocation().getChunk();

		if (!PlotManager.instance.hasPlot(chunk))
			return;

		Player attacker = (Player) event.getDamager();
		Plot plot = PlotManager.instance.getPlot(chunk);

		event.setCancelled(!canChangePlot(attacker, plot));
	}

	private Boolean canChangePlot(Player player, Plot plot) {

		// If plot is locked
		if (plot.associated(PlotManager.lockedUUID)) {

			// If the player has permission to build in locked chunks
			if (player.hasPermission("claimedcubes.lock.override"))
				return true;

			Tell.player(player, "You have no permission! This plot is locked!");
			return false;
		}

		// If chunk is claimed
		if (!plot.hasPermission(player.getUniqueId(), PlotPermission.PLACE)) {
			Tell.player(player, "You have no permission to do this!");
			return false;
		}

		return true;
	}
}
