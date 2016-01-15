package hr.shrubec.ls.loto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KoloDO {

	private Integer kolo;
	private Date datum;
	private List<KombinacijaDO> kombinacije=new ArrayList<KombinacijaDO>();
	private List<Integer> izvuceno=new ArrayList<Integer>();
	
	
	private Integer ukupnoPogodjeno0=0;
	private Integer ukupnoPogodjeno1=0;
	private Integer ukupnoPogodjeno2=0;
	private Integer ukupnoPogodjeno3=0;
	private Integer ukupnoPogodjeno4=0;
	private Integer ukupnoPogodjeno5=0;
	private Integer ukupnoPogodjeno6=0;
	private Integer ukupnoPogodjeno7=0;
	private Integer ukupnoPogodjeno8=0;
	private Integer ukupnoPogodjeno9=0;
	private Integer ukupnoPogodjeno10=0;
	private Integer ukupnoPogodjeno11=0;
	private Integer ukupnoPogodjeno12=0;
	
	private Integer ukupnoOdigrano=0;
	

	public Integer getKolo() {
		return kolo;
	}

	
	public void povecajPogodjeno(int pogodjeno) {
		
		if (pogodjeno == 0)
			ukupnoPogodjeno0++;
		if (pogodjeno == 1)
			ukupnoPogodjeno1++;
		if (pogodjeno == 2)
			ukupnoPogodjeno2++;
		if (pogodjeno == 3)
			ukupnoPogodjeno3++;
		if (pogodjeno == 4)
			ukupnoPogodjeno4++;
		if (pogodjeno == 5)
			ukupnoPogodjeno5++;
		if (pogodjeno == 6)
			ukupnoPogodjeno6++;
		if (pogodjeno == 7)
			ukupnoPogodjeno7++;
		if (pogodjeno == 8)
			ukupnoPogodjeno8++;
		if (pogodjeno == 9)
			ukupnoPogodjeno9++;
		if (pogodjeno == 10)
			ukupnoPogodjeno10++;
		if (pogodjeno == 11)
			ukupnoPogodjeno11++;
		if (pogodjeno == 12)
			ukupnoPogodjeno12++;
		
	}
	
	
	public void setKolo(Integer kolo) {
		this.kolo = kolo;
	}
	public Date getDatum() {
		return datum;
	}
	public void setDatum(Date datum) {
		this.datum = datum;
	}
	public List<KombinacijaDO> getKombinacije() {
		return kombinacije;
	}
	public void setKombinacije(List<KombinacijaDO> kombinacije) {
		this.kombinacije = kombinacije;
	}
	public List<Integer> getIzvuceno() {
		return izvuceno;
	}
	public void setIzvuceno(List<Integer> izvuceno) {
		this.izvuceno = izvuceno;
	}
	public Integer getUkupnoPogodjeno0() {
		return ukupnoPogodjeno0;
	}
	public void setUkupnoPogodjeno0(Integer ukupnoPogodjeno0) {
		this.ukupnoPogodjeno0 = ukupnoPogodjeno0;
	}
	public Integer getUkupnoPogodjeno1() {
		return ukupnoPogodjeno1;
	}
	public void setUkupnoPogodjeno1(Integer ukupnoPogodjeno1) {
		this.ukupnoPogodjeno1 = ukupnoPogodjeno1;
	}
	public Integer getUkupnoPogodjeno2() {
		return ukupnoPogodjeno2;
	}
	public void setUkupnoPogodjeno2(Integer ukupnoPogodjeno2) {
		this.ukupnoPogodjeno2 = ukupnoPogodjeno2;
	}
	public Integer getUkupnoPogodjeno3() {
		return ukupnoPogodjeno3;
	}
	public void setUkupnoPogodjeno3(Integer ukupnoPogodjeno3) {
		this.ukupnoPogodjeno3 = ukupnoPogodjeno3;
	}
	public Integer getUkupnoPogodjeno4() {
		return ukupnoPogodjeno4;
	}
	public void setUkupnoPogodjeno4(Integer ukupnoPogodjeno4) {
		this.ukupnoPogodjeno4 = ukupnoPogodjeno4;
	}
	public Integer getUkupnoPogodjeno5() {
		return ukupnoPogodjeno5;
	}
	public void setUkupnoPogodjeno5(Integer ukupnoPogodjeno5) {
		this.ukupnoPogodjeno5 = ukupnoPogodjeno5;
	}
	public Integer getUkupnoPogodjeno6() {
		return ukupnoPogodjeno6;
	}
	public void setUkupnoPogodjeno6(Integer ukupnoPogodjeno6) {
		this.ukupnoPogodjeno6 = ukupnoPogodjeno6;
	}
	public Integer getUkupnoPogodjeno7() {
		return ukupnoPogodjeno7;
	}
	public void setUkupnoPogodjeno7(Integer ukupnoPogodjeno7) {
		this.ukupnoPogodjeno7 = ukupnoPogodjeno7;
	}
	public Integer getUkupnoPogodjeno8() {
		return ukupnoPogodjeno8;
	}
	public void setUkupnoPogodjeno8(Integer ukupnoPogodjeno8) {
		this.ukupnoPogodjeno8 = ukupnoPogodjeno8;
	}
	public Integer getUkupnoPogodjeno9() {
		return ukupnoPogodjeno9;
	}
	public void setUkupnoPogodjeno9(Integer ukupnoPogodjeno9) {
		this.ukupnoPogodjeno9 = ukupnoPogodjeno9;
	}
	public Integer getUkupnoPogodjeno10() {
		return ukupnoPogodjeno10;
	}
	public void setUkupnoPogodjeno10(Integer ukupnoPogodjeno10) {
		this.ukupnoPogodjeno10 = ukupnoPogodjeno10;
	}

	public Integer getUkupnoPogodjeno11() {
		return ukupnoPogodjeno11;
	}

	public void setUkupnoPogodjeno11(Integer ukupnoPogodjeno11) {
		this.ukupnoPogodjeno11 = ukupnoPogodjeno11;
	}

	public Integer getUkupnoPogodjeno12() {
		return ukupnoPogodjeno12;
	}

	public void setUkupnoPogodjeno12(Integer ukupnoPogodjeno12) {
		this.ukupnoPogodjeno12 = ukupnoPogodjeno12;
	}


	public Integer getUkupnoOdigrano() {
		return ukupnoOdigrano;
	}


	public void setUkupnoOdigrano(Integer ukupnoOdigrano) {
		this.ukupnoOdigrano = ukupnoOdigrano;
	}
	
	
	
	
}
