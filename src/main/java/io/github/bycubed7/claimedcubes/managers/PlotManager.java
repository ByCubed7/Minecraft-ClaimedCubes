package io.github.bycubed7.claimedcubes.managers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Chunk;

import io.github.bycubed7.claimedcubes.plot.Plot;
import io.github.bycubed7.corecubes.unit.Vector2Int;

public class PlotManager {
	public static PlotManager instance;

	public static UUID lockedUUID = null;// new UUID(0, 0);

	private HashMap<Vector2Int, Plot> plots;

	public PlotManager() {
		instance = this;
		plots = new HashMap<Vector2Int, Plot>();
	}

	public void load(HashMap<UUID, Plot> data) {
		data.forEach((i, p) -> p.getClaims().forEach(c -> plots.put(c, p)));
	}

	// Plots

	public Plot create(UUID ownerId) {
		// Debug.Log("[Plot Manager] Creating plot for player");
		Plot newPlot = new Plot(ownerId);
		return newPlot;
	}

	public void merge(UUID from, UUID to) {
		plots.forEach((a, b) -> {
			if (b.getOwnerId().equals(from))
				b.transfer(to);
			;
		});
	}

	public void remove(UUID from) {
		plots.forEach((a, b) -> {
			if (b.getOwnerId().equals(from))
				plots.remove(a, b);
		});
	}

	public Plot findByAssociate(UUID playerId) {
		// Returns a plot by searching through the hash
		HashSet<Plot> playerPlots = new HashSet<Plot>();
		plots.values().stream().filter(s -> s.associated(playerId)).forEach(s -> playerPlots.add(s));

		if (playerPlots.size() == 0)
			return null;

		// NOTE This is a dumb way of doing it
		return playerPlots.toArray(new Plot[0])[0];
	}

	// Claiming

	public Plot claim(UUID playerId, Vector2Int coords) {
		Plot playerPlot = findByAssociate(playerId);

		// if the player does not have a plot, create one!
		if (playerPlot == null)
			playerPlot = create(playerId);

		playerPlot.addClaim(coords);
		plots.put(coords, playerPlot);

		DataManager.set(playerId, playerPlot);

		return playerPlot;

	}

	public void claim(UUID uniqueId, Chunk chunk) {
		claim(uniqueId, coords(chunk));
	}

	public void unclaim(Vector2Int coords) {
		getPlot(coords).removeClaim(coords);
		plots.remove(coords);
	}

	public void unclaim(Chunk chunk) {
		unclaim(coords(chunk));
	}

	public void dissolve(UUID uniqueId) {
		plots.forEach((pos, plot) -> {
			if (plot.associated(uniqueId))
				unclaim(pos);
		});
	}

	public Boolean claimed(Vector2Int coords) {
		return plots.containsKey(coords) && !plots.get(coords).associated(lockedUUID);
	}

	public Boolean claimed(Chunk chunk) {
		return claimed(coords(chunk));
	}

	// Locking

	public void lock(Vector2Int coords) {
		Plot plot = findByAssociate(lockedUUID);
		// if the manager does not have a lock plot
		if (plot == null)
			plot = create(lockedUUID);

		plot.addClaim(coords);
		plots.put(coords, plot);

		// Save
		DataManager.set(lockedUUID, plot);
	}

	public void lock(Chunk chunk) {
		lock(coords(chunk));
	}

	public void unlock(Vector2Int coords) {
		Plot plot = findByAssociate(lockedUUID);
		// if the manager does not have a lock plot
		if (plot == null)
			plot = create(lockedUUID);

		plot.removeClaim(coords);
		plots.remove(coords, plot);

		DataManager.set(lockedUUID, plot);
	}

	public void unlock(Chunk chunk) {
		unlock(coords(chunk));
	}

	public Boolean locked(Vector2Int coords) {
		return plots.containsKey(coords) && plots.get(coords).associated(lockedUUID);
	}

	public Boolean locked(Chunk chunk) {
		return locked(coords(chunk));
	}

	// Get

	public Plot getPlot(Vector2Int coords) {
		return plots.get(coords);
	}

	public Plot getPlot(Chunk chunk) {
		return getPlot(coords(chunk));
	}

	// Has Contains

	public boolean hasPlot(Vector2Int coords) {
		// Debug.Log("[Plot Manager] Has plot was called");
		return plots.containsKey(coords);
	}

	public boolean hasPlot(Chunk chunk) {
		return hasPlot(coords(chunk));
	}

	static private Vector2Int coords(Chunk chunk) {
		return new Vector2Int(chunk.getX(), chunk.getZ());
	}

}
