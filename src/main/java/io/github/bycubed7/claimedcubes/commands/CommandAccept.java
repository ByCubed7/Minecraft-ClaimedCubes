package io.github.bycubed7.claimedcubes.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.claimedcubes.managers.DataManager;
import io.github.bycubed7.claimedcubes.managers.PlotManager;
import io.github.bycubed7.claimedcubes.managers.RequestManager;
import io.github.bycubed7.claimedcubes.plot.Plot;
import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.ActionFailed;
import io.github.bycubed7.corecubes.managers.Tell;

public class CommandAccept extends Action {

	public CommandAccept(JavaPlugin _plugin) {
		super("accept", _plugin);
		plugin.getCommand(name).setExecutor(this);
	}

	protected ActionFailed approved(Player player, String[] args) {

		// Has the player got any requests?
		if (!RequestManager.has(player.getUniqueId())) {
			Tell.player(player, "You haven't got any requests!");
			return ActionFailed.OTHER;
		}

		return ActionFailed.NONE;
	}

	protected boolean execute(Player player, String[] args) {

		UUID requesterID = RequestManager.get(player.getUniqueId());
		Plot requesterPlot = PlotManager.findByAssociate(requesterID); // <-- BUG RETURNS NULL

		// If the requesterPlot can't be found, create one!
		if (requesterPlot == null)
			PlotManager.create(requesterID);

		// Remove the player from any current plots?
		if (PlotManager.hasPlotByOwner(player.getUniqueId()))
			PlotManager.merge(PlotManager.findByOwner(player.getUniqueId()), requesterPlot);
		// PlotManager.remove(PlotManager.findByOwner(player.getUniqueId()));

		requesterPlot.addMember(player.getUniqueId());
		DataManager.set(requesterID, requesterPlot);

		Tell.player(Bukkit.getPlayer(requesterID), player.getDisplayName() + " accepted the request!");
		Tell.player(player, "Accepted " + requesterPlot.getOwnerName() + "'s request!");

		RequestManager.remove(player.getUniqueId());

		return true;
	}
}
