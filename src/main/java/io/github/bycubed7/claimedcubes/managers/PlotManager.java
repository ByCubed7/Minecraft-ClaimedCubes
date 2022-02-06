package io.github.bycubed7.claimedcubes.managers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.bukkit.Chunk;

import io.github.bycubed7.claimedcubes.plot.Plot;
import io.github.bycubed7.corecubes.unit.Vector2Int;

public class PlotManager {
	public static UUID lockedUUID = new UUID(0, 0);

	private static HashSet<Plot> plots;

	public PlotManager() {
		plots = new HashSet<Plot>();
	}

	public static void load(HashMap<UUID, Plot> data) {
		data.forEach((id, plot) -> plots.add(plot));
	}

	// Plots

	public static Plot create(UUID ownerId) {
		Plot newPlot = new Plot(ownerId);

		// Debug.log("Create plot with tied UUID: " + ownerId);

		plots.add(newPlot);
		DataManager.set(ownerId, newPlot);
		return newPlot;
	}

	// Find plots by:

	public static Plot findByOwner(UUID ownerId) {
		if (ownerId == null)
			return plots.stream().filter(plot -> plot.getOwnerId() == null).findFirst().orElse(null);
		return plots.stream().filter(plot -> ownerId.equals(plot.getOwnerId())).findFirst().orElse(null);
	}

	public static Plot findByAssociate(UUID associateId) {
		return plots.stream().filter(plot -> plot.associated(associateId)).findFirst().orElse(null);
	}

	public static Plot findByCoords(@Nonnull Vector2Int coords) {
		return plots.stream().filter(plot -> plot.hasClaim(coords)).findFirst().orElse(null);
	}

	// Has plots by:

	public static boolean hasPlotByOwner(UUID ownerId) {
		return findByOwner(ownerId) != null;
	}

	public static boolean hasPlotByAssociate(UUID associateId) {
		return findByAssociate(associateId) != null;
	}

	public static boolean hasPlotByCoords(@Nonnull Vector2Int coords) {
		return findByCoords(coords) != null;
	}

	//
	// Main data methods

	public static void merge(Plot from, Plot to) {
		// TO gains the claims from FROM
		from.getClaims().stream().forEach(claim -> to.addClaim(claim));
		DataManager.set(from.getOwnerId(), from);
		remove(from);
	}

	public static void remove(Plot plot) {
		plots.remove(plot);
		DataManager.remove(plot.getOwnerId());
	}

	public static void claim(UUID playerId, Vector2Int coords) {
		Plot playerPlot = findByAssociate(playerId);
		if (playerPlot == null)
			playerPlot = create(playerId);
		playerPlot.addClaim(coords);
		DataManager.set(playerId, playerPlot);
	}

	public static void unclaim(Vector2Int coords) {
		Plot plot = findByCoords(coords);
		plot.removeClaim(coords);
		DataManager.set(plot.getOwnerId(), plot);
	}

	public static void lock(Vector2Int coords) {
		Plot lockedPlot = findByOwner(lockedUUID);
		// if the manager does not have a lock plot
		if (lockedPlot == null)
			lockedPlot = create(lockedUUID);

		lockedPlot.addClaim(coords);

		// Save
		DataManager.set(lockedPlot.getOwnerId(), lockedPlot);
	}

	public static void unlock(Vector2Int coords) {
		Plot lockedPlot = getLockPlot();

		lockedPlot.removeClaim(coords);
		DataManager.set(lockedUUID, lockedPlot);
	}

	private static @Nonnull Plot getLockPlot() {
		Plot lockedPlot = findByOwner(lockedUUID);
		// if the manager does not have a lock plot
		if (lockedPlot == null)
			lockedPlot = create(lockedUUID);

		return lockedPlot;
	}

	public static Boolean claimed(Vector2Int coords) {
		Plot plot = findByCoords(coords);
		if (plot == null)
			return false;

		if (locked(coords))
			return false;

		return true;
	}

	public static Boolean locked(Vector2Int coords) {
		return getLockPlot().hasClaim(coords);
	}

	// Player Convenience methods

	public static void remove(UUID playerId) {
		remove(findByAssociate(playerId));
	}

	public static void merge(UUID fromPlayerId, UUID toPlayerId) {
		merge(findByAssociate(fromPlayerId), findByAssociate(toPlayerId));
	}

	// Chunk Convenience methods

	public static Plot findByChunk(Chunk chunk) {
		return findByCoords(coords(chunk));
	}

	public static boolean hasPlotByChunk(Chunk chunk) {
		return findByChunk(chunk) != null;
	}

	//

	public static void claim(UUID uniqueId, Chunk chunk) {
		claim(uniqueId, coords(chunk));
	}

	public static void unclaim(Chunk chunk) {
		unclaim(coords(chunk));
	}

	public static Boolean claimed(Chunk chunk) {
		return claimed(coords(chunk));
	}

	//

	public static void lock(Chunk chunk) {
		lock(coords(chunk));
	}

	public static void unlock(Chunk chunk) {
		unlock(coords(chunk));
	}

	public static Boolean locked(Chunk chunk) {
		return locked(coords(chunk));
	}

	//

	private static Vector2Int coords(Chunk chunk) {
		return new Vector2Int(chunk.getX(), chunk.getZ());
	}

}
