package io.github.bycubed7.claimedcubes.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.claimedcubes.managers.PlotManager;
import io.github.bycubed7.claimedcubes.plot.Plot;
import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.ActionFailed;
import io.github.bycubed7.corecubes.managers.Tell;

public class CommandKick extends Action {

	public CommandKick(JavaPlugin _plugin) {
		super("kick", _plugin);
		plugin.getCommand(name).setExecutor(this);
	}

	protected ActionFailed approved(Player player, String[] args) {

		if (args.length > 1)
			return ActionFailed.ARGUMENTLENGTH;

		// Does the kicked player exist?
		Player kickedPlayer = Bukkit.getPlayer(args[0]);
		if (kickedPlayer == null) {
			Tell.player(player, "Can't find the player to kick!");
			return ActionFailed.OTHER;
		}

		// Is the player an member of a plot?
		Plot plot = PlotManager.instance.findByAssociate(player.getUniqueId());
		if (plot == null) {
			Tell.player(player, "You don't have a plot to kick them from!");
			return ActionFailed.OTHER;
		}

		// Is the kicked player the owner
		if (kickedPlayer.getUniqueId().equals(plot.getOwnerId())) {
			Tell.player(player, "...You can't kick the owner.");
			return ActionFailed.OTHER;
		}

		// Is the kicked player in the plot
		if (!plot.associated(kickedPlayer.getUniqueId())) {
			Tell.player(player, "Can't kick someone who's not in your plot!");
		}

		// Does the player have permission to kick?
		if (player.getUniqueId().equals(plot.getOwnerId())) {
			Tell.player(player, "You don't have permission to do this!");
			return ActionFailed.OTHER;
		}

		return ActionFailed.NONE;
	}

	protected boolean execute(Player player, String[] args) {
		Player kickedPlayer = Bukkit.getPlayer(args[0]);

		PlotManager.instance.findByAssociate(player.getUniqueId()).removeMember(kickedPlayer.getUniqueId());

		Tell.player(player, "Kicked member!");
		return true;
	}
}
