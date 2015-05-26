package pl.agh.edu.iosr.cassandra.db;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;
import pl.agh.edu.iosr.cassandra.db.DBHandler;
import pl.agh.edu.iosr.cassandra.db.config.DBColumnDefinition;
import pl.agh.edu.iosr.cassandra.db.config.DBDatatype;
import pl.agh.edu.iosr.cassandra.db.connection.DBConnection;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

public class DBHandlerTest extends TestCase {
	private static final String TABLE_BY_CUSTOMER_NAME = "products_by_customer_name";
	private DBHandler productsDAO;

	protected void setUp() throws Exception {
		DBConnection.setUpConnection();
		productsDAO = new DBHandler();
	}

	public void shouldRegisterView() {
		List<DBColumnDefinition> columns = new LinkedList<DBColumnDefinition>();
		columns.add(new DBColumnDefinition("id", DBDatatype.INT, false, true));
		columns.add(new DBColumnDefinition("name", DBDatatype.VARCHAR));
		columns.add(new DBColumnDefinition("category", DBDatatype.VARCHAR));
		columns.add(new DBColumnDefinition("customer_name", DBDatatype.VARCHAR, true, 
											false));
		columns.add(new DBColumnDefinition("price", DBDatatype.INT));
		columns.add(new DBColumnDefinition("purchase_date", DBDatatype.TIMESTAMP));
		productsDAO.deleteView(TABLE_BY_CUSTOMER_NAME);
		productsDAO.registerView(TABLE_BY_CUSTOMER_NAME, columns);
		
		try {
			DBConnection.getSession().execute(
					"SELECT * FROM " + TABLE_BY_CUSTOMER_NAME + ";");
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	public void shouldInsertProduct() {
		List<DBColumnDefinition> columns = new LinkedList<DBColumnDefinition>();
		columns.add(new DBColumnDefinition("id", DBDatatype.INT, false, true));
		columns.add(new DBColumnDefinition("name", DBDatatype.VARCHAR));
		columns.add(new DBColumnDefinition("category", DBDatatype.VARCHAR));
		columns.add(new DBColumnDefinition("customer_name", DBDatatype.VARCHAR, true, 
											false));
		columns.add(new DBColumnDefinition("price", DBDatatype.INT));
		columns.add(new DBColumnDefinition("purchase_date", DBDatatype.TIMESTAMP));
		productsDAO.deleteView(TABLE_BY_CUSTOMER_NAME);
		productsDAO.registerView(TABLE_BY_CUSTOMER_NAME, columns);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		String date1 = "2015-05-10 11:25";
		ResultSet results = null;
		try {
			productsDAO.insertProduct(1, "Gruszka", "Owoce", "Jan", 2, 
										dateFormat.parse(date1));
			results = DBConnection.getSession().
					execute("SELECT * FROM " + TABLE_BY_CUSTOMER_NAME + ";");
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(results != null);
		assertTrue(results.all().size() > 0);
	}

	public void shouldGetSpecificResults() {
		List<DBColumnDefinition> columns = new LinkedList<DBColumnDefinition>();
		columns.add(new DBColumnDefinition("id", DBDatatype.INT, false, true));
		columns.add(new DBColumnDefinition("name", DBDatatype.VARCHAR));
		columns.add(new DBColumnDefinition("category", DBDatatype.VARCHAR));
		columns.add(new DBColumnDefinition("customer_name", DBDatatype.VARCHAR, true, 
											false));
		columns.add(new DBColumnDefinition("price", DBDatatype.INT));
		columns.add(new DBColumnDefinition("purchase_date", DBDatatype.TIMESTAMP));
		productsDAO.deleteView(TABLE_BY_CUSTOMER_NAME);
		productsDAO.registerView(TABLE_BY_CUSTOMER_NAME, columns);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		String date1 = "2015-05-10 11:25";
		List<Row> results = null;
		try {
			productsDAO.insertProduct(1, "Gruszka", "Owoce", "Jan", 2, 
										dateFormat.parse(date1));
			results = productsDAO.getSpecificResults(TABLE_BY_CUSTOMER_NAME, "Jan");
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(results != null);
		assertTrue(results.size() > 0);
	}

}
