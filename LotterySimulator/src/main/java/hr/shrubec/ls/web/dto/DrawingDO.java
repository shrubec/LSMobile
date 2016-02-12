package hr.shrubec.ls.web.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DrawingDO {

	private Integer drawing;
	private String date;
	private String numberOfHits;
	private List<Integer> drawedNmbers;
	private List<Integer> selectedNumbers;
	private BigDecimal percantage;
	
	private String message;
	private boolean finished;

	public DrawingDO() {
		
	}
	
	public DrawingDO(Integer drawing, String date, String numberOfHits, List<Integer> drawedNmbers,List<Integer> selectedNumbers,Integer totalDrawingCount) {
		this.drawing = drawing;
		this.date = date;
		this.numberOfHits = numberOfHits;
		this.drawedNmbers = drawedNmbers;
		this.selectedNumbers=selectedNumbers;
		calculatePercantage(totalDrawingCount);
		Collections.sort(this.selectedNumbers, new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				// TODO Auto-generated method stub
				return o2.compareTo(o1);
			}
			
		});
		
		Collections.sort(this.drawedNmbers, new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				// TODO Auto-generated method stub
				return o2.compareTo(o1);
			}
			
		});
		
	}

	private void calculatePercantage(Integer totalDrawingCount) {
		BigDecimal bd1 = new BigDecimal(this.drawing);
		BigDecimal bdTotal = bd1.divide(new BigDecimal(totalDrawingCount), 2, RoundingMode.HALF_UP);
		percantage = bdTotal.multiply(new BigDecimal(100));
	}

	public Integer getDrawing() {
		return drawing;
	}

	public void setDrawing(Integer drawing) {
		this.drawing = drawing;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<Integer> getDrawedNmbers() {
		return drawedNmbers;
	}

	public void setDrawedNmbers(List<Integer> drawedNmbers) {
		this.drawedNmbers = drawedNmbers;
	}

	public String getNumberOfHits() {
		return numberOfHits;
	}

	public void setNumberOfHits(String numberOfHits) {
		this.numberOfHits = numberOfHits;
	}

	public BigDecimal getPercantage() {
		return percantage;
	}

	public void setPercantage(BigDecimal percantage) {
		this.percantage = percantage;
	}

	public List<Integer> getSelectedNumbers() {
		return selectedNumbers;
	}

	public void setSelectedNumbers(List<Integer> selectedNumbers) {
		this.selectedNumbers = selectedNumbers;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	
}
