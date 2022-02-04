package io.github.bycubed7.claimedcubes.commands;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.claimedcubes.managers.RequestManager;
import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.ActionFailed;

public class CommandReload extends Action {

	public CommandReload(JavaPlugin _plugin) {
		super("reload", _plugin);
		plugin.getCommand(name).setExecutor(this);
	}

	protected ActionFailed approved(Player player, String[] args) {
		return ActionFailed.NONE;
	}

	protected boolean execute(Player player, String[] args) {
		RequestManager.clean();
		return true;
	}

}
