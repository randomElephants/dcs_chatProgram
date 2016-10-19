package dcs_1;

import java.io.*;
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
	private static HashMap<String,String> peerNicknames;
	private static Boolean exiting = false;
	

	public static void main(String[] args) throws InterruptedException, UnknownHostException {
		System.out.println("Starting program...");
			
		if (!isInputValid(args)) {
			System.exit(1);
		}
		
		try {			
			myIP = getOwnIPAsCidr();			
		} catch (UnknownHostException e) {
			System.out.println("Error: finding own IP threw unknown host exception.");
			System.exit(0);
		}
		
		//TODO: decide if InetAddress is more useful that a string of the address?
		//It is!
		peerNicknames = new HashMap<String, String>();
		peerNicknames.put("127.0.0.1", "localhost");
		
	
		//Start the 2 servers and their threads
		MessageSenderServer sender = null;
		ReceiveMessageProcessServer receiver = null;

		try {
			sender = new MessageSenderServer(port);
			receiver = new ReceiveMessageProcessServer(port);
			
			
		} catch (SocketException e) {
			System.err.println("Unable to set up the chat servers. Program will exit.");
			System.err.println(e.getMessage());
			System.exit(1);
		}
		
		Thread senderThread = new Thread(sender);
		senderThread.start();
							
		Thread receiverThread = new Thread(receiver);
		receiverThread.start();
		
		Runtime.getRuntime().addShutdownHook(new ShutdownUtil(receiver, sender, senderThread, receiverThread));			
			
		System.out.println("My IP is: " + myIP);
		System.out.println("Port is: " +  port + " Name is: " + myNickname);
		System.out.println("Everything has worked!");
		
		System.out.println("You can start typing messages!");
		
		while (!exiting) {
					
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
			String sentence;
			try {
				sentence = inFromUser.readLine();
				
				if (sentence.equalsIgnoreCase("exit")) {
					exiting = true;
				} else {
					System.out.println("You typed: " + sentence);
					System.out.println("Message will be sent");
					
					InetAddress ip = InetAddress.getByName("localhost");
					sender.sendMessage(ip, sentence);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
		System.exit(0);
		
/*		ArrayList<String> peerIPs = findReachableHosts(myIP);
		
		int pos = ordinalIndexOf(myIP, ".", 2);
		System.out.println("3rd dot is at char " + pos);
		//System.out.println("Subnet is: " + myIP.substring())
		String address = myIP.substring(pos+1);
		System.out.println("Unique part is: " + address);*/
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
					ips.add(hostToTry);
				} else {
					System.out.println("Host " + hostToTry +" was not reachable.");
				}
			} catch (UnknownHostException e) {
				System.out.println("Unknown host exception for address: " + hostToTry);
			} catch (IOException e) {
				System.out.println("IO Exception while attempting to contact host " + hostToTry + "(Error: " + e.getMessage());
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
