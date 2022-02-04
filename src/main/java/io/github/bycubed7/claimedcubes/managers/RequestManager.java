package io.github.bycubed7.claimedcubes.managers;

import java.util.HashMap;
import java.util.UUID;

public class RequestManager {
	// public static Integer expireTime = 1200;

	// to | request
	private static HashMap<UUID, Request> requests;

	public RequestManager() {
		// Debug.Log("[Plot Manager] Starting up..");
		requests = new HashMap<UUID, Request>();
		// plugin = _plugin;
	}

	public static void addRequest(UUID to, UUID from) {
		Request request = new Request(to, from, System.currentTimeMillis());
		requests.put(to, request);
	}

	public static UUID get(UUID to) {
		Request get = requests.get(to);
		if (get == null)
			return null;

		// Is request timed out?
//		if (get.time + expireTime < System.currentTimeMillis()) {
//			requests.remove(to);
//			return null;
//		}

		return get.from;
	}

	public static void remove(UUID to) {
		requests.remove(to);
	}

	public static boolean has(UUID to) {
		return get(to) != null;
	}

	public static void clean() {
//		requests.forEach((to, request) -> {
//			if (request.time + expireTime < System.currentTimeMillis())
//				requests.remove(to);
//		});
	}
}
