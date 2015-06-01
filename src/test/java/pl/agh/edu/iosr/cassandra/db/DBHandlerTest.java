package pl.agh.edu.iosr.cassandra.db;

import java.util.Map;

import junit.framework.TestCase;
import pl.agh.edu.iosr.cassandra.db.connection.DBConnection;

public class DBHandlerTest extends TestCase {
	private static final String QUERIES_TABLE = "queries";
	private static final String RESULTS_TABLE = "results";
	private DBHandler dbHandler;

	protected void setUp() throws Exception {
		DBConnection.setUpConnection();
		dbHandler = new DBHandler();
	}
	
	public void shouldInsertQuery() {		
		String query = "SELECT * FROM queries";
		dbHandler.insertQuery(query);
		
		Map<Integer,String> map = dbHandler.getAllQueries();
		assertTrue(map.containsValue(query));
		//assertTrue(dbHandler.getResultsForId(map.get(key)));
	}

}
