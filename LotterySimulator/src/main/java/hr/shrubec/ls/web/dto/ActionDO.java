package hr.shrubec.ls.web.dto;

public class ActionDO {

	public static final int START=1;
	public static final int PAUSE=2;
	public static final int NEXT=3;
	public static final int RESTART=4;
	public static final int BACK=5;
	
	private String action;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	
}
