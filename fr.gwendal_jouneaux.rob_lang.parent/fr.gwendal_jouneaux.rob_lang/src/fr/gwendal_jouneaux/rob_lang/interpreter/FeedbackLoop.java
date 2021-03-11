package fr.gwendal_jouneaux.rob_lang.interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import fr.gwendal_jouneaux.rob_lang.interpreter.AbstractAdaptationModule.Strategy;
import fr.gwendal_jouneaux.rob_lang.interpreter.decision.model.Bounds;
import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.Goal;
import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.Resource;


public class FeedbackLoop {
	private static List<AbstractAdaptationModule> modules = new ArrayList<>();
	private static List<Boolean> activation = new ArrayList<>();
	private static ContextRobLANG context = null;
	private static long time = 0;
	
	public static void registerModule(AbstractAdaptationModule module) {
		modules.add(module);
		activation.add(false);
		context.registerModule(module);
	}
	
	public static void reset() {
		modules = new ArrayList<>();
		activation = new ArrayList<>();
		time = 0;
	}
	
	public static void setContext(ContextRobLANG ctx) {
		context = ctx;
	}
	
	public static final boolean updateBefore(EObject subjectNode, Object[] args) {
		if(loopTrigger(subjectNode)) {
			loop();
		}
		boolean proceed = true;
		for (int i = 0; i < modules.size(); i++) {
			if(activation.get(i)) {
				AbstractAdaptationModule m = modules.get(i);
				Map<String, Bounds> softgoalsBounds = context.getBounds();
				if(!m.isTargetedNode(subjectNode)) continue;
				if(!m.fitInBounds(softgoalsBounds)) continue;
				if(m.callStrategy() == Strategy.BEFORE) {
					m.updateBefore(subjectNode, args);
				}
				if(m.callStrategy() == Strategy.AROUND) {
					proceed = m.updateBefore(subjectNode, args) && proceed;
				}
			}
		}
		return proceed;
	}
	
	public static final Object updateAfter(EObject subjectNode, Object[] args, Object returned) {
		Object out = returned;
		for (int i = 0; i < modules.size(); i++) {
			if(activation.get(i)) {
				AbstractAdaptationModule m = modules.get(i);
				if(!m.isTargetedNode(subjectNode)) continue;
				if(m.callStrategy() != Strategy.BEFORE) {
					if(m.updateAfter(subjectNode, args, returned)) {
						out = m.byPassResult();
					}
				}
			}
		}
		return out;
	}
	
	public final static boolean loopTrigger(EObject subjectNode) {
		long delta = System.currentTimeMillis()-time;
		if(delta > 3000) {
			time = System.currentTimeMillis();
			return true;
		} else {
			return false;
		}
	}
	
	public final static void loop() {
		System.out.println("MAPE-K LOOP");
		monitor();
		analyze();
		plan();
		execute();
	}
	
	private final static void monitor() {
		context.setUserConfig(context.loadUserConfig());
		List<Resource> resources = context.getResources();
		for (Resource resource : resources) {
			if (resource.ID.equals("Battery")) {
				resource.setMonitoredValue(context.getBattery() / 100);
			} else {
				resource.setMonitoredValue(0);
			}
		}
	}
	
	private final static void analyze() {
		Goal tradeOff = context.getGoal();
		tradeOff.cleanModel();
		Map<String, Double> userConfig = context.getUserConfig();
		Set<String> IDS = userConfig.keySet();
		
		for (AbstractAdaptationModule module : modules) {
			for (String id : IDS) {
				module.getModuleTradeOff().updateLink(id, userConfig.get(id));
			}
		}
		
		context.getGoal().assessVariables();
	}
	
	private final static void plan   () {
		activation = new ArrayList<>();
		for (AbstractAdaptationModule module : modules) {
			if (module != null && module.getModuleTradeOff() != null) {
				activation.add(module.getModuleTradeOff().execute());
			} else {
				activation.add(true);
			}
		}
	}
	
	private final static void execute() {
		for (int i = 0; i < modules.size(); i++) {
			modules.get(i).setEnabled(activation.get(i));
		}
	}

	public static AdaptationContext getAdaptationContext() {
		return context;
	}

}
