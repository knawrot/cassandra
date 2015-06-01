package pl.agh.edu.iosr.cassandra.db;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pl.agh.edu.iosr.cassandra.db.connection.DBConnection;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;

public class DBHandler {
	private static final String QUERIES_TABLE = "queries";
	private static final String RESULTS_TABLE = "results";
	private int idCounter;

	public DBHandler() {
		idCounter = updateIdCounter();
	}
	
	private int updateIdCounter() {
		Set<Integer> ids = getAllQueries().keySet();
		return (ids.size() == 0) ? 0 : Collections.max(ids) + 1;
	}
	
	public void insertQuery(String query) {	
		PreparedStatement stmt = DBConnection.getSession().prepare(
				"INSERT INTO " + QUERIES_TABLE
				+ "(id,query)"
				+ " VALUES(?,?);");
		DBConnection.getSession().execute(stmt.bind(idCounter, query));
		
		stmt = DBConnection.getSession().prepare(
				"INSERT INTO " + RESULTS_TABLE
				+ "(id,result)"
				+ " VALUES(?,?);");
		DBConnection.getSession().execute(stmt.bind(idCounter++, 0));
	}
	
	public int getResultsForId(int id) {
		PreparedStatement stmt = DBConnection.getSession().prepare(
				"SELECT * FROM " + RESULTS_TABLE
				+ " WHERE id=?;");
		
		return DBConnection.getSession()
				.execute(stmt.bind(id))
				.all().get(0).getInt("result");
	}
	
	public Map<Integer,String> getAllQueries() {
		Map<Integer,String> results = new HashMap<Integer, String>();
		String stmt = "SELECT * FROM " + QUERIES_TABLE + ";";
		int maxIndex = 0;
		
		for (Row row: DBConnection.getSession().execute(stmt)) {
			maxIndex = row.getInt("id");
			if (maxIndex > idCounter)
				idCounter = maxIndex;
			results.put(maxIndex, row.getString("query"));
		}
		
		return results;
	}
	
}
