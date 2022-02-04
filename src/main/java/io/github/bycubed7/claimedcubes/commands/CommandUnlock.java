package io.github.bycubed7.claimedcubes.commands;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.claimedcubes.managers.PlotManager;
import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.ActionFailed;
import io.github.bycubed7.corecubes.managers.Tell;

public class CommandUnlock extends Action {

	public CommandUnlock(JavaPlugin _plugin) {
		super("unlock", _plugin);
		plugin.getCommand(name).setExecutor(this);
	}

	protected ActionFailed approved(Player player, String[] args) {
		Chunk chunk = player.getLocation().getChunk();

		// Is this chunk not locked?
		if (!PlotManager.locked(chunk)) {
			Tell.player(player, "This chunk is not locked!");
			return ActionFailed.OTHER;
		}

		return ActionFailed.NONE;
	}

	protected boolean execute(Player player, String[] args) {
		Chunk chunk = player.getLocation().getChunk();

		// unlock the chunk
		PlotManager.unlock(chunk);

		Tell.player(player, "Unlocked this chunk!");
		return true;
	}

}
