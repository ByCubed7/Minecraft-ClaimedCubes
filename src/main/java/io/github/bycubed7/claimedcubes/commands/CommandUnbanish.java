package io.github.bycubed7.claimedcubes.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.claimedcubes.managers.PlotManager;
import io.github.bycubed7.claimedcubes.plot.Plot;
import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.ActionFailed;
import io.github.bycubed7.corecubes.managers.Tell;

public class CommandUnbanish extends Action {

	public CommandUnbanish(JavaPlugin _plugin) {
		super("unbanish", _plugin);
		plugin.getCommand(name).setExecutor(this);
	}

	protected ActionFailed approved(Player player, String[] args) {

		if (args.length > 1)
			return ActionFailed.ARGUMENTLENGTH;

		// Does the added player exist?
		Player unbanishedPlayer = Bukkit.getPlayer(args[0]);
		if (unbanishedPlayer == null) {
			Tell.player(player, "Can't find the player to unbanish!");
			return ActionFailed.OTHER;
		}

		Plot plot = PlotManager.instance.findByAssociate(player.getUniqueId());

		// Is the player in a plot?
		if (plot == null) {
			Tell.player(player, "You are not in a plot!");
			return ActionFailed.OTHER;
		}

		// Is the player banished?
		if (plot.hasBan(unbanishedPlayer.getUniqueId())) {
			Tell.player(player, "The player is not banished!");
			return ActionFailed.OTHER;
		}

		// Does the player have permission?
		if (!plot.getOwnerId().equals(player.getUniqueId())) {
			Tell.player(player, "You don't have permission to do this!");
			return ActionFailed.OTHER;
		}

		return ActionFailed.NONE;
	}

	protected boolean execute(Player player, String[] args) {
		Player unbanishedPlayer = Bukkit.getPlayer(args[0]);

		PlotManager.instance.findByAssociate(player.getUniqueId()).removeBan(unbanishedPlayer.getUniqueId());

		Tell.player(player, "Unbanished player!");
		return true;
	}

}
