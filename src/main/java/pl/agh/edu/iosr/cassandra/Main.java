package pl.agh.edu.iosr.cassandra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import pl.agh.edu.iosr.cassandra.client.QueriesManager;
import pl.agh.edu.iosr.cassandra.client.ResultsManager;
import pl.agh.edu.iosr.cassandra.db.DBHandler;
import pl.agh.edu.iosr.cassandra.db.connection.DBConnection;



public class Main {
	private static Map<Integer,String> queries;
	
	private static void runOnLocalhost() {
		try {
			String line;
			String command = "/home/hadoop/apache-storm-0.9.4/bin/storm"
				+ " jar"
				+ " /home/hadoop/kafkastorm-0.0.2-SNAPSHOT-jar-with-dependencies.jar"
				+ " pl.edu.agh.iosr.lambda.kafkastorm.KafkaStormTopology"
				+ "  -sql \"";
			for (Entry<Integer,String> entry : queries.entrySet()) {
				command += entry.getValue() + ";";
			}
			command += "\"";
			System.out.println(command);
			Process p = Runtime.getRuntime().exec(new String[]{
											"bash"
											,"-c"
											,command});
			BufferedReader bri = new BufferedReader
			        (new InputStreamReader(p.getInputStream()));
			BufferedReader bre = new BufferedReader
					(new InputStreamReader(p.getErrorStream()));
			while ((line = bri.readLine()) != null) {
				System.out.println(line);
			}
			bri.close();
			while ((line = bre.readLine()) != null) {
				System.out.println(line);
			}
			bre.close();
			p.waitFor();
			p.destroy();
			System.out.println("Components started successfully.");
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
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
		
		runOnLocalhost();
		
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
