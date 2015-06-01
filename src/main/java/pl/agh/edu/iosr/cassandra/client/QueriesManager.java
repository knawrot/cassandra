package pl.agh.edu.iosr.cassandra.client;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import pl.agh.edu.iosr.cassandra.db.DBHandler;

public class QueriesManager implements Manager {
	private Map<Integer,String> queries;
	private boolean shouldRefresh;
	private Scanner scanner;
	private DBHandler dbHandler;
	
	public QueriesManager(Scanner scanner, DBHandler dbHandler) {
		this.scanner = scanner;
		this.dbHandler = dbHandler;
		queries = dbHandler.getAllQueries();
		shouldRefresh = true;
	}
	
	public void operate() {
		int operation;
		boolean shouldExit = false;
		
		while (!shouldExit) {
			System.out.println("Choose one of the following options:\n"
					+ "1. Print all available quries\n"
					+ "2. Add another query\n"
					+ "3. Proceed (i.e. exit this procedure)");
			operation = Integer.parseInt(scanner.nextLine());
			switch (operation) {
				case 1:
					System.out.println("List of all queries:");
					listQueries();
					break;
				case 2:
					System.out.println("Provide a query:");
					addQuery(scanner.nextLine());
					break;
				case 3:
					System.out.println("Exiting queries manager...");
					shouldExit = true;
					break;
				default:
					System.out.println("Wrong option. "
										+ "Only (1), (2) and (3) allowed!\n");
			}
		}
	}

	private void listQueries() {
		if (shouldRefresh) {
			queries = dbHandler.getAllQueries();
			shouldRefresh = false;
		}
		
		for (Entry<Integer,String> entry : queries.entrySet()) {
			System.out.println(entry.getKey() + ". " + entry.getValue());
		}
	}
	
	public String getQuery(int id) {
		return queries.get(id);
	}
	
	private void addQuery(String query) {
		dbHandler.insertQuery(query);
		shouldRefresh = true;
	}

}
