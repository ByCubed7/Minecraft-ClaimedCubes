package io.github.bycubed7.claimedcubes.commands;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.claimedcubes.managers.PlotManager;
import io.github.bycubed7.claimedcubes.plot.Plot;
import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.ActionFailed;
import io.github.bycubed7.corecubes.managers.Tell;

public class CommandDissolve extends Action {

	public CommandDissolve(JavaPlugin _plugin) {
		super("dissolve", _plugin);
		plugin.getCommand(name).setExecutor(this);
	}

	protected ActionFailed approved(Player player, String[] args) {

		if (args.length > 1)
			return ActionFailed.ARGUMENTLENGTH;

		Plot plot = PlotManager.instance.findByAssociate(player.getUniqueId());

		// Is the player in a plot?
		if (plot == null) {
			Tell.player(player, "You are not in a plot!");
			return ActionFailed.OTHER;
		}

		// Does the player have permission?
		if (player.getUniqueId().equals(plot.getOwnerId())) {
			Tell.player(player, "You don't have permission to do this!");
			return ActionFailed.OTHER;
		}

		return ActionFailed.NONE;
	}

	protected boolean execute(Player player, String[] args) {
		PlotManager.instance.dissolve(player.getUniqueId());

		Tell.player(player, "Dissolved!");
		return true;
	}
}
