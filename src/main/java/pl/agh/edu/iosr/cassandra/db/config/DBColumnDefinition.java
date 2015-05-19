package pl.agh.edu.iosr.cassandra.db.config;

public class DBColumnDefinition {
	private final String name;
	private final String dataType;
	private final boolean isPrimaryKey;
	private final boolean isClusteringKey;
	
	public DBColumnDefinition(String name, String dataType) {
		this.name = name;
		this.dataType = dataType;
		isPrimaryKey = false;
		isClusteringKey = false;
	}
	
	public DBColumnDefinition(String name, String dataType, 
							boolean isPrimaryKey, boolean isClusteringKey) {
		this.name = name;
		this.dataType = dataType;
		this.isPrimaryKey = isPrimaryKey;
		this.isClusteringKey = isClusteringKey;
	}
	
	public String getName() {
		return name;
	}

	public String getDataType() {
		return dataType;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	public boolean isClusteringKey() {
		return isClusteringKey;
	}
}
