package pl.agh.edu.iosr.cassandra;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import pl.agh.edu.iosr.cassandra.client.QueriesManager;
import pl.agh.edu.iosr.cassandra.client.ResultsManager;
import pl.agh.edu.iosr.cassandra.db.DBHandler;
import pl.agh.edu.iosr.cassandra.db.connection.DBConnection;
import pl.agh.edu.iosr.cassandra.exceptions.StormCommunicationException;
import pl.edu.agh.iosr.lambda.kafkastorm.KafkaStormInterface;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;


public class Main {
	private static Map<Integer,String> queries;
	
	private static void sendQueriesToStorm() throws StormCommunicationException {
		try {
			Registry registry = LocateRegistry.getRegistry("localhost", 1099);
			KafkaStormInterface stormRemote = (KafkaStormInterface) registry
												.lookup("KafkaStormTopology");
			for (Entry<Integer,String> entry : queries.entrySet()) {
				stormRemote.createTopology(null, null, "topologyFor" + entry.getKey(), null,
						null, null, null, entry.getKey() + " " + entry.getValue(),
						null);
			}
			Thread.sleep(5000);
		} catch (RemoteException | NotBoundException | AlreadyAliveException 
				| InvalidTopologyException | InterruptedException e) {
			throw new StormCommunicationException((e.getMessage() != null) ?
													e.getMessage()
													: e.toString());
		}
	}
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		queries	= new HashMap<Integer, String>();
		
		DBConnection.setUpConnection();
		DBHandler dbHandler = new DBHandler();
		
		System.out.println("Welcome to Lambda Client. "
				+ "Manage your queries first.\n");
		
		QueriesManager queriesManager = new QueriesManager(scanner, dbHandler);
		queriesManager.operate();
		
		System.out.println("Please, select queries you want to run on Storm. "
							+ "If ready, press (s).");
		
		boolean shouldQuit = false;
		int queryId;
		String text;
		while (!shouldQuit) {
			text = scanner.nextLine();
			try {
				queryId = new Integer(text);
				queries.put(queryId, queriesManager.getQuery(queryId));
			} catch (NumberFormatException e) {
				if (text.equals("s"))
					shouldQuit = true;
				else 
					System.out.println("Unrecocgnized option!");
			}
		}
		
		try {
			sendQueriesToStorm();
		} catch (StormCommunicationException e) {
			System.err.println(e.getMessage());
			System.exit(0);
		}
		
		System.out.println("List of queries with their ids:");
		System.out.println("ID  QUERY");
		for (Entry<Integer,String> entry : queries.entrySet()) {
			System.out.println(entry.getKey() + "  " + entry.getValue());
		}
		System.out.println("Now, you can fetch results using those ids.");
		
		ResultsManager resultsManager = new ResultsManager(scanner, 
															dbHandler,
															queries);
		resultsManager.operate();

		scanner.close();
		System.exit(0);
	}
}
