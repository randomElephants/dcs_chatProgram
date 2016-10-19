package dcs_1;

import java.util.ArrayList;

public class ShutdownUtil extends Thread {
	
	private ArrayList<Thread> threads;
	private MessageSenderServer sender;
	private ReceiveMessageProcessServer receiver;
	
	public ShutdownUtil (ReceiveMessageProcessServer receiver, MessageSenderServer sender, Thread senderThread, Thread receiverThread) {
		this.threads = new ArrayList<Thread>();
		this.threads.add(senderThread);
		this.threads.add(receiverThread);
		this.sender = sender;
		this.receiver = receiver;
	}
	
	@Override
	public void run() {
		
		System.out.println("Program is shutting down resources...");

		this.sender.sayGoodbye();
		this.receiver.exit();
		
		for(Thread t : threads) {				
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Program will now exit.");
	}

}
