package io.github.bycubed7.claimedcubes;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

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
import io.github.bycubed7.claimedcubes.listeners.BlockListener;
import io.github.bycubed7.claimedcubes.listeners.MovementListener;
import io.github.bycubed7.claimedcubes.managers.DataManager;
import io.github.bycubed7.claimedcubes.managers.PlotManager;
import io.github.bycubed7.claimedcubes.managers.RequestManager;
import io.github.bycubed7.corecubes.managers.ConfigManager;
import io.github.bycubed7.corecubes.managers.Debug;

public class Main extends JavaPlugin {

	public static Main instance;

	@Override
	public void onEnable() {
		instance = this;
		new Debug(this);

		// Read config
		Debug.Log("Reading Config..");
		new ConfigManager(this, "default.yml");

		Debug.Log("Setting up Managers..");
		new PlotManager();
		new RequestManager(this);

		Debug.Log("Loading Data..");
		new DataManager();
		PlotManager.instance.load(DataManager.load());

		// Get intergration api / settings
		Debug.Log("Looking for Compatible Plugins..");

		Debug.Log("Setting up Event Listeners..");
		new BlockListener(this);
		new MovementListener(this);

		// Set up commands
		Debug.Log("Setting up Commands..");

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

		// Loading is done!
		Debug.Log(ChatColor.GREEN + "Done!");

		Debug.banner.add("  _______     _              _______     __");
		Debug.banner.add(" / ___/ /__ _(_)_ _  ___ ___/ / ___/_ __/ /  ___ ___");
		Debug.banner.add("/ /__/ / _ `/ /  ' \\/ -_) _  / /__/ // / _ \\/ -_|_-<");
		Debug.banner.add("\\___/_/\\_,_/_/_/_/_/\\__/\\_,_/\\___/\\_,_/_.__/\\__/___/");

		Debug.Banner();

		// Using a schedular so that it runs once ALL plugins are loaded.
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				onStart();
			}
		});
	}

	public void onStart() {
		// Called at the start and after /reload
	}

}
