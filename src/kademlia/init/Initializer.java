package kademlia.init;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import kademlia.JKademliaNode;
import kademlia.dht.GetParameter;
import kademlia.dht.KademliaStorageEntry;
import kademlia.exceptions.ContentNotFoundException;
import kademlia.node.KademliaId;
import kademlia.simulations.DHTContentImpl;

public class Initializer {

    private JKademliaNode kad1;
	private JKademliaNode kad2;
	private DHTContentImpl c;
	private JKademliaNode kad3;

	public static void main(String[] args)
    {
        new Initializer();
    }
    
    public Initializer() {
    	initConnection();
    	UserInputForChat();
    }

	private void UserInputForChat() {
		System.out.println("\nNachricht an andere Nodes eingeben: \n");
		Scanner sc = new Scanner(System.in);
		while(sc.hasNextLine()) {
			putDataIntoNetwork(sc.nextLine());
			receiveDataFromNetwork();
		}
	}

	private void receiveDataFromNetwork() {
        System.out.println("Retrieving Content");
        GetParameter gp = new GetParameter(c.getKey(), DHTContentImpl.TYPE);
        gp.setOwnerId(c.getOwnerId());
        System.out.println("Get Parameter: " + gp + "\n");
        KademliaStorageEntry conte;
		try {
			conte = kad1.get(gp);
			System.out.println("Requesting Node: " + kad1.getOwnerId());
	        System.out.println("Content Found: " + new DHTContentImpl().fromSerializedForm(conte.getContent()));
	        System.out.println("Content Metadata: " + conte.getContentMetadata());
	        
	        System.out.println("\n");
	        
			conte = kad3.get(gp);
			System.out.println("Requesting Node: " + kad3.getOwnerId());
	        System.out.println("Content Found: " + new DHTContentImpl().fromSerializedForm(conte.getContent()));
	        System.out.println("Content Metadata: " + conte.getContentMetadata());
		} catch (NoSuchElementException | IOException | ContentNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newMessageFromNeighbors();
	}

	private void newMessageFromNeighbors() {
        System.out.println("\nReceiving new messages from Peers!");
        c = new DHTContentImpl(kad1.getOwnerId(), "Thanks for your new message!");
        try {
			kad1.put(c);
		} catch (IOException e) {
			e.printStackTrace();
		}
        System.out.println("Retrieving Content");
        GetParameter gp = new GetParameter(c.getKey(), DHTContentImpl.TYPE);
        gp.setOwnerId(c.getOwnerId());
        System.out.println("Get Parameter: " + gp + "\n");
        KademliaStorageEntry conte;
		try {
			conte = kad2.get(gp);
			System.out.println("Requesting Node: " + kad2.getOwnerId());
	        System.out.println("Content Found: " + new DHTContentImpl().fromSerializedForm(conte.getContent()));
	        System.out.println("Content Metadata: " + conte.getContentMetadata());
	        
	        System.out.println("\n");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		UserInputForChat();
	}		

	private void putDataIntoNetwork(String input) {
        System.out.println("Message for put: " + input);
        c = new DHTContentImpl(kad2.getOwnerId(), input);
        try {
			kad2.put(c);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initConnection() {
		try
        {
            kad1 = new JKademliaNode("Timo", new KademliaId("ASF45678947584567467"), 7574);
            System.out.println("Created Node Kad 1: " + kad1.getNode().getNodeId());

            kad2 = new JKademliaNode("David", new KademliaId("ASERTKJDHGVHERJHGFLK"), 7572);
            System.out.println("Created Node Kad 2: " + kad2.getNode().getNodeId());
            

            /* Connecting 2 to 1 */
            System.out.println("Connecting Kad 1 and Kad 2");
            kad1.bootstrap(kad2.getNode());

            kad3 = new JKademliaNode("Leopold", new KademliaId("ASERTKJDOLKMNBVFR45G"), 7783);
            System.out.println("\nCreated Node Kad 3: " + kad3.getNode().getNodeId());

            System.out.println("Connecting Kad 3 and Kad 2");
            kad3.bootstrap(kad2.getNode());

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
	}
}
