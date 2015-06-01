package pl.agh.edu.iosr.cassandra.db.connection;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import junit.framework.TestCase;

public class DBConnectionTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void shouldConnect() {
		Cluster cluster = Cluster.builder()
			      .addContactPoint(DBConnectionProperties.NODE)
			      .withPort(DBConnectionProperties.PORT)
			      .build();
		Session session = cluster.connect(DBConnectionProperties.KEYSAPCE);
		
		assertTrue(session != null);
		try {
			session.execute("USE " + DBConnectionProperties.KEYSAPCE + ";");
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
