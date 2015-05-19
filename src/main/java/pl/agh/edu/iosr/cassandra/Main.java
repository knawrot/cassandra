package pl.agh.edu.iosr.cassandra;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import pl.agh.edu.iosr.cassandra.client.CassandraClient;



public class Main {
	private static final int THREADS_NUM = 1;
	
	public static void main(String[] args) {
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < THREADS_NUM; i++) {
			executorService.execute(new CassandraClient());
		}
		executorService.shutdown();
		try {
			executorService.awaitTermination(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
