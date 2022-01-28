package io.github.bycubed7.claimedcubes.commands;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.claimedcubes.managers.PlotManager;
import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.ActionFailed;
import io.github.bycubed7.corecubes.managers.Tell;

public class CommandClaim extends Action {

	public CommandClaim(JavaPlugin _plugin) {
		super("claim", _plugin);
		plugin.getCommand(name).setExecutor(this);
	}

	protected ActionFailed approved(Player player, String[] args) {

		// Can the player claim any more plot?

		// Get the chunk in question
		Chunk chunk = player.getLocation().getChunk();

		// Is plot already claimed?
		if (PlotManager.instance.claimed(chunk)) {
			Tell.player(player, "This chunk is already claimed!");
			return ActionFailed.OTHER;
		}

		// Is plot locked?
		if (PlotManager.instance.locked(chunk)) {
			Tell.player(player, "This chunk is locked!");
			return ActionFailed.OTHER;
		}

		return ActionFailed.NONE;
	}

	protected boolean execute(Player player, String[] args) {
		Chunk chunk = player.getLocation().getChunk();

		PlotManager.instance.claim(player.getUniqueId(), chunk);

		Tell.player(player, "Claimed this chunk!");
		return true;
	}
}
