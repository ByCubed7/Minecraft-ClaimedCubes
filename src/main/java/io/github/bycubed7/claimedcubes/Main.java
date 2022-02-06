package io.github.bycubed7.claimedcubes;

import io.github.bycubed7.claimedcubes.commands.CommandAccept;
import io.github.bycubed7.claimedcubes.commands.CommandAdd;
import io.github.bycubed7.claimedcubes.commands.CommandBanish;
import io.github.bycubed7.claimedcubes.commands.CommandClaim;
import io.github.bycubed7.claimedcubes.commands.CommandDissolve;
import io.github.bycubed7.claimedcubes.commands.CommandKick;
import io.github.bycubed7.claimedcubes.commands.CommandLeave;
import io.github.bycubed7.claimedcubes.commands.CommandLock;
import io.github.bycubed7.claimedcubes.commands.CommandReload;
import io.github.bycubed7.claimedcubes.commands.CommandSettings;
import io.github.bycubed7.claimedcubes.commands.CommandUnbanish;
import io.github.bycubed7.claimedcubes.commands.CommandUnclaim;
import io.github.bycubed7.claimedcubes.commands.CommandUnlock;
import io.github.bycubed7.claimedcubes.listeners.AttackListener;
import io.github.bycubed7.claimedcubes.listeners.BlockListener;
import io.github.bycubed7.claimedcubes.listeners.InteractListener;
import io.github.bycubed7.claimedcubes.listeners.MovementListener;
import io.github.bycubed7.claimedcubes.managers.DataManager;
import io.github.bycubed7.claimedcubes.managers.PlotManager;
import io.github.bycubed7.claimedcubes.managers.RequestManager;
import io.github.bycubed7.corecubes.CubePlugin;

public class Main extends CubePlugin {

	@Override
	protected void onBoot() {
		banner.add("  _______     _              _______     __");
		banner.add(" / ___/ /__ _(_)_ _  ___ ___/ / ___/_ __/ /  ___ ___");
		banner.add("/ /__/ / _ `/ /  ' \\/ -_) _  / /__/ // / _ \\/ -_|_-<");
		banner.add("\\___/_/\\_,_/_/_/_/_/\\__/\\_,_/\\___/\\_,_/_.__/\\__/___/");

	}

	@Override
	protected void onManagers() {
		new PlotManager();
		new RequestManager();
		new DataManager();

		PlotManager.load(DataManager.load());
	}

	@Override
	protected void onListeners() {
		new BlockListener(this);
		new MovementListener(this);
		new AttackListener(this);
		new InteractListener(this);
	}

	@Override
	protected void onCommands() {
		new CommandClaim(this);
		new CommandUnclaim(this);

		new CommandLock(this);
		new CommandUnlock(this);

		new CommandAccept(this);
		new CommandAdd(this);
		new CommandDissolve(this);
		new CommandKick(this);
		new CommandLeave(this);

		new CommandBanish(this);
		new CommandUnbanish(this);

		new CommandReload(this);
		new CommandSettings(this);
	}

	@Override
	protected void onReady() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart() {
		// Called at the start and after /reload
	}

}
