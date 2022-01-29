package io.github.bycubed7.claimedcubes.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.claimedcubes.managers.PlotManager;
import io.github.bycubed7.claimedcubes.managers.RequestManager;
import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.ActionFailed;
import io.github.bycubed7.corecubes.managers.Tell;

public class CommandAccept extends Action {

	public CommandAccept(JavaPlugin _plugin) {
		super("accept", _plugin);
		plugin.getCommand(name).setExecutor(this);
	}

	protected ActionFailed approved(Player player, String[] args) {

		UUID requesterID = RequestManager.get(player.getUniqueId());

		// Has the player got any requests?
		if (requesterID == null) {
			Tell.player(player, "You havn't got any requests!");
			return ActionFailed.OTHER;
		}

		return ActionFailed.NONE;
	}

	protected boolean execute(Player player, String[] args) {

		UUID requesterID = RequestManager.get(player.getUniqueId());
		Player requester = Bukkit.getPlayer(requesterID);

		// Remove the player from any current plots?
		// PlotManager.instance.remove(player.getUniqueId());
		PlotManager.instance.merge(player.getUniqueId(), requesterID);

		// Add player to requesters plots member list
		PlotManager.instance.findByAssociate(requesterID).addMember(player.getUniqueId());

		Tell.player(requester, player.getDisplayName() + " accepted the request!");
		Tell.player(player, "Accepted " + requester.getDisplayName() + "'s request!");

		RequestManager.remove(player.getUniqueId());

		return true;
	}
}
