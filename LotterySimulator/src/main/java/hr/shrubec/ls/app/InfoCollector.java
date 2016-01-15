package hr.shrubec.ls.app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value="singleton")
public class InfoCollector {

	
	private Set<String> sessions=new HashSet<String>();
	private int parametersHit=0;
	private int numbersHit=0;
	private int simulationHit=0;
	private int simulationStartHit=0;
	private int simulationPauseHit=0;
	private int simulationNextHit=0;
	private int simulationResetHit=0;
	
	
	public List<String> getInfo() {
		List<String> info=new ArrayList<String>();
		info.add("Session ids: " + sessions.size());
		info.add("Osnovna forma: " + parametersHit);
		info.add("Inicijalizacija brojeva: " + numbersHit);
		info.add("Inicijalizacija simulacije: " + simulationHit);
		info.add("Start simulacije: " + simulationStartHit);
		info.add("Pause simulacije: " + simulationPauseHit);
		info.add("Sljedece izvlacenje: " + simulationNextHit);
		info.add("Reset simulacije: " + simulationResetHit);
		return info;
	}
	
	public void reset() {
		parametersHit=0;
		numbersHit=0;
		simulationHit=0;
		simulationStartHit=0;
		simulationPauseHit=0;
		simulationNextHit=0;
		simulationResetHit=0;
		sessions.clear();
	}

	public void addParametersHit() {
		parametersHit++;
	}
	public void addNumbersHit() {
		numbersHit++;
	}
	
	public void addSimulationHit() {
		simulationHit++;
	}
	
	public void addSimulationStartHit() {
		simulationStartHit++;
	}
	
	public void addSimulationPauseHit() {
		simulationPauseHit++;
	}
	
	public void addSimulationNextHit() {
		simulationNextHit++;
	}
	
	public void addSimulationResetHit() {
		simulationResetHit++;
	}
	
	public void addSession(String sessionId) {
		sessions.add(sessionId);
	}
	
	
	public Set<String> getSessions() {
		return sessions;
	}

	public void setSessions(Set<String> sessions) {
		this.sessions = sessions;
	}

	public int getParametersHit() {
		return parametersHit;
	}

	public void setParametersHit(int parametersHit) {
		this.parametersHit = parametersHit;
	}

	public int getNumbersHit() {
		return numbersHit;
	}

	public void setNumbersHit(int numbersHit) {
		this.numbersHit = numbersHit;
	}

	public int getSimulationHit() {
		return simulationHit;
	}

	public void setSimulationHit(int simulationHit) {
		this.simulationHit = simulationHit;
	}

	public int getSimulationStartHit() {
		return simulationStartHit;
	}

	public void setSimulationStartHit(int simulationStartHit) {
		this.simulationStartHit = simulationStartHit;
	}

	public int getSimulationPauseHit() {
		return simulationPauseHit;
	}

	public void setSimulationPauseHit(int simulationPauseHit) {
		this.simulationPauseHit = simulationPauseHit;
	}

	public int getSimulationNextHit() {
		return simulationNextHit;
	}

	public void setSimulationNextHit(int simulationNextHit) {
		this.simulationNextHit = simulationNextHit;
	}

	public int getSimulationResetHit() {
		return simulationResetHit;
	}

	public void setSimulationResetHit(int simulationResetHit) {
		this.simulationResetHit = simulationResetHit;
	}
	
	
}
