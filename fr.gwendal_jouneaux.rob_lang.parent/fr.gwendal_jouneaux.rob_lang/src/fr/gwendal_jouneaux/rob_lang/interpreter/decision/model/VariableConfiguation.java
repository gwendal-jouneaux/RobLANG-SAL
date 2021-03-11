package fr.gwendal_jouneaux.rob_lang.interpreter.decision.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.GoalModelingElement;
import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.Variable;

public class VariableConfiguation{
	
	public static Set<VariableConfiguation> CONFIGURATION_SPACE;
	
	Map<Variable, Double> conf;
	
	public VariableConfiguation(Map<Variable, Double> conf) {
		this.conf = conf;
	}
	
	public Set<Variable> getVariables(){
		return conf.keySet();
	}
	
	public void addVarState(Variable var, Double state) {
		conf.put(var, state);
	}
	
	public void generateVarState(Variable var) {
		conf.put(var, randomValueFor(var));
	}
	
	public void addVarStateIfNotPresent(Variable var, Double state) {
		if (! conf.containsKey(var)) {
			conf.put(var, state);
		}
	}
	
	
	public static VariableConfiguation crossover(VariableConfiguation first, VariableConfiguation second) {
		Map<Variable, Double> newConf = new HashMap();
		for (Variable variable : first.conf.keySet()) {
			if(Math.random() > 0.5) {
				newConf.put(variable, first.conf.get(variable));
			} else {
				newConf.put(variable, second.conf.get(variable));
			}
		}
		return new VariableConfiguation(newConf);
	}
	
	public void mutate() {
		for (Variable variable : conf.keySet()) {
			if(Math.random() < 0.05 * (1.0/conf.size())) {
				generateVarState(variable);
			}
		}
	}
	
	
	private double randomValueFor(Variable var) {
		return var.getLowerBound() + Math.random()*(var.getHigherBound() - var.getLowerBound());
	}
	
	private boolean configurationFor(Set<Variable> neededVars) {
		Set<Variable> configVars = this.getVariables();
		if(neededVars.size() != configVars.size()) return false;
		
		for (Variable variable : configVars) {
			if(! neededVars.contains(variable)) return false;
		}
		return true;
	}
	
	public VariableConfiguation copy() {
		return new VariableConfiguation(new HashMap(conf));
	}
	
	public Map<GoalModelingElement, Double> getConfiguration(){
		return new HashMap<GoalModelingElement, Double>(conf);
	}
	
	
	public boolean correspond(VariableConfiguation conf) {
		VariableConfiguation smallest = conf.conf.size() <  this.conf.size() ? conf : this;
		VariableConfiguation other    = conf.conf.size() >= this.conf.size() ? conf : this;
		
		for (Variable var : smallest.getVariables()) {
			if(smallest.conf.get(var) != other.conf.get(var)) {
				return false;
			}
		}
		return true;
	}
	
	public static Set<VariableConfiguation> generateConfigurations(Set<Variable> vars){
		if(CONFIGURATION_SPACE != null && ! CONFIGURATION_SPACE.isEmpty() && CONFIGURATION_SPACE.iterator().next().configurationFor(vars)) return CONFIGURATION_SPACE;
		
		Set<VariableConfiguation> out = new HashSet();
		if(vars.isEmpty()) {
			out.add(new VariableConfiguation(new HashMap()));
			return out;
		}
		
		for (Variable variable : vars) {
			VariableConfiguation low = new VariableConfiguation(new HashMap());
			VariableConfiguation high = new VariableConfiguation(new HashMap());
			low.addVarState(variable, variable.getLowerBound());
			high.addVarState(variable, variable.getHigherBound());
			for (Variable other : vars) {
				if(variable != other) {
					low.generateVarState(other);
					high.generateVarState(other);
				}
			}
			out.add(low);
			out.add(high);
		}
		CONFIGURATION_SPACE = out;
		return out;
	}

	
}
