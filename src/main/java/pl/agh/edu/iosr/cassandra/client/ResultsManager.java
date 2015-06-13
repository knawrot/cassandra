package pl.agh.edu.iosr.cassandra.client;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import pl.agh.edu.iosr.cassandra.db.DBHandler;

public class ResultsManager implements Manager {
	private Scanner scanner;
	private DBHandler dbHandler;
	private Map<Integer,String> queries;
	
	public ResultsManager(Scanner scanner, DBHandler dbHandler,
							Map<Integer,String> queries) {
		this.scanner = scanner;
		this.queries = queries;
		this.dbHandler = dbHandler;
	}

	//TODO: return to providing queries
	public void operate() {
		int operation;
		boolean shouldExit = false;
		
		while (!shouldExit) {
			System.out.println("Choose one of the following options:\n"
					+ "1. Print results for given query id\n"
					+ "2. Exit program");
			operation = Integer.parseInt(scanner.nextLine());
			switch (operation) {
				case 1:
					System.out.println("Provide query id: ");
					printResults(Integer.parseInt(scanner.nextLine()));
					break;
				case 2:
					System.out.println("Exiting results manager...");
					shouldExit = true;
					break;
				default:
					System.out.println("Wrong option. "
										+ "Only (1) and (2) allowed!\n");
			}
		}
	}

	private void printResults(int id) {
		Map<String, String> result = dbHandler.getResultsForId(id);
		System.out.println("Result for \"" + queries.get(id) + "\"\n");
		System.out.println("GROUP  |  RESULT");
		for (Entry<String, String> entry : result.entrySet()) {
			System.out.println(entry.getKey() + " | " + entry.getValue());
		}
	}
	

}
