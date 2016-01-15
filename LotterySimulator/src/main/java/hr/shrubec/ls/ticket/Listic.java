package hr.shrubec.ls.ticket;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import hr.shrubec.ls.loto.Util;
import hr.shrubec.ls.random.Ranecu;

public class Listic {
	
	
	private Boolean sistemski=false;
	private Map<Integer,Set<Broj>> kombinacije=new TreeMap<Integer,Set<Broj>>();
	
	
	public Listic(Boolean sistemski) {
		this.sistemski=sistemski;
	}
	
	public void dodajRandomKombinaciju(Ranecu ranescu,Integer brojKrizica,Integer max) {
		
		Set<Broj> kombinacija=new TreeSet<Broj>();
		while (kombinacija.size() <= brojKrizica) {			
			if (kombinacija.size() == brojKrizica) break;
			Integer odabraniBroj=ranescu.choose(1, max);
			if (!Util.isOdabrani(kombinacija,odabraniBroj)) {
				kombinacija.add(new Broj(odabraniBroj));	
			}
		}
		
		dodajKombinaciju(kombinacija);
		
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer,Set<Broj>> getDobitneKombinacije(Integer min) {
		
		Map<Integer,Set<Broj>> dobitneKombinacije=new TreeMap<Integer,Set<Broj>>();
		Set<Integer> oznakeKombinacija=kombinacije.keySet();
		for (Integer key:oznakeKombinacija) {		
			Set<Broj> kombinacija=kombinacije.get(key);
			int pogodjeno=0;
			Iterator it=kombinacija.iterator();
			while (it.hasNext()) {
			      Broj broj=(Broj)it.next();
			      if (broj.getPogodjen().booleanValue()) 
			    	  pogodjeno++;
			}
			if(pogodjeno >= min.intValue())
				dobitneKombinacije.put(key, kombinacija);
		}
		
		return dobitneKombinacije;
	}
	
	
	@SuppressWarnings("unchecked")
	public Integer getBrojPogodjenihUKombinaciji(Integer oznakaKombinacija) {
		
		Set<Broj> kombinacija=kombinacije.get(oznakaKombinacija);
		int pogodjeno=0;
		Iterator it=kombinacija.iterator();
		while (it.hasNext()) {
		      Broj broj=(Broj)it.next();
		      if (broj.getPogodjen().booleanValue()) 
		    	  pogodjeno++;
		}
		
		return pogodjeno;
		
	}
	
	
	public void dodajKombinaciju(Set<Broj> kombinacija) {		
		kombinacije.put(kombinacije.size()+1, kombinacija);
	}
	
	public void dodajKombinaciju(Integer brojKombinacije,Set<Broj> kombinacija) {

		kombinacije.put(brojKombinacije, kombinacija);
	
	}



	public Map<Integer, Set<Broj>> getKombinacije() {
		return kombinacije;
	}



	public void setKombinacije(Map<Integer, Set<Broj>> kombinacije) {
		this.kombinacije = kombinacije;
	}



	public Boolean getSistemski() {
		return sistemski;
	}



	public void setSistemski(Boolean sistemski) {
		this.sistemski = sistemski;
	}

	
}
