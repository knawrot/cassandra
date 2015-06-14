package pl.agh.edu.iosr.cassandra.entities;

public class QueryResultsRow {
	private int id;
	private String groupBy;
	private String result;
	
	public QueryResultsRow(int id, String groupBy, String result) {
		this.id = id;
		this.groupBy = groupBy;
		this.result = result;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}

}
