package io.github.bycubed7.claimedcubes.commands;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.claimedcubes.managers.PlotManager;
import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.ActionFailed;
import io.github.bycubed7.corecubes.managers.Tell;

public class CommandUnclaim extends Action {

	public CommandUnclaim(JavaPlugin _plugin) {
		super("unclaim", _plugin);
		plugin.getCommand(name).setExecutor(this);
	}

	protected ActionFailed approved(Player player, String[] args) {

		// Get the plot in question
		Chunk chunk = player.getLocation().getChunk();

		// Does plot exist?
		if (!PlotManager.hasPlotByChunk(chunk)) {
			// This plot is not claimed!
			Tell.player(player, "This chunk is not claimed!");
			return ActionFailed.OTHER;
		}

		// Is the plot owned by the player?
		if (!PlotManager.findByChunk(chunk).associated(player.getUniqueId())) {
			// This plot is not claimed!
			Tell.player(player, "You don't own this claim!");
			return ActionFailed.OTHER;
		}

		return ActionFailed.NONE;
	}

	protected boolean execute(Player player, String[] args) {
		Chunk chunk = player.getLocation().getChunk();

		PlotManager.unclaim(chunk);

		Tell.player(player, "Unclaimed the chunk!");
		return true;
	}
}
