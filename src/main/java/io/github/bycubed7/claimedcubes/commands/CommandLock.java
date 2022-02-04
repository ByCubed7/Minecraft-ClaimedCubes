package io.github.bycubed7.claimedcubes.commands;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.claimedcubes.managers.PlotManager;
import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.ActionFailed;
import io.github.bycubed7.corecubes.managers.Tell;

public class CommandLock extends Action {

	public CommandLock(JavaPlugin _plugin) {
		super("lock", _plugin);
		plugin.getCommand(name).setExecutor(this);
	}

	protected ActionFailed approved(Player player, String[] args) {
		Chunk chunk = player.getLocation().getChunk();

		// Is this chunk already locked?
		if (PlotManager.locked(chunk)) {
			Tell.player(player, "This chunk is already locked!");
			return ActionFailed.OTHER;
		}

		return ActionFailed.NONE;
	}

	protected boolean execute(Player player, String[] args) {
		Chunk chunk = player.getLocation().getChunk();

		// Lock the chunk
		PlotManager.lock(chunk);

		Tell.player(player, "Locked this chunk!");
		return true;
	}
}
