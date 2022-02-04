package io.github.bycubed7.claimedcubes.commands;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.claimedcubes.managers.DataManager;
import io.github.bycubed7.claimedcubes.managers.PlotManager;
import io.github.bycubed7.claimedcubes.plot.Plot;
import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.ActionFailed;
import io.github.bycubed7.corecubes.managers.Tell;

public class CommandLeave extends Action {

	public CommandLeave(JavaPlugin _plugin) {
		super("leave", _plugin);
		plugin.getCommand(name).setExecutor(this);
	}

	protected ActionFailed approved(Player player, String[] args) {

		Plot plot = PlotManager.findByAssociate(player.getUniqueId());

		// Is player part of a plot?
		if (plot == null) {
			Tell.player(player, "You are not part of a plot!");
			return ActionFailed.OTHER;
		}

		// Are they the owner of it?
		if (plot.getOwnerId().equals(player.getUniqueId())) {
			Tell.player(player, "You are the owner of the plot! Use /dissolve instead");
			return ActionFailed.OTHER;
		}

		return ActionFailed.NONE;
	}

	protected boolean execute(Player player, String[] args) {
		Plot plot = PlotManager.findByAssociate(player.getUniqueId());

		plot.removeMember(player.getUniqueId());
		DataManager.set(plot.getOwnerId(), plot);

		Tell.player(player, "You have left the plot!");

		return true;
	}
}
