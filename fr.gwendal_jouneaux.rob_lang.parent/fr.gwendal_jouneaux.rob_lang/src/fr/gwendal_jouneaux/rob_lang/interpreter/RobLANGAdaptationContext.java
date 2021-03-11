package fr.gwendal_jouneaux.rob_lang.interpreter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.Resource;

public class RobLANGAdaptationContext extends AdaptationContext {

	@Override
	public Map<String, Double> loadUserConfig() {
		Map<String, Double> out = new HashMap<String, Double>();
		String energyStr = "0.0";
		String speedStr = "0.0";
		try {
			String confFilePath = System.getenv("CONF_PATH");
			File confFile = new File(confFilePath);
			Scanner scanner = new Scanner(confFile);
			energyStr = scanner.nextLine();
			speedStr = scanner.nextLine();
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		out.put("Energy", Double.parseDouble(energyStr) / 100.0);
		out.put("Time", Double.parseDouble(speedStr) / 100.0);
		return out;
	}

	@Override
	public String[] softgoalIDs() {
		return new String[] { "Energy", "Time" };
	}

	@Override
	protected Resource[] createResources() {
		return new Resource[] { new Resource("Battery") };
	}

}
