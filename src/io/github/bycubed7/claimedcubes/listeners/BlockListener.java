package io.github.bycubed7.claimedcubes.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.claimedcubes.managers.PlotManager;
import io.github.bycubed7.claimedcubes.plot.Plot;
import io.github.bycubed7.claimedcubes.plot.PlotPermission;
import io.github.bycubed7.corecubes.managers.Tell;

public class BlockListener implements Listener {

	public BlockListener(JavaPlugin plugin) {
		Bukkit.getServer().getPluginManager().registerEvents((Listener) this, plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		Chunk chunk = event.getBlock().getChunk();

		if (!PlotManager.instance.hasPlot(chunk))
			return;

		Plot plot = PlotManager.instance.getPlot(chunk);

		event.setCancelled(!canChangePlot(event.getPlayer(), plot));
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		Chunk chunk = event.getBlock().getChunk();

		if (!PlotManager.instance.hasPlot(chunk))
			return;

		Plot plot = PlotManager.instance.getPlot(chunk);

		event.setCancelled(!canChangePlot(event.getPlayer(), plot));
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

//	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
//	public void onBlockFromToEventMonitor(BlockFromToEvent event) {
//		//
//		PlotManager plotManager = PlotManager.instance;
//
//		Plot fromClaim = plotManager.getPlot(event.getBlock().getChunk());
//		Plot toClaim = plotManager.getPlot(event.getToBlock().getChunk());
//
//		if (toClaim == null)
//			return;
//
//		if (toClaim.isNot(fromClaim))
//			event.setCancelled(true);
//
//	}
}
