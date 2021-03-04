package fr.gwendal_jouneaux.rob_lang.interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.gwendal_jouneaux.rob_lang.interpreter.decision.model.Goal;
import fr.gwendal_jouneaux.rob_lang.interpreter.decision.model.Resource;


public abstract class AdaptationContext {
	private Map<String, Double> userConfig;
	private List<Resource> resources;
	private List<Goal> tradeOffByModule;
	private Goal tradeOff;

	public AdaptationContext() {
		tradeOff = new Goal("GlobalTradeOff");
		resources = new ArrayList<Resource>();
		for (Resource resource : createResources()) {
			resources.add(resource);
		}
		tradeOffByModule = new ArrayList<Goal>();	
	}
	
	final void registerModule(AbstractAdaptationModule module) {
		module.afterRegister();
		tradeOffByModule.add(module.getModuleTradeOff());
		tradeOff.addContribution(module.getModuleTradeOff(), 1.0);
	}
	
	final Map<String, Double> getUserConfig() {
		return userConfig;
	}

	final void setUserConfig(Map<String, Double> userConfig) {
		this.userConfig = userConfig;
	}
	
	public final List<Resource> getResources() {
		return resources;
	}
	
	final List<Goal> getTradeOffByModule() {
		return tradeOffByModule;
	}
	
	final Goal getGoal() {
		return tradeOff;
	}

	public abstract Map<String, Double> loadUserConfig();
	
	public abstract String[] softgoalIDs();
	
	protected abstract Resource[] createResources();
}
