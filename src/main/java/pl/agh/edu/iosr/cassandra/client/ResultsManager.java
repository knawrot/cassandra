package pl.agh.edu.iosr.cassandra.client;

import java.util.Map;
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

	//TODO: do while loop over results
	public void operate() {
		System.out.println("Provide query id: ");
		printResults(scanner.nextInt());
	}

	private void printResults(int id) {
		int result = dbHandler.getResultsForId(id);
		System.out.println("Result for \""
				+ queries.get(id) + "\": \n"
				+ result);
	}
	

}
