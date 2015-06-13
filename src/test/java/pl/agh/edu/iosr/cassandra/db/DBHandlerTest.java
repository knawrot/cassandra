package pl.agh.edu.iosr.cassandra.db;

import java.util.Map;

import com.datastax.driver.core.Row;

import junit.framework.TestCase;
import pl.agh.edu.iosr.cassandra.db.connection.DBConnection;

public class DBHandlerTest extends TestCase {
	private static final String QUERIES_TABLE = "queries";
	private DBHandler dbHandler;

	protected void setUp() throws Exception {
		DBConnection.setUpConnection();
		dbHandler = new DBHandler();
	}
	
	public void shouldInsertQuery() {		
		String query = "SELECT count(Price) group by Name";
		dbHandler.insertQuery(query);
		
		boolean found = false;
		String stmt = "SELECT * FROM " + QUERIES_TABLE;
		for (Row row: DBConnection.getSession().execute(stmt + ";")) {
			if (row.getString("query").equals(stmt)) {
				found = true;
				break;
			}
		}
		
		assertTrue(found);
	}
	
	public void shouldGetAllQueries() {		
		String query1 = "SELECT timestamp FROM " + QUERIES_TABLE;
		String query2 = "SELECT name FROM " + QUERIES_TABLE;
		dbHandler.insertQuery(query1);
		dbHandler.insertQuery(query2);
		
		Map<Integer,String> map = dbHandler.getAllQueries();
		assertTrue(map.containsValue(query1));
		assertTrue(map.containsValue(query2));
	}

}
