package pl.agh.edu.iosr.cassandra.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.agh.edu.iosr.cassandra.db.connection.DBConnection;
import pl.agh.edu.iosr.cassandra.entities.QueryResultsRow;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;

public class DBHandler {
	private static final String QUERIES_TABLE = "queries";
	private static final String RESULTS_TABLE = "results";
	private int idCounter;
	private PreparedStatement insertQueryStmt;
	private PreparedStatement fetchResultsStmt;

	public DBHandler() {
		idCounter = updateIdCounter();
		fetchResultsStmt = DBConnection.getSession().prepare(
				"SELECT * FROM " + RESULTS_TABLE
				+ " WHERE query_id=?;");
		insertQueryStmt = DBConnection.getSession().prepare(
				"INSERT INTO " + QUERIES_TABLE
				+ "(id,query)"
				+ " VALUES(?,?);");
	}
	
	private int updateIdCounter() {
		Set<Integer> ids = getAllQueries().keySet();
		return (ids.size() == 0) ? 0 : Collections.max(ids) + 1;
	}
	
	public void insertQuery(String query) {	
		DBConnection.getSession().execute(insertQueryStmt.bind(idCounter, query));
		idCounter++;
	}
	
	public List<QueryResultsRow> getResultsForId(int id) {		
		List<QueryResultsRow> results = new ArrayList<QueryResultsRow>();
		for (Row row: DBConnection.getSession().execute(fetchResultsStmt.bind(id))) {
			results.add(new QueryResultsRow(id, row.getString("group_by"), 
										row.getString("result")));
		}
		
		return results;
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
