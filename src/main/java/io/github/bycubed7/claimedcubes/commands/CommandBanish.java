package io.github.bycubed7.claimedcubes.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.claimedcubes.managers.PlotManager;
import io.github.bycubed7.claimedcubes.plot.Plot;
import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.ActionFailed;
import io.github.bycubed7.corecubes.managers.Tell;

public class CommandBanish extends Action {

	public CommandBanish(JavaPlugin _plugin) {
		super("banish", _plugin);
		plugin.getCommand(name).setExecutor(this);
	}

	protected ActionFailed approved(Player player, String[] args) {

		if (args.length < 1)
			return ActionFailed.ARGUMENTLENGTH;

		// Does the added player exist?
		Player addedPlayer = Bukkit.getPlayer(args[0]);
		if (addedPlayer == null) {
			Tell.player(player, "Can't find the player to banish!");
			return ActionFailed.OTHER;
		}

		Plot plot = PlotManager.instance.findByAssociate(player.getUniqueId());

		// Is the player in a plot?
		if (plot == null) {
			Tell.player(player, "You are not in a plot!");
			return ActionFailed.OTHER;
		}

		// Is the banished player the owner?
		if (plot.getOwnerId().equals(addedPlayer.getUniqueId())) {
			Tell.player(player, "You can not banish the owner!");
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
		Player kickedPlayer = Bukkit.getPlayer(args[0]);

		PlotManager.instance.findByAssociate(player.getUniqueId()).addBan(kickedPlayer.getUniqueId());

		Tell.player(player, "Banished player!");
		return true;
	}

}
