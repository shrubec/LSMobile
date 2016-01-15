package hr.shrubec.ls.web.dto;

public class SimulationDO {

	private DrawingDO drawing;
	private StatisticsDO statistics;
	
	public SimulationDO(DrawingDO drawing,StatisticsDO statistics) {
		this.drawing=drawing;
		this.statistics=statistics;
	}
	
	public DrawingDO getDrawing() {
		return drawing;
	}
	public void setDrawing(DrawingDO drawing) {
		this.drawing = drawing;
	}
	public StatisticsDO getStatistics() {
		return statistics;
	}
	public void setStatistics(StatisticsDO statistics) {
		this.statistics = statistics;
	}
	
	
}
