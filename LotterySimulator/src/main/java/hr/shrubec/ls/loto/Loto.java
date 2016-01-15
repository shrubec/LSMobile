package hr.shrubec.ls.loto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import hr.shrubec.ls.random.RanMT;
import hr.shrubec.ls.random.Ranecu;
import hr.shrubec.ls.ticket.Broj;
import hr.shrubec.ls.ticket.Listic;
import hr.shrubec.ls.web.dto.DrawingDO;
import hr.shrubec.ls.web.dto.SimulationDO;
import hr.shrubec.ls.web.dto.StatisticsDO;

public class Loto {

	Logger LOGGER=Logger.getLogger(Loto.class.getName());
	
	private RanMT ranMT;
	private Ranecu ranescu;
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	private Calendar cal=Calendar.getInstance();
	
	private int brojeva;
	private int odBrojeva;
	
	private Integer trenutnoKoloBroj=1;
	private KoloDO trenutnoKolo;
	private boolean finished=false;
	private boolean savedAsFinished=false;
	
	private KoloDO koloDo=new KoloDO();
	
	private IzvlacenjeDO trenutnoIzvlacenje;
	private IzvlacenjeDO izvlacenjeDo=new IzvlacenjeDO();
	
	
	private boolean jackpot=false;
	
	private List<String> messagesList=new ArrayList<String>();
	private List<Integer> izvlacenja;
	private int trajanjeGodina=10;
	private Calendar startTime;
	private boolean paused=false;
//	private boolean nextStepActivated=false;
	
	private List<List<Integer>> kombinacijeBrojeva;
	
	private SimpMessagingTemplate template;
	private Integer totalDrawingCount=0;
	private String random;
	
	private Integer speed=20;
	private int modulKolo=5;
	private int modulStatistika=100;
	
	private HttpSession httpSession;
	
	public void destroySimulation() {
		this.finished=true;
		if (kombinacijeBrojeva != null) {
			kombinacijeBrojeva.clear();
			messagesList.clear();
			trenutnoIzvlacenje=null;
			izvlacenjeDo=null;
			trenutnoKolo=null;
			ranescu=null;		
			ranMT=null;
			totalDrawingCount=null;
			template=null;
			startTime=null;
			random=null;
		}
	}
	
	
	public Loto(SimpMessagingTemplate template,int brojeva, int odBrojeva,int trajanjeGodina,List<String> izvlacenja,String random,HttpSession httpSession) {
		
		this.httpSession=httpSession;
		this.random=random;
		this.template=template;
		this.brojeva=brojeva;
		this.odBrojeva=odBrojeva;
		this.trajanjeGodina=trajanjeGodina;
		this.izvlacenja=new ArrayList<Integer>();
		
		startTime=Calendar.getInstance();
		for (String number:izvlacenja) {
			this.izvlacenja.add(Integer.valueOf(number));
		}
		
		ranescu=new Ranecu(new Date());		
		ranMT=new RanMT(new Date());
		
		calculateTotalDrawingCount(this.izvlacenja, trajanjeGodina);
		Integer dan=cal.get(Calendar.DAY_OF_WEEK);
		while (!this.izvlacenja.contains(dan)) {
			cal.add(Calendar.DAY_OF_WEEK,1);
			dan=cal.get(Calendar.DAY_OF_WEEK);
		}

	}
	
	
	private void calculateTotalDrawingCount(List<Integer> days,Integer years) {
		
		Calendar cal=Calendar.getInstance();
		Calendar startTime=Calendar.getInstance();
		
		while(true) {
			cal.add(Calendar.DAY_OF_YEAR,1);
			int dan=cal.get(Calendar.DAY_OF_WEEK);
			while (!days.contains(dan)) {
				cal.add(Calendar.DAY_OF_YEAR,1);
				dan=cal.get(Calendar.DAY_OF_WEEK);
			}
			totalDrawingCount++;
			
			if (cal.get(Calendar.YEAR) >= (startTime.get(Calendar.YEAR) + years)) {
				return;
			}
		}
		
	}
	
	
	public void kombinacije(List<List<Integer>> kombinacijeBrojeva) {
		
		if (kombinacijeBrojeva != null)
			this.kombinacijeBrojeva=kombinacijeBrojeva;
		
		while(!finished) {
			
			if (paused) {
				return;
			}
			
			if (this.speed == 0) {
				this.speed=10;
			}
			
			
			
			try {
				Thread.sleep(this.speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			kombinacijePojedinacno();
			
//			if (nextStepActivated) {
//				paused=true;
//			}
		}
	}
	
	public void kombinacijePojedinacno() {
		
		System.out.println("Izvlacim kolo " + trenutnoKoloBroj+", " + httpSession.getMaxInactiveInterval()+", " + httpSession.getLastAccessedTime());
		
		if (this.finished == true) {
			return;
		}
		
		List<Listic> listici=new ArrayList<Listic>();
		for (List<Integer> brojevi:kombinacijeBrojeva) {
			
			Listic listic=new Listic(false);				
			Set<Broj> kombinacija=new TreeSet<Broj>();
			for (Integer broj:brojevi) {
				kombinacija.add(new Broj(broj));				
			}
	
			listic.dodajKombinaciju(kombinacija);
			listici.add(listic);
		}

		odigraj(trenutnoKoloBroj,listici);		
		
		if (jackpot) {
			this.finished=true;
			LOGGER.info("JACKPOT!!!");
			
			SimulationDO simulationDO=getCurrentSimulationObject(izvlacenjeDo.getKola().get(0).getKombinacije().get(0).getOdigrano(), izvlacenjeDo.getKola().get(0).getKombinacije().get(0).getPogodjeno().size());
			simulationDO.getDrawing().setFinished(true);
			simulationDO.getDrawing().setMessage("Jackpot winned!");
			this.template.convertAndSend("/topic/message/"+random, simulationDO.getDrawing());
			this.template.convertAndSend("/topic/message2/"+random, simulationDO.getStatistics());
			
			return;
			
		}
		
		
		if (this.cal.get(Calendar.YEAR) >= (startTime.get(Calendar.YEAR) + trajanjeGodina)) {
			this.finished=true;
			SimulationDO simulationDO=getCurrentSimulationObject(izvlacenjeDo.getKola().get(0).getKombinacije().get(0).getOdigrano(), izvlacenjeDo.getKola().get(0).getKombinacije().get(0).getPogodjeno().size());
			simulationDO.getDrawing().setFinished(true);
			simulationDO.getDrawing().setMessage("Simulation finished!");
			this.template.convertAndSend("/topic/message/"+random, simulationDO.getDrawing());
			this.template.convertAndSend("/topic/message2/"+random, simulationDO.getStatistics());
			
			
			return;
		}
	
		trenutnoKoloBroj++;
	}
	
	
	public void kombinacijeRandom(int brojKola,int listicaUKolu,int brojKombinacija) {
		
		for (int i=1; i <=brojKola; i++) {		
			
			List<Listic> listici=new ArrayList<Listic>();
			for (int k=1; k<=listicaUKolu; k++) {
				Listic listic=new Listic(false);
				for (int z=1; z <= brojKombinacija; z++)
					listic.dodajRandomKombinaciju(ranescu, brojeva, odBrojeva);
				listici.add(listic);
			}
			

			odigraj(i,listici);			
		}
		
	}
	
	public void sistem(int brojKola,List<List<Integer>> kombinacijeBrojeva) {
		for (int i=1; i <=brojKola; i++) {	
			
			List<Listic> listici=new ArrayList<Listic>();
			for (List<Integer> brojevi:kombinacijeBrojeva) {
				
				Listic listic=new Listic(true);				
				Set<Broj> kombinacija=new TreeSet<Broj>();
				for (Integer broj:brojevi) {
					kombinacija.add(new Broj(broj));				
				}
		
				listic.dodajKombinaciju(kombinacija);
				listici.add(listic);
			}

			odigraj(i,listici);			
		}
	}
	
	public void sistemRandom(int brojKola,int listicaUKolu,int brojevaUKombinaciji) {
		
		for (int i=1; i <=brojKola; i++) {			
			
			List<Listic> listici=new ArrayList<Listic>();
			for (int k=1; k<=listicaUKolu; k++) {
				Listic listic=new Listic(true);
				listic.dodajRandomKombinaciju(ranescu, brojevaUKombinaciji, odBrojeva);
				listici.add(listic);
			}
	
			odigraj(i,listici);			
		}
		
		
	
	}

	private void odigraj(int kolo,List<Listic> listici) {
		
		List<Listic> osvjezeniListici=new ArrayList<Listic>();
		osvjezeniListici.addAll(listici);
		
//		if (brojeva == 6 && odBrojeva == 45) {
//			cal.add(Calendar.DAY_OF_WEEK,7);
//		}
//		else if (brojeva == 7 && odBrojeva == 39) {
//			int dan=cal.get(Calendar.DAY_OF_WEEK);
//			if (dan == Calendar.WEDNESDAY ) 
//				cal.add(Calendar.DAY_OF_WEEK,3);
//			else
//				cal.add(Calendar.DAY_OF_WEEK,4);
//		}
//		
		
		cal.add(Calendar.DAY_OF_WEEK,1);
		int dan=cal.get(Calendar.DAY_OF_WEEK);
		while (!izvlacenja.contains(dan)) {
			cal.add(Calendar.DAY_OF_WEEK,1);
			dan=cal.get(Calendar.DAY_OF_WEEK);
		}
		
	
		Izvlacenje izvlacenje=new Izvlacenje(kolo,cal.getTime(),osvjezeniListici);
		izvlacenje.izvuci(brojeva, odBrojeva, ranMT);
		izvlacenje.provjeriRezultate();

		izvlacenjeDo=new IzvlacenjeDO();
		List<Listic> dobitniListici=izvlacenje.getListici();
		for (Listic listic:dobitniListici) {
			Map<Integer,Set<Broj>> dobitneKombinacije=listic.getDobitneKombinacije(0);
			Set<Integer> oznakeKombinacija=dobitneKombinacije.keySet();
			
			
			
			for (Integer key:oznakeKombinacija) {
				
				KoloDO prethodnoKolo=koloDo;
				
				koloDo=new KoloDO();
				koloDo.setKolo(izvlacenje.getKolo());
				koloDo.setDatum(izvlacenje.getDatum());
				
				
				Set<Broj> kombinacija=dobitneKombinacije.get(key);
				
				KombinacijaDO kombinacijaDo=new KombinacijaDO();
				
//				LOGGER.info("Odabrano:");
				for (Broj broj:kombinacija) {
//					LOGGER.info(broj.getBroj().intValue()+ ", ");
					kombinacijaDo.getOdigrano().add(broj.getBroj());
				}
//				LOGGER.info("Pogo�eno:");
				
				int pogodjeno=0;
				for (Broj broj:kombinacija) {
					if (broj.getPogodjen()) {
//						LOGGER.info(broj.getBroj().intValue()+ ", ");
						kombinacijaDo.getPogodjeno().add(broj.getBroj());
						pogodjeno++;
					}
				}
				
				
				koloDo.setUkupnoOdigrano(prethodnoKolo.getUkupnoOdigrano()+1);
				koloDo.setUkupnoPogodjeno0(prethodnoKolo.getUkupnoPogodjeno0());
				koloDo.setUkupnoPogodjeno1(prethodnoKolo.getUkupnoPogodjeno1());
				koloDo.setUkupnoPogodjeno2(prethodnoKolo.getUkupnoPogodjeno2());
				koloDo.setUkupnoPogodjeno3(prethodnoKolo.getUkupnoPogodjeno3());
				koloDo.setUkupnoPogodjeno4(prethodnoKolo.getUkupnoPogodjeno4());
				koloDo.setUkupnoPogodjeno5(prethodnoKolo.getUkupnoPogodjeno5());
				koloDo.setUkupnoPogodjeno6(prethodnoKolo.getUkupnoPogodjeno6());
				koloDo.setUkupnoPogodjeno7(prethodnoKolo.getUkupnoPogodjeno7());
				koloDo.setUkupnoPogodjeno8(prethodnoKolo.getUkupnoPogodjeno8());
				koloDo.setUkupnoPogodjeno9(prethodnoKolo.getUkupnoPogodjeno9());
				koloDo.setUkupnoPogodjeno10(prethodnoKolo.getUkupnoPogodjeno10());
				
				koloDo.povecajPogodjeno(pogodjeno);
//				LOGGER.info("Izvu�eno:");
				for (Broj broj:izvlacenje.getIzvucenaKombinacija()) {
//					LOGGER.info(broj.getBroj().intValue()+ ", ");
					koloDo.getIzvuceno().add(broj.getBroj());
				}
				
				koloDo.getKombinacije().add(kombinacijaDo);
				
				izvlacenjeDo.getKola().add(koloDo);
				
//				LOGGER.info("KOLA SIZE: "  +izvlacenjeDo.getKola().size() );
//				LOGGER.info(izvlacenje.getKolo() +". KOLO, DATUM " + sdf.format(izvlacenje.getDatum()) + ", UKUPNO POGO�ENO: " + listic.getBrojPogodjenihUKombinaciji(key) + ", ");
				
				setTrenutnoKolo(koloDo);
				
				
				if (listic.getBrojPogodjenihUKombinaciji(key).intValue() >= (brojeva)) {
					jackpot=true;
					messagesList.add( sdf.format(izvlacenje.getDatum()) + " Jackpot!");
				}
				
				if (listic.getBrojPogodjenihUKombinaciji(key).intValue() >= (brojeva - 1)) {
					messagesList.add( sdf.format(izvlacenje.getDatum()) + " Jackpot missed by one number!");
				}
			}
		}
		
		setTrenutnoIzvlacenje(izvlacenjeDo);
		
		SimulationDO simulationDO=getCurrentSimulationObject(izvlacenjeDo.getKola().get(0).getKombinacije().get(0).getOdigrano(), izvlacenjeDo.getKola().get(0).getKombinacije().get(0).getPogodjeno().size());
		
		if (paused) {
			this.template.convertAndSend("/topic/message/"+random, simulationDO.getDrawing());
			this.template.convertAndSend("/topic/message2/"+random, simulationDO.getStatistics());
		}
		else {
			if (trenutnoKolo.getKolo() % modulKolo == 0)
				this.template.convertAndSend("/topic/message/"+random, simulationDO.getDrawing());
			
			if (trenutnoKolo.getKolo() % modulStatistika == 0)
				this.template.convertAndSend("/topic/message2/"+random, simulationDO.getStatistics());
		}

	}

	
	
	private SimulationDO getCurrentSimulationObject(List<Integer> odigrano,Integer pogodjeno) {
		KoloDO trenutnoKolo=getTrenutnoIzvlacenje().getKola().get(0);
		
		java.util.Set<Integer> set=new java.util.HashSet<Integer>();
		set.addAll(getTrenutnoIzvlacenje().getKola().get(0).getIzvuceno());
		
		
		DrawingDO drawing=new DrawingDO(trenutnoKolo.getKolo(),sdf.format(trenutnoKolo.getDatum()),pogodjeno+" / "+getBrojeva(),trenutnoKolo.getIzvuceno(),odigrano,totalDrawingCount);
		
		Object[][] csList=new Object[brojeva+1][2];
		csList[0]=new Object[] {"All missed: "+koloDo.getUkupnoPogodjeno0(),koloDo.getUkupnoPogodjeno0()};
		csList[1]=new Object[] {"1 hit: "+koloDo.getUkupnoPogodjeno1(),koloDo.getUkupnoPogodjeno1()};
		csList[2]=new Object[] {"2 hits: "+koloDo.getUkupnoPogodjeno2(),koloDo.getUkupnoPogodjeno2()};
		csList[3]=new Object[] {"3 hits: "+koloDo.getUkupnoPogodjeno3(),koloDo.getUkupnoPogodjeno3()};
		csList[4]=new Object[] {"4 hits: "+koloDo.getUkupnoPogodjeno4(),koloDo.getUkupnoPogodjeno4()};
		csList[5]=new Object[] {"5 hits: "+koloDo.getUkupnoPogodjeno5(),koloDo.getUkupnoPogodjeno5()};
		if (brojeva >= 6)
			csList[6]=new Object[] {"6 hits: "+koloDo.getUkupnoPogodjeno6(),koloDo.getUkupnoPogodjeno6()};
		if (brojeva >= 7)
			csList[7]=new Object[] {"7 hits: 0",koloDo.getUkupnoPogodjeno7(),koloDo.getUkupnoPogodjeno7()};
		
		StatisticsDO stat=new StatisticsDO(csList);

		return new SimulationDO(drawing,stat);
		
		
	}
	
	

	public KoloDO getTrenutnoKolo() {
		return trenutnoKolo;
	}

	public void setTrenutnoKolo(KoloDO trenutnoKolo) {
		this.trenutnoKolo = trenutnoKolo;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public IzvlacenjeDO getTrenutnoIzvlacenje() {
		return trenutnoIzvlacenje;
	}

	public void setTrenutnoIzvlacenje(IzvlacenjeDO trenutnoIzvlacenje) {
		this.trenutnoIzvlacenje = trenutnoIzvlacenje;
	}

	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		Integer s=100-speed;
		this.speed = s*2;
		
		if (speed.intValue() == 0) {
			this.speed=500;
		}
		
	}

	public List<String> getMessagesList() {
		return messagesList;
	}

	public void setMessagesList(List<String> messagesList) {
		this.messagesList = messagesList;
	}
	

	public String getLotteryType() {
		return String.valueOf(brojeva) +" / " + String.valueOf(odBrojeva);
	}

	public int getBrojeva() {
		return brojeva;
	}

	public void setBrojeva(int brojeva) {
		this.brojeva = brojeva;
	}

	public boolean isSavedAsFinished() {
		return savedAsFinished;
	}

	public void setSavedAsFinished(boolean savedAsFinished) {
		this.savedAsFinished = savedAsFinished;
	}

	public int getTrajanjeGodina() {
		return trajanjeGodina;
	}

	public void setTrajanjeGodina(int trajanjeGodina) {
		this.trajanjeGodina = trajanjeGodina;
	}

	
	public void pauseSimulation() {
		this.paused=true;
	}
	
	public void resumeSimulation() {
		this.paused=false;
		kombinacije(null);
//		nextStepActivated=false;
	}
	
	public void nextStep() {
//		nextStepActivated=true;
//		paused=false;
		kombinacijePojedinacno();
	}
	


	public boolean isJackpot() {
		return jackpot;
	}

	public void setJackpot(boolean jackpot) {
		this.jackpot = jackpot;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	public void emptyPage()  {
		this.template.convertAndSend("/topic/message/"+random, new DrawingDO());
		this.template.convertAndSend("/topic/message2/"+random, new StatisticsDO());
	}
	
	
}
