package io.github.bycubed7.claimedcubes.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.claimedcubes.managers.PlotManager;
import io.github.bycubed7.claimedcubes.plot.Plot;
import io.github.bycubed7.corecubes.managers.Tell;

public class MovementListener implements Listener {

	public MovementListener(JavaPlugin plugin) {
		Bukkit.getServer().getPluginManager().registerEvents((Listener) this, plugin);
	}

	@EventHandler(ignoreCancelled = true)
	public void onMove(PlayerMoveEvent event) {
		if (event.getTo() == null)
			return;
		if (playerMove(event.getFrom(), event.getTo(), event.getPlayer())) {
			if (event.getPlayer().isInsideVehicle())
				event.getPlayer().leaveVehicle();
			event.setCancelled(true);
		}

	}

	private boolean playerMove(Location fromLocation, Location toLocation, Player player) {
		Chunk from = fromLocation.getChunk();
		Chunk to = toLocation.getChunk();

		if (from.equals(to))
			return false;

		Plot plotFrom = PlotManager.findByChunk(from);
		Plot plotTo = PlotManager.findByChunk(to);

		if (plotFrom == plotTo)
			return false;

		// Entering a plot:
		if (PlotManager.claimed(to)) {

			if (plotTo.hasBan(player.getUniqueId())) {
				// You are banned >:(
				Tell.actionbar(player, "You are banned from this plot! D:");
				return true;
			}

			if (plotTo.associated(player.getUniqueId()))
				Tell.actionbar(player, "Entering " + plotTo.getOwnerName() + "'s Plot! (Member)");
			else
				Tell.actionbar(player, "Entering " + plotTo.getOwnerName() + "'s Plot! (Visitor)");
		}

		// Exiting a plot
		else if (PlotManager.claimed(from)) {
			Tell.actionbar(player, "You are exiting " + plotFrom.getOwnerName() + "'s Plot!");
		}

		return false;
	}
}
