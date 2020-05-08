package ddc.support.util;

import java.lang.management.*;

public class CPULoadAverage {

	public static void main(String[] args) {
		OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
		System.out.println("getName:                " + os.getName());
		System.out.println("getArch:                " + os.getArch());
		System.out.println("getAvailableProcessors: " + os.getAvailableProcessors());
		System.out.println("getVersion:             " + os.getVersion());
		System.out.println("getSystemLoadAverage:   " + os.getSystemLoadAverage());

		// Make CPU spin for a bit

		for (int y = 0; y < Long.MAX_VALUE; y++) {
			if (y % 100000000 == 0)
				System.out.println("getSystemLoadAverage:   " + os.getSystemLoadAverage());
			double z = y * y;
		}

		System.out.println("getSystemLoadAverage:   " + os.getSystemLoadAverage());
	}
}
