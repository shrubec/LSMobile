package hr.shrubec.ls.app;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hr.shrubec.ls.loto.Loto;
import hr.shrubec.ls.web.dto.SimulationParameters;

@RestController
public class SimulationController {
	
	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@Autowired 
	private InfoCollector infoCollector;

	@RequestMapping(path="/saveParameters",method = RequestMethod.POST)
	public SimulationParameters save(@RequestBody SimulationParameters data) {
		infoCollector.addNumbersHit();
		data.initialize();
		httpSession.setAttribute("simulationParameters", data);
		return data;
	}
	
	@RequestMapping(value="/simulationParameters")
	public SimulationParameters getSimulationParameters() {
		clearSimulation();
        return (SimulationParameters)httpSession.getAttribute("simulationParameters");
    }
	
	@RequestMapping(value="/initialSimulationParameters")
	public SimulationParameters getInitialSimulationParameters() {
		
		List<String> list=infoCollector.getInfo();
		for (String s:list) {
			System.out.println(s);
		}
		
		infoCollector.addParametersHit();
		clearSimulation();
		SimulationParameters data=new SimulationParameters();
		data.setType("1");
		data.setDays(new boolean[]{false,false,false,false,false,false,true});
		data.setDuration("2");
		data.setNumbers(0);
		data.setNumbersFrom(0);
		return data;
    }
	
	@RequestMapping(path="/initializeSimulation",method = RequestMethod.POST)
	public SimulationParameters simulation(@RequestBody SimulationParameters data) {
		infoCollector.addSimulationHit();
		httpSession.setAttribute("simulationParameters", data);
		createSimulation(data);
		
//		List<String> days=new ArrayList<String>();
//		for (int i=0; i < data.getDays().length; i++) {
//			boolean b=data.getDays()[i];
//			if (b) {
//				days.add(String.valueOf(i));
//			}
//		}
//		
//		Loto loto=new Loto(template,data.getNumbers(),data.getNumbersFrom(), getDuration(data.getDuration()),days);
//		httpSession.setAttribute("loto",loto);
		
        return data;
    }
	
	@RequestMapping(path="/simulationAction",method = RequestMethod.GET)
	public String action(@RequestParam String action) {
		
		//start simulation
		if (action.equals("1")) {
			infoCollector.addSimulationStartHit();
			Loto loto=(Loto) httpSession.getAttribute("loto");
			
			if (loto != null && loto.isPaused() && !loto.isFinished()) {
				loto.resumeSimulation();
				return action;
			}
			
			SimulationParameters parameters=(SimulationParameters) httpSession.getAttribute("simulationParameters");
			List<Integer> selectedNumbers=new ArrayList<Integer>();
			for (hr.shrubec.ls.web.dto.Number num:parameters.getRenderNumbers()) {
				if (num.isSelected()) {
					selectedNumbers.add(num.getValue());
				}
			}
			
			httpSession.setAttribute("selectedNumbers", selectedNumbers);
			List<List<Integer>> tickets=new ArrayList<List<Integer>>();
			tickets.add(selectedNumbers);
			loto.kombinacije(tickets);
		}
		
		else if (action.equals("2")) {
			infoCollector.addSimulationPauseHit();
			Loto loto=(Loto) httpSession.getAttribute("loto");
			loto.pauseSimulation();
			return action;
			
		}
		
		else if (action.equals("3")) {
			infoCollector.addSimulationNextHit();
			Loto loto=(Loto) httpSession.getAttribute("loto");
			loto.nextStep();
			action="2";
			return action;
			
		}
		
		else if (action.equals("4")) {
			infoCollector.addSimulationResetHit();
			Loto loto=(Loto) httpSession.getAttribute("loto");
			loto.pauseSimulation();
			loto.destroySimulation();
			loto=createSimulation((SimulationParameters) httpSession.getAttribute("simulationParameters"));
			loto.emptyPage();
		}
		
        return action;
    }
	
	
	private Loto createSimulation(SimulationParameters data) {
		List<String> days=new ArrayList<String>();
		for (int i=0; i < data.getDays().length; i++) {
			boolean b=data.getDays()[i];
			if (b) {
				days.add(String.valueOf(i));
			}
		}
		Loto loto=new Loto(template,data.getNumbers(),data.getNumbersFrom(), getDuration(data.getDuration()),days,data.getRandom(),httpSession);
		httpSession.setAttribute("loto",loto);
		return loto;
	}
	
	private static int getDuration(String duration) {
		if (duration.equals("1")) return 10;
		else if (duration.equals("2")) return 20;
		else if (duration.equals("3")) return 50;
		else if (duration.equals("4")) return 100;
		else if (duration.equals("5")) return 500;
		else  return 1000;
	}
	
	private void clearSimulation() {
		Loto loto=(Loto) httpSession.getAttribute("loto");
		if (loto != null) {
			loto.destroySimulation();
		}
	}
	
}
