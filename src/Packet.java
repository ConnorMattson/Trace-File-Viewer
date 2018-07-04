public class Packet {
	public int sourceIP;
	public int destinationIP;
	public short size;
	public float time;
	
	/**
 	* Constructs a packet object 
 	*
 	* @param  sourceIP  An integer representing the IP address from which the packet was sent
 	* @param  destinationIP  An integer representing the IP address which the packet was sent to
 	* @param  size  The size of the packet in pytes
 	* @param  time  The timestamp given to the packet by the eavesdropping program
 	*/
	public Packet(int sourceIP, int destinationIP, short size, float time) {
		this.sourceIP = sourceIP;
		this.destinationIP = destinationIP;
		this.size = size;
		this.time = time;
	}

	/**
 	* Constructs a packet object 
 	*
 	* @param  sourceIP  The IP address from which the packet was sent
 	* @param  destinationIP  The IP address which the packet was sent to
 	* @param  size  The size of the packet in pytes
 	* @param  time  The timestamp given to the packet by the eavesdropping program
 	*/
	public Packet(String sourceIP, String destinationIP, short size, float time) {
		this.sourceIP = stringToIP(sourceIP);
		this.destinationIP = stringToIP(destinationIP);
		this.size = size;
		this.time = time;
	}

	/**
 	* Converts an IP address from a string to a list of bits stored in an integer
 	*
 	* @param  stringIP  The IP address to be converted
 	* @return 			an integer representing the IP address
 	*/
	public static int stringToIP(String stringIP) {
		int ip = 0;
		String[] stringList = stringIP.split("\\.");
		for (int i = 3; i >= 0; i--) ip += (Integer.parseInt(stringList[i]) << i*8);
		return ip;
	}

	/**
 	* Converts an IP address from a list of bits stored in an integer to a string 
 	*
 	* @param  ip  An integer representing the IP address to be converted
 	* @return 	  The IP address
 	*/
	public static String ipToString(int ip) {
		return String.format("%d.%d.%d.%d", ip&0xFF,  (ip>>8)&0xFF, (ip>>16)&0xFF, (ip>>24)&0xFF);
	}
}