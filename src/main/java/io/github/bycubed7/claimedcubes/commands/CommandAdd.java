package io.github.bycubed7.claimedcubes.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.claimedcubes.managers.PlotManager;
import io.github.bycubed7.claimedcubes.managers.RequestManager;
import io.github.bycubed7.claimedcubes.plot.Plot;
import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.ActionFailed;
import io.github.bycubed7.corecubes.managers.Tell;

public class CommandAdd extends Action {

	public CommandAdd(JavaPlugin _plugin) {
		super("add", _plugin);
		plugin.getCommand(name).setExecutor(this);
	}

	protected ActionFailed approved(Player player, String[] args) {

		if (args.length < 1) {
			return ActionFailed.ARGUMENTLENGTH;
		}

		// Does the added player exist?
		Player addedPlayer = Bukkit.getPlayer(args[0]);
		if (addedPlayer == null) {
			Tell.player(player, "Can't find the player to add!");
			return ActionFailed.OTHER;
		}

		if (addedPlayer.equals(player)) {
			Tell.player(player, "You can't add yourself!");
			return ActionFailed.OTHER;
		}

		Plot plot = PlotManager.findByAssociate(player.getUniqueId());

		// Is the added player in your plot already
		if (plot.associated(addedPlayer.getUniqueId())) {
			Tell.player(player, "Can't add someone who's already a member!");
			return ActionFailed.OTHER;
		}

		// Is the added player already a member of another plot?
		// if (PlotManager.instance.findByAssociate(player.getUniqueId()) != null) {
		// Tell.player(player, "The player is already part of another plot!");
		// return ActionFailed.OTHER;
		// }

		// Does the player have permission to add?
		if (!player.getUniqueId().equals(plot.getOwnerId())) {
			Tell.player(player, "You don't have permission to do this!");
			return ActionFailed.OTHER;
		}

		return ActionFailed.NONE;
	}

	protected boolean execute(Player player, String[] args) {
		Player addedPlayer = Bukkit.getPlayer(args[0]);

		// Send a request to the player
		PlotManager.findByAssociate(player.getUniqueId());
		RequestManager.addRequest(addedPlayer.getUniqueId(), player.getUniqueId());

		Tell.player(addedPlayer, player.getDisplayName() + " sent you a member request! use /accept to accept!");
		Tell.player(player, "Sent a request!");
		return true;
	}
}
