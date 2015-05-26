package pl.agh.edu.iosr.cassandra.db;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.agh.edu.iosr.cassandra.db.config.DBColumnDefinition;
import pl.agh.edu.iosr.cassandra.db.connection.DBConnection;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

public class DBHandler {
	private Map<String,String> columnPrivKeysMap;

	public DBHandler() {
		columnPrivKeysMap = new HashMap<String, String>();
	}
	
	public void registerView(String columnName, List<DBColumnDefinition> columns) {		
		try {
			DBConnection.getSession().execute(parseCreateQuery(columnName, columns));
		} catch (Exception e) {
			System.err.println("View was not registered. Cause:\n" 
								+ e);
		}
	}
	
	/* every possible entity to be entered
	 * has to define it's own insertion method */
	public void insertProduct(int id, String name, String category,
							String customerName, int price, Date purchaseDate) {	
		java.sql.Date sqlPurchaseDate = new java.sql.Date(purchaseDate.getTime());
		PreparedStatement stmt;
		BatchStatement batchStmt = new BatchStatement();
		for (String viewId : columnPrivKeysMap.keySet()) {
			stmt = DBConnection.getSession().prepare(
					"INSERT INTO " + viewId
					+ "(id, name, category, customer_name, price, purchase_date)"
					+ " VALUES (?,?,?,?,?,?);");
			batchStmt.add(stmt.bind(id,name,category,customerName,
				price,sqlPurchaseDate));
		}

		DBConnection.getSession().execute(batchStmt);
	}
	
	public List<Row> getSpecificResults(String columnName, String specificValue) {
		PreparedStatement stmt = DBConnection.getSession().prepare(
				"SELECT * FROM " + columnName
				+ " WHERE " + columnPrivKeysMap.get(columnName) + "=?;");
		
		ResultSet results = DBConnection.getSession()
							.execute(stmt.bind(specificValue));
		
		return results.all();
	}
	
	private String parseCreateQuery(String columnName, 
									List<DBColumnDefinition> columns)
					throws Exception {
		List<String> primaryKeys = new LinkedList<String>();
		List<String> clusteringKeys = new LinkedList<String>();
		StringBuilder query = new StringBuilder("CREATE TABLE " + columnName + " (");
		for (DBColumnDefinition entry : columns) {
			query.append(entry.getName());
			query.append(" ");
			query.append(entry.getDataType());
			query.append(",");
			if (entry.isPrimaryKey()) primaryKeys.add(entry.getName());
			if (entry.isClusteringKey()) clusteringKeys.add(entry.getName());
		}
		query.append("PRIMARY KEY (");
		if (primaryKeys.size() > 0) {
			//TODO: decide whether handling composite partition key is possible
			if (primaryKeys.size() > 1) {
				throw new Exception("Only one primary key is allowed");
			}
			else
				query.append(primaryKeys.get(0));
		}
		else {
			/* setting default primary key for first column */
			query.append(columns.get(0).getName());
		}
		if (clusteringKeys.size() > 0) {
			for (String string : clusteringKeys) {
				query.append(",");
				query.append(string);
			}
		}
		query.append("));");
		
		columnPrivKeysMap.put(columnName, primaryKeys.get(0));
		return query.toString();
	}
	
	public void deleteView(String name) {
		DBConnection.getSession().execute("DROP TABLE IF EXISTS " + name + ";");
	}

}
