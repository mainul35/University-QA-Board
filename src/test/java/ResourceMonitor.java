import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Random;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

public class ResourceMonitor {

	public static void printUsage(Runtime runtime) {
		long total, free, used;
		int mb = 1024 * 1024;

		total = runtime.totalMemory();
		free = runtime.freeMemory();
		used = total - free;
		System.out.println("\nTotal Memory: " + total / mb + "MB");
		System.out.println(" Memory Used: " + used / mb + "MB");
		System.out.println(" Memory Free: " + free / mb + "MB");
		System.out.println("Percent Used: " + ((double) used / (double) total) * 100 + "%");
		System.out.println("Percent Free: " + ((double) free / (double) total) * 100 + "%");
	}

	public static void log(Object message) {
		System.out.println(message);
	}

	public static int calcCPU(long cpuStartTime, long elapsedStartTime, int cpuCount) {
		long end = System.nanoTime();
		long totalAvailCPUTime = cpuCount * (end - elapsedStartTime);
		long totalUsedCPUTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - cpuStartTime;
		// log("Total CPU Time:" + totalUsedCPUTime + " ns.");
		// log("Total Avail CPU Time:" + totalAvailCPUTime + " ns.");
		float per = (((float) totalUsedCPUTime * 100) / (float) totalAvailCPUTime);
		log(per);
		return (int) per;
	}

	static boolean isPrime(int n) {
		// 2 is the smallest prime
		if (n <= 2) {
			return n == 2;
		}
		// even numbers other than 2 are not prime
		if (n % 2 == 0) {
			return false;
		}
		// check odd divisors from 3
		// to the square root of n
		for (int i = 3, end = (int) Math.sqrt(n); i <= end; i += 2) {
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}
	
	public static double getProcessCpuLoad() throws Exception {

	    MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
	    ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
	    AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });

	    if (list.isEmpty())     return Double.NaN;

	    Attribute att = (Attribute)list.get(0);
	    Double value  = (Double)att.getValue();

	    // usually takes a couple of seconds before we get real values
	    if (value == -1.0)      return Double.NaN;
	    // returns a percentage value with 1 decimal point precision
	    return ((int)(value * 1000) / 10.0);
	}

	public static void main(String[] args) {
		int mb = 1024 * 1024;
		int gb = 1024 * 1024 * 1024;
		/* PHYSICAL MEMORY USAGE */
		System.out.println("\n**** Sizes in Mega Bytes ****\n");
		com.sun.management.OperatingSystemMXBean operatingSystemMXBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory
				.getOperatingSystemMXBean();
		// RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		// operatingSystemMXBean = (com.sun.management.OperatingSystemMXBean)
		// ManagementFactory.getOperatingSystemMXBean();
		com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean) java.lang.management.ManagementFactory
				.getOperatingSystemMXBean();
		long physicalMemorySize = os.getTotalPhysicalMemorySize();
		System.out.println("PHYSICAL MEMORY DETAILS \n");
		System.out.println("total physical memory : " + physicalMemorySize / mb + "MB ");
		long physicalfreeMemorySize = os.getFreePhysicalMemorySize();
		System.out.println("total free physical memory : " + physicalfreeMemorySize / mb + "MB");
		/* DISC SPACE DETAILS */
		File diskPartition = new File("C:");
		File diskPartition1 = new File("E:");
		File diskPartition2 = new File("F:");
		long totalCapacity = diskPartition.getTotalSpace() / gb;
		long totalCapacity1 = diskPartition1.getTotalSpace() / gb;
		double freePartitionSpace = diskPartition.getFreeSpace() / gb;
		double freePartitionSpace1 = diskPartition1.getFreeSpace() / gb;
		double freePartitionSpace2 = diskPartition2.getFreeSpace() / gb;
		double usablePatitionSpace = diskPartition.getUsableSpace() / gb;
		System.out.println("\n**** Sizes in Giga Bytes ****\n");
		System.out.println("DISC SPACE DETAILS \n");
		// System.out.println("Total C partition size : " + totalCapacity + "GB");
		// System.out.println("Usable Space : " + usablePatitionSpace + "GB");
		System.out.println("Free Space in drive C: : " + freePartitionSpace + "GB");
		System.out.println("Free Space in drive D:  : " + freePartitionSpace1 + "GB");
		System.out.println("Free Space in drive E: " + freePartitionSpace2 + "GB");
		if (freePartitionSpace <= totalCapacity % 10 || freePartitionSpace1 <= totalCapacity1 % 10) {
			System.out.println(" !!!alert!!!!");
		} else
			System.out.println("no alert");

		Runtime runtime;
		byte[] bytes;
		System.out.println("\n \n**MEMORY DETAILS  ** \n");
		// Print initial memory usage.
		runtime = Runtime.getRuntime();
		printUsage(runtime);

		// Allocate a 1 Megabyte and print memory usage
		bytes = new byte[1024 * 1024];
		printUsage(runtime);

		bytes = null;
		// Invoke garbage collector to reclaim the allocated memory.
		runtime.gc();

		// Wait 5 seconds to give garbage collector a chance to run
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}

		// Total memory will probably be the same as the second printUsage call,
		// but the free memory should be about 1 Megabyte larger if garbage
		// collection kicked in.
		printUsage(runtime);
		for (int i = 0; i < 30; i++) {
			long start = System.nanoTime();
			// log(start);
			// number of available processors;
			int cpuCount = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
			Random random = new Random(start);
			int seed = Math.abs(random.nextInt());
			log("\n \n CPU USAGE DETAILS \n\n");
			log("Starting Test with " + cpuCount + " CPUs and random number:" + seed);
			int primes = 10000;
			//
			long startCPUTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
			start = System.nanoTime();
			while (primes != 0) {
				if (isPrime(seed)) {
					primes--;
				}
				seed++;

			}
			float cpuPercent = calcCPU(startCPUTime, start, cpuCount);
			log("CPU USAGE : " + cpuPercent + " % ");

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}

		try {
			Thread.sleep(500);
		} // `enter code here`
		catch (Exception ignored) {
		}
	}
}