package hr.shrubec.ls.web.dto;

public class StatisticsDO {

	private Object[][] currentStatistics;
	
	public StatisticsDO() {
		
	}
	
	public StatisticsDO( Object[][] currentStatistics) {
		this.currentStatistics=currentStatistics;
	}

	public Object[][] getCurrentStatistics() {
		return currentStatistics;
	}

	public void setCurrentStatistics(Object[][] currentStatistics) {
		this.currentStatistics = currentStatistics;
	}
	
	
}
