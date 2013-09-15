package uk.co.kicraft.wanted;

import java.util.HashMap;

public class ServiceRegister {

	public static HashMap<String, Object> services = new HashMap<String, Object>();

	public static void registerService(String serviceName, Object service) {
		services.put(serviceName, service);
	}

	public static Object getService(String serviceName) {
		return services.get(serviceName);
	}

}
