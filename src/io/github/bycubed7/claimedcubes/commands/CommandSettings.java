package io.github.bycubed7.claimedcubes.commands;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.ActionFailed;

public class CommandSettings extends Action {

	public CommandSettings(JavaPlugin _plugin) {
		super("settings", _plugin);
		plugin.getCommand(name).setExecutor(this);
	}

	protected ActionFailed approved(Player player, String[] args) {
		return ActionFailed.NONE;
	}

	protected boolean execute(Player player, String[] args) {
		return true;
	}

}
