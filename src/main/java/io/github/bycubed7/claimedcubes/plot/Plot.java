package io.github.bycubed7.claimedcubes.plot;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import io.github.bycubed7.corecubes.unit.Vector2Int;

@SuppressWarnings("unused")
public class Plot implements Serializable {
	private static final long serialVersionUID = 4015228406810175386L;

	private UUID ownerId = null;
	private Set<UUID> members = new HashSet<>();
	private Set<UUID> banned = new HashSet<>();

	EnumSet<PlotPermission> memberPermissions = EnumSet.allOf(PlotPermission.class);
	EnumSet<PlotPermission> visitorPermissions = EnumSet.noneOf(PlotPermission.class);

	// The name of the plot
	private String name = "Plot";
	private Set<Vector2Int> claims = new HashSet<>();

	private boolean locked = false;

	public Plot(UUID _ownerId) {
		ownerId = _ownerId;
	}

	// Members

	public boolean associated(UUID playerId) {

		if (ownerId == null)
			return playerId == null;

		if (ownerId.equals(playerId))
			return true;

		if (members.contains(playerId))
			return true;

		return false;
	}

	public void addMember(UUID playerId) {
		members.add(playerId);
	}

	public void removeMember(UUID playerId) {
		members.remove(playerId);
	}

	public boolean hasMember(UUID playerId) {
		return members.contains(playerId);
	}

	// Banned

	public void addBan(UUID playerId) {
		banned.add(playerId);
	}

	public void removeBan(UUID playerId) {
		banned.remove(playerId);
	}

	public boolean hasBan(UUID playerId) {
		return banned.contains(playerId);
	}

	// Claims

	public void addClaim(Vector2Int coords) {
		claims.add(coords);
	}

	public void addClaim(Chunk chunk) {
		addClaim(new Vector2Int(chunk.getX(), chunk.getZ()));
	}

	public void removeClaim(Vector2Int coords) {
		claims.remove(coords);
	}

	public void removeClaim(Chunk chunk) {
		removeClaim(new Vector2Int(chunk.getX(), chunk.getZ()));
	}

	// Permissions

	public boolean hasPermission(UUID playerId, PlotPermission permission) {
		if (playerId.equals(ownerId))
			return true;

		if (members.contains(playerId) && memberPermissions.contains(permission))
			return true;

		if (visitorPermissions.contains(permission))
			return true;

		return false;

	}

	// Gets

	public UUID getOwnerId() {
		return ownerId;
	}

	public Player getOwner() {
		return Bukkit.getPlayer(ownerId);
	}

	public String getOwnerName() {
		return getOwner().getName();
	}

	public Set<Vector2Int> getClaims() {
		return claims;
	}

	// Comparison

	@Override
	public boolean equals(Object o) {

		if (o == this)
			return true;

		if (!(o instanceof Plot))
			return false;

		Plot c = (Plot) o;

		return ownerId == c.ownerId;
	}

	public void transfer(UUID newOwnerId) {
		ownerId = newOwnerId;
	}
}
