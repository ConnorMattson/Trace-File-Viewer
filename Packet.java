public class Packet {
	public int sourceIP;
	public int destinationIP;
	public short size;
	public float time;
	
	public Packet(int sourceIP, int destinationIP, short size, float time) {
		this.sourceIP = sourceIP;
		this.destinationIP = destinationIP;
		this.size = size;
		this.time = time;
	}

	public Packet(String sourceIP, String destinationIP, short size, float time) {
		this.sourceIP = stringToIP(sourceIP);
		this.destinationIP = stringToIP(destinationIP);
		this.size = size;
		this.time = time;
	}

	public static int stringToIP(String stringIP) {
		int ip = 0;
		String[] stringList = stringIP.split("\\.");
		for (int i = 3; i >= 0; i--) ip += (Integer.parseInt(stringList[i]) << i*8);
		return ip;
	}

	public static String ipToString(int ip) {
		return String.format("%d.%d.%d.%d", ip&0xFF,  (ip>>8)&0xFF, (ip>>16)&0xFF, (ip>>24)&0xFF);

	}
}