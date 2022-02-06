package io.github.bycubed7.claimedcubes.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import io.github.bycubed7.claimedcubes.plot.Plot;
import io.github.bycubed7.corecubes.managers.Debug;

public class DataManager {
	static String filePath = "plugins/ClaimedCubes/claims.data";

	static HashMap<UUID, Plot> plots;

	public static void set(UUID id, Plot plot) {
		plots.put(id, plot);
		save(); // <-- May cause lag
	}

	public static void remove(UUID id) {
		plots.remove(id);
		save(); // <-- May cause lag
	}

	public static Plot get(UUID id) {
		return plots.get(id);
	}

	// Con

	public DataManager() {
		plots = new HashMap<UUID, Plot>();
	}

	public DataManager(HashMap<UUID, Plot> data) {
		plots = data;
	}

	// Saving and Loading

	public static boolean save() {
		try {
			BukkitObjectOutputStream out = new BukkitObjectOutputStream(
					new GZIPOutputStream(new FileOutputStream(filePath)));

			out.writeObject(plots);
			out.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static HashMap<UUID, Plot> load() {

		// If the file doesnt exist, create it!
		if (!new File(filePath).exists()) {
			Debug.log("Creating new data file!");
			save();
		}

		try {
			BukkitObjectInputStream in = new BukkitObjectInputStream(
					new GZIPInputStream(new FileInputStream(filePath)));
			Object data = in.readObject();
			if (data instanceof HashMap)
				plots = (HashMap<UUID, Plot>) data;
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			save();
			load(); // <-- Possible Bug: MAY CAUSE INFINITE LOOP
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return plots;
	}
}
