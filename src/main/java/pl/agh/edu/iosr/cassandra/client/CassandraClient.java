package pl.agh.edu.iosr.cassandra.client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import pl.agh.edu.iosr.cassandra.db.dao.ProductsDAO;
import pl.agh.edu.iosr.cassandra.db.config.DBColumnDefinition;
import pl.agh.edu.iosr.cassandra.db.config.DBDatatype;




public class CassandraClient implements Runnable {
	private static final String TABLE_BY_ID = "products_by_id";
	private static final String TABLE_BY_NAME = "products_by_name";
	private static final String TABLE_BY_CATEGORY = "products_by_category";
	private static final String TABLE_BY_CUSTOMER_NAME = "products_by_customer_name";
	private static final String customerName = "Jan";
	private static final String date1 = "2015-05-10 11:25";
	private static final String date2 = "2015-05-11 14:30";

	public void run() {
		ProductsDAO productsDAO = new ProductsDAO();
		initializeDB(productsDAO);		
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		try {
			productsDAO.insertProduct(1, "Gruszka", "Owoce", customerName, 2,
					dateFormat.parse(date1));
			productsDAO.insertProduct(1, "Pomidor", "Warzywa", "Tomasz", 5,
					dateFormat.parse(date2));
			productsDAO.getSpecificResults(TABLE_BY_CUSTOMER_NAME, customerName);
		} catch (ParseException e) {
			System.out.println("Exception occured while parsing Date! ");
		}
		
	}

	private void initializeDB(ProductsDAO productsDAO) {
		List<DBColumnDefinition> columns = new LinkedList<DBColumnDefinition>();
		columns.add(new DBColumnDefinition("id", DBDatatype.INT, true, false));
		columns.add(new DBColumnDefinition("name", DBDatatype.VARCHAR));
		columns.add(new DBColumnDefinition("category", DBDatatype.VARCHAR));
		columns.add(new DBColumnDefinition("customer_name", DBDatatype.VARCHAR));
		columns.add(new DBColumnDefinition("price", DBDatatype.INT));
		columns.add(new DBColumnDefinition("purchase_date", DBDatatype.TIMESTAMP));
		productsDAO.registerView(TABLE_BY_ID, columns);
		columns = new LinkedList<DBColumnDefinition>();
		columns.add(new DBColumnDefinition("id", DBDatatype.INT, false, true));
		columns.add(new DBColumnDefinition("name", DBDatatype.VARCHAR, true, false));
		columns.add(new DBColumnDefinition("category", DBDatatype.VARCHAR));
		columns.add(new DBColumnDefinition("customer_name", DBDatatype.VARCHAR));
		columns.add(new DBColumnDefinition("price", DBDatatype.INT));
		columns.add(new DBColumnDefinition("purchase_date", DBDatatype.TIMESTAMP));
		productsDAO.registerView(TABLE_BY_NAME, columns);
		columns = new LinkedList<DBColumnDefinition>();
		columns.add(new DBColumnDefinition("id", DBDatatype.INT, false, true));
		columns.add(new DBColumnDefinition("name", DBDatatype.VARCHAR));
		columns.add(new DBColumnDefinition("category", DBDatatype.VARCHAR, true, false));
		columns.add(new DBColumnDefinition("customer_name", DBDatatype.VARCHAR));
		columns.add(new DBColumnDefinition("price", DBDatatype.INT));
		columns.add(new DBColumnDefinition("purchase_date", DBDatatype.TIMESTAMP));
		productsDAO.registerView(TABLE_BY_CATEGORY, columns);
		columns = new LinkedList<DBColumnDefinition>();
		columns.add(new DBColumnDefinition("id", DBDatatype.INT, false, true));
		columns.add(new DBColumnDefinition("name", DBDatatype.VARCHAR));
		columns.add(new DBColumnDefinition("category", DBDatatype.VARCHAR));
		columns.add(new DBColumnDefinition("customer_name", DBDatatype.VARCHAR, true, 
											false));
		columns.add(new DBColumnDefinition("price", DBDatatype.INT));
		columns.add(new DBColumnDefinition("purchase_date", DBDatatype.TIMESTAMP));
		productsDAO.registerView(TABLE_BY_CUSTOMER_NAME, columns);
	}
	
}
