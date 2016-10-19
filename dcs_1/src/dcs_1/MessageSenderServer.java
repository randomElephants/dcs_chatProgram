package dcs_1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MessageSenderServer implements Runnable {
	
	private int port;
	private DatagramSocket serverSocket;
	private ArrayList<InetAddress> network;
	private Boolean exiting;
	
	public MessageSenderServer(int port) throws UnknownHostException, SocketException {
		this.port = port;
		this.network = new ArrayList<InetAddress>();
		this.network.add(InetAddress.getByName("localhost"));
		this.serverSocket = new DatagramSocket();			
		this.exiting = false;
	}

	@Override
	public void run() {	
		while (!exiting) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void sendMessage(InetAddress sendTo, String message) {
		byte[] sendData = new byte[1024];
		sendData = message.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, sendTo, port);
		try {
			serverSocket.send(sendPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}
	
	public void sayGoodbye() {
		
		System.out.println("Saying goodbye");
		
		for(InetAddress address : network) {
			this.sendMessage(address, "Bye!");
		}
		
		this.exiting = true;
		serverSocket.close();
		System.out.println("MessageSender now exiting.");
	}
}
