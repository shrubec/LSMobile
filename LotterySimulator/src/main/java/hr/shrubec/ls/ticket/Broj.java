package hr.shrubec.ls.ticket;

@SuppressWarnings("unchecked")
public class Broj implements Comparable{

	private Integer broj;
	private Boolean pogodjen=false;
	
	
	public Broj() {
		
	}

	public Broj(Integer broj) {
		this.broj=broj;
	}

	public Integer getBroj() {
		return broj;
	}


	public void setBroj(Integer broj) {
		this.broj = broj;
	}


	public Boolean getPogodjen() {
		return pogodjen;
	}


	public void setPogodjen(Boolean pogodjen) {
		this.pogodjen = pogodjen;
	}

//	@Override
	public int compareTo(Object o) {
		Broj br=(Broj) o;
		return broj.compareTo(br.getBroj());
	}
	
	
	
}
