package dcs_1;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatProgram {
	
	private static int port;
	private static String myIP;
	private static String myNickname;
	private static HashMap<InetAddress,String> peerNicknames;
	

	public static void main(String[] args) {
		System.out.println("Starting program...");
		
		Runtime.getRuntime().addShutdownHook(new DoShutdown());
		
		if (!isInputValid(args)) {
			System.exit(1);
		}
		
		try {			
			myIP = getOwnIPAsCidr();			
		} catch (UnknownHostException e) {
			System.out.println("Error: finding own IP threw unknown host exception.");
			System.exit(0);
		}
		
/*		ArrayList<String> peerIPs = findReachableHosts(myIP);
		
		int pos = ordinalIndexOf(myIP, ".", 2);
		System.out.println("3rd dot is at char " + pos);
		//System.out.println("Subnet is: " + myIP.substring())
		String address = myIP.substring(pos+1);
		System.out.println("Unique part is: " + address);*/
		
		System.out.println("My IP is: " + myIP);
		System.out.println("Port is: " +  port + " Name is: " + myNickname);
		System.out.println("Everything has worked!");
	}
	
	public static String getOwnIP() throws UnknownHostException {
		InetAddress IP = InetAddress.getLocalHost();
		return IP.getHostAddress();
	}
	
	public static String getOwnIPAsCidr() throws UnknownHostException {
		InetAddress IP = InetAddress.getLocalHost();
		int length = -1;
		try {
			NetworkInterface myInterface = NetworkInterface.getByInetAddress(IP);
			length = myInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength();
			System.out.println("NetworkPrefixLength is " + length);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("couldn't get network prefix length");
		}
		
		if (length > -1) {
			return (IP.getHostAddress()+"/"+length);
		} else {
			return IP.getHostAddress();
		}
		
	}
	
	public static Boolean isInputValid(String[] input) {
		Boolean isOK = true;
		
		if (input.length != 2) {
			System.out.println("Error: enter 2 arguments: the port to use and your nickname, in that order");
			isOK = false;
		} else {
			try {
				port = Integer.parseInt(input[0]);
				myNickname = input[1];
			} catch (NumberFormatException e) {
				System.out.println("Error: The value you entered for the port is not a valid number.");
				isOK = false;
			}
			
			if ((port <4000) ||(port>4010)) {
				System.out.println("Error: you need to enter a port between 4000 and 4010");
				isOK = false;
			}
		}
			
		return isOK;
	}
	
	//@see ADD REFERENCE HERE
	//TODO: reference
	//TODO: how to find peers??? This is only a shell!!
	public static ArrayList<String> findReachableHosts(String ownIP) {
		ArrayList<String> ips = new ArrayList<String>();
		int timeout = 2000;
		int myIPMinus10 = 10;
		int myIPPlus10 = 20;
		String networkInterface = "Get this from IP";
		
		for (int i=myIPMinus10; i<myIPPlus10; i++) {
			String hostToTry = networkInterface + i;
			try {
				InetAddress peer = InetAddress.getByName(hostToTry);
				
				if (peer.isReachable(timeout)) {
					System.out.println("Contacted host: "+peer);
				} 
			}
		}
		
		return ips;
	}
	
	//TODO: add @see reference
	//Starts with "zeroth" occurance - bad?
	public static int ordinalIndexOf(String haystack, String needle, int occurance) {
		int pos = haystack.indexOf(needle, 0);
		
		while ((occurance!=0) && (pos!=-1)) {
			occurance--;
			pos = haystack.indexOf(needle, pos+1);
		}
		
		return pos;
	}

}
