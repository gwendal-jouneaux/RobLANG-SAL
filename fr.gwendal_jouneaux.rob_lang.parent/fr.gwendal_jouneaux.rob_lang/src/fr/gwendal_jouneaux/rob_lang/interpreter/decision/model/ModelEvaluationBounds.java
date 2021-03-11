package fr.gwendal_jouneaux.rob_lang.interpreter.decision.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.Variable;

public class ModelEvaluationBounds {
	
	protected Map<VariableConfiguation, Bounds> valuesByConfiguration;
	protected Set<Variable> vars;
	
	public ModelEvaluationBounds(Map<VariableConfiguation, Bounds> valuesByConfiguration) {
		this.valuesByConfiguration = valuesByConfiguration;
		this.vars = valuesByConfiguration.keySet().iterator().next().getVariables();
	}
	
	public Set<Variable> getVariables(){
		return vars;
	}
	
	public Bounds boundsFor(VariableConfiguation conf){
		Set<VariableConfiguation> configs = valuesByConfiguration.keySet();
		for (VariableConfiguation variableConfiguation : configs) {
			if(variableConfiguation.correspond(conf)) {
				return valuesByConfiguration.get(variableConfiguation);
			}
		}
		return new Bounds();
	}
	
	public static ModelEvaluationBounds merge(List<ModelEvaluationBounds> childsBounds, List<Double> weight) {
		Set<Variable> newVars = new HashSet();
		for (ModelEvaluationBounds bounds : childsBounds) {
			newVars.addAll(bounds.getVariables());
		}
		Set<VariableConfiguation> configurations = VariableConfiguation.generateConfigurations(newVars);
		Map<VariableConfiguation, Bounds> valuesByConfiguration = new HashMap();
		for (VariableConfiguation variableConfiguation : configurations) {
			Bounds bounds = new Bounds();
			for (int i = 0; i < childsBounds.size(); i++) {
				bounds.add(childsBounds.get(i).boundsFor(variableConfiguation).factor(weight.get(i)));
			}
			valuesByConfiguration.put(variableConfiguation, bounds);
		}
		return new ModelEvaluationBounds(valuesByConfiguration);
	}
}
