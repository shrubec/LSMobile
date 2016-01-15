package hr.shrubec.ls.app;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	@Autowired
	private HttpSession httpSession;
	
	@Autowired 
	private InfoCollector infoCollector;
	
	@RequestMapping("/")
	public String main() {
		infoCollector.addSession(httpSession.getId());
		return "simulationMain.html";
	}
	
}
