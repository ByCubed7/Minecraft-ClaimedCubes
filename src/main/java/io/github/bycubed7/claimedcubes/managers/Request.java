package io.github.bycubed7.claimedcubes.managers;

import java.util.UUID;

public class Request {

	public UUID from, to;
	public Long time;

	public Request(UUID _from, UUID _to, Long _time) {
		from = _from;
		to = _to;
		time = _time;
	}
}
