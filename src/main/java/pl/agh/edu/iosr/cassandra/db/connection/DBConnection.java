package pl.agh.edu.iosr.cassandra.db.connection;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class DBConnection {
	private static Cluster cluster;
	private static Session session;
	
	public static void setUpConnection() {
		cluster = Cluster.builder()
			      .addContactPoint(DBConnectionProperties.NODE)
			      .withPort(DBConnectionProperties.PORT)
			      .build();
		session = cluster.connect(DBConnectionProperties.KEYSAPCE);
	}
	
	public static Session getSession() {
		if (session == null) {
			throw new RuntimeException("Connection not set up!");
		}
		
		return session;
	}
	
}
