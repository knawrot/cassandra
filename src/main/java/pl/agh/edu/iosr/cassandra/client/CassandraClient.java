package pl.agh.edu.iosr.cassandra.client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pl.agh.edu.iosr.cassandra.db.DBHandler;
import pl.agh.edu.iosr.cassandra.db.config.DBColumnDefinition;
import pl.agh.edu.iosr.cassandra.db.config.DBDatatype;

import com.datastax.driver.core.Row;




public class CassandraClient {
	private static final String TABLE = "stormcf";
	private DBHandler productsDAO;

	public CassandraClient() {
		productsDAO = new DBHandler();
	}
	
	public void doActions() {
		initializeDB();		
	}

	private void initializeDB() {
		List<DBColumnDefinition> columns = new LinkedList<DBColumnDefinition>();
		columns.add(new DBColumnDefinition("word", DBDatatype.VARCHAR, true, false));
		columns.add(new DBColumnDefinition("count", DBDatatype.INT));
		productsDAO.deleteView(TABLE);
		productsDAO.registerView(TABLE, columns);	
	}
	
	public List<String> getSampleResults(String searchedValue) {
		List<String> results = new ArrayList<String>();
		
		for (Row row : productsDAO.getSpecificResults(TABLE, searchedValue)) {
			results.add(row.toString());
		}
		
		return results;
	}
}
