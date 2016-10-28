package service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import backend.Aggregator;
import backend.ProcessJson;

@RestController
public class Controller {
	ProcessJson pj = new ProcessJson();
	Aggregator agg = new Aggregator();
	JSONParser parser = new JSONParser();

	@RequestMapping(path = "/process", method=RequestMethod.POST)
	public JSONObject process(@RequestBody String dailyDataString) {
		System.out.println(dailyDataString);
		JSONObject responseObj = null;
		try {
			// Process the string received from the front end
			pj.processDailyDataString(dailyDataString);
			pj.printDailySet();
			
			// send the daily set to the aggregate fn
			agg.aggregate(pj.dailySet);
			agg.printAggregatedSet();
			
			responseObj = (JSONObject) parser.parse("{\"result\": \"done\"}");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				responseObj = (JSONObject) parser.parse("{\"result\": \"failed\"}");
			} catch (ParseException e1) {
			}
		}


		return responseObj;
	}
}
