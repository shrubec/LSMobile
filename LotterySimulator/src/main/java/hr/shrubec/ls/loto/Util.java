package hr.shrubec.ls.loto;

import java.util.Set;

import hr.shrubec.ls.ticket.Broj;

public class Util {

	public static boolean isOdabrani(Set<Broj> brojevi,Integer odabrani) {
		
		for (Broj broj:brojevi) {
			if (broj.getBroj().intValue() == odabrani.intValue()) return true;
		}
		return false;
		
	}
	
}
