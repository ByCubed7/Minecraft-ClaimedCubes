package io.github.bycubed7.claimedcubes.plot;

import io.github.bycubed7.corecubes.unit.Vector3Int;

public class Area {
	public Vector3Int high;
	public Vector3Int low;

	public Area() {
		high = new Vector3Int(0, 0, 0);
		low = new Vector3Int(0, 0, 0);
	}

	public Area(Vector3Int _high, Vector3Int _low) {
		high = _high;
		low = _low;
	}

}
