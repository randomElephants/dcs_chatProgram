package dcs_1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ReceiveMessageProcessServer implements Runnable {
	
	private int port;
	private DatagramSocket serverSocket;
	private boolean exiting;
	
	public ReceiveMessageProcessServer(int port) throws SocketException{
		this.port = port;
		this.exiting = false;
		this.serverSocket = new DatagramSocket(this.port);
	}
	
	//TODO: add citation
	@Override
	public void run() {
		try {
			while (!exiting) {
				byte[] receiveData = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				
				String sentence = new String(receivePacket.getData());
				System.out.println("Message received: " + sentence);
			}
			System.out.println("ReceiveMessageServer exiting");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("An IO error occurred in the UDP server");
			e.printStackTrace();
			return;
		} 
	}

	public synchronized void exit() {
		// TODO Auto-generated method stub
		this.exiting = true;
		serverSocket.close();
	}
}

/*else if (sentence.startsWith("HELO")) {
	///Handle peer probing for who's online
	System.out.println("Am I online?");
} else if (sentence.startsWith("File")) {
	///Handle request for file
	System.out.println("Send file!!");*/
//}
