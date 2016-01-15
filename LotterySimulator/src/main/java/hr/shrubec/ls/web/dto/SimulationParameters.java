package hr.shrubec.ls.web.dto;

public class SimulationParameters {

	private String type;
	private String duration;
	private boolean[] days;
	private Number[] renderNumbers;
	private int numbersFrom;
	private int numbers;
	private String random;
	
	
	public void initialize() {
		initializeNumbersFrom();
		initializeNumbers();
		initializeRenderNumbers();
	}
	
	private void initializeNumbersFrom() {
		if (type.equals("1")) numbersFrom= 35;
		else if (type.equals("2")) numbersFrom=  36;
		else if (type.equals("3")) numbersFrom=  40;
		else if (type.equals("4")) numbersFrom=  42;
		else if (type.equals("5")) numbersFrom=  90;
		else if (type.equals("6")) numbersFrom=  39;
		else if (type.equals("7")) numbersFrom=  42;
		else if (type.equals("8")) numbersFrom=  45;
		else if (type.equals("9")) numbersFrom=  49;
		else if (type.equals("10")) numbersFrom=  35;
		else if (type.equals("11")) numbersFrom=  39;
		else if (type.equals("12")) numbersFrom=  49;
		else numbersFrom=0;
	}
	
	private void initializeNumbers() {
		if (type.equals("1")) numbers=  5;
		else if (type.equals("2")) numbers= 5;
		else if (type.equals("3")) numbers= 5;
		else if (type.equals("4")) numbers= 5;
		else if (type.equals("5")) numbers= 5;
		else if (type.equals("6")) numbers= 6;
		else if (type.equals("7")) numbers= 6;
		else if (type.equals("8")) numbers= 6;
		else if (type.equals("9")) numbers= 6;
		else if (type.equals("10")) numbers= 7;
		else if (type.equals("11")) numbers= 7;
		else if (type.equals("12")) numbers= 7;
		else numbers= 0;
	}
	
	private void initializeRenderNumbers() {
		renderNumbers=new Number [90];
		int num=getNumbersFrom();
		for (int i=1; i <= 90; i++) {
			if (i <= num) {
				Number n=new Number(i,true,false);
				renderNumbers[i-1] = n;
			}
			else {
				Number n=new Number(i,false,false);
				renderNumbers[i-1] = n;
			}
		}
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public boolean[] getDays() {
		return days;
	}
	public void setDays(boolean[] days) {
		this.days = days;
	}
	public int getNumbersFrom() {
		return numbersFrom;
	}
	public void setNumbersFrom(int numbersFrom) {
		this.numbersFrom = numbersFrom;
	}
	public int getNumbers() {
		return numbers;
	}
	public void setNumbers(int numbers) {
		this.numbers = numbers;
	}
	public Number[] getRenderNumbers() {
		return renderNumbers;
	}
	public void setRenderNumbers(Number[] renderNumbers) {
		this.renderNumbers = renderNumbers;
	}

	public String getRandom() {
		return random;
	}

	public void setRandom(String random) {
		this.random = random;
	}
	
}
