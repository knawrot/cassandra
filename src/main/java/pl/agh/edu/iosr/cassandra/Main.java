package pl.agh.edu.iosr.cassandra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pl.agh.edu.iosr.cassandra.client.CassandraClient;
import pl.agh.edu.iosr.cassandra.db.connection.DBConnection;



public class Main {
	private static final String QUERY = "insert into demo.stormcf(word, count) "
										+ "values(?, ?)";
	
	private static void runOnLocalhost() {
		try {
			String line;
			String command = "/home/hadoop/apache-storm-0.9.4/bin/storm"
				+ " jar"
				+ " /home/hadoop/kafkastorm-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
				+ " lambda.kafkastorm.KafkaStormTopology"
				+ " -cql \"" + QUERY + "\"";
			System.out.println(command);
			Process p = Runtime.getRuntime().exec(new String[]{
											"bash"
											,"-c"
											,command});
			BufferedReader bri = new BufferedReader
			        (new InputStreamReader(p.getInputStream()));
			BufferedReader bre = new BufferedReader
					(new InputStreamReader(p.getErrorStream()));
			while ((line = bri.readLine()) != null) {
				System.out.println(line);
			}
			bri.close();
			while ((line = bre.readLine()) != null) {
				System.out.println(line);
			}
			bre.close();
			p.waitFor();
			p.destroy();
			System.out.println("Done.");
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		DBConnection.setUpConnection();
		
		CassandraClient client = new CassandraClient();
		client.doActions();
		
		runOnLocalhost();

		System.exit(0);
	}
}
