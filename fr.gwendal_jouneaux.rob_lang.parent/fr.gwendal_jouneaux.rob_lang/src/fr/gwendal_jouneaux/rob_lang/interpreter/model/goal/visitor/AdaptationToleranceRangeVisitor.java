package fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.visitor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import fr.gwendal_jouneaux.rob_lang.interpreter.decision.model.Bounds;
import fr.gwendal_jouneaux.rob_lang.interpreter.decision.model.VariableConfiguation;
import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.Goal;
import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.GoalModelingElement;
import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.Resource;
import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.Softgoal;
import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.Task;
import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.Variable;

public class AdaptationToleranceRangeVisitor implements IGoalVisitor<Set<Variable>> {
	
	private Map<Softgoal, Bounds> softgoalsAdaptationBounds;

	public AdaptationToleranceRangeVisitor(Map<Softgoal, Bounds> softgoalsAdaptationBounds) {
		this.softgoalsAdaptationBounds = softgoalsAdaptationBounds;
	}

	@Override
	public Set<Variable> visitGoal(Goal goal) {
		if(goal.getFlattenedImpact() != null) {
			return visitFlattened(goal);
		}
		
		if(! goal.verifyTags()) return new HashSet();
		
		// get active variables
		Set<Variable> out = new HashSet();
		Set<GoalModelingElement> children = goal.getInputLinks().keySet();
		for (GoalModelingElement element : children) {
			if(element == null) continue;
			out.addAll(element.accept(this));
		}
		
		//Evaluate configuration fitness
		Set<VariableConfiguation> confs = VariableConfiguation.generateConfigurations(out);
		TreeMap<VariableConfiguation, Double> sortedConfs = fitness(confs, goal);
		
		while(criteria(sortedConfs)) {
			List<VariableConfiguation> toCrossover = selection(sortedConfs);
			confs = crossover(toCrossover);
			confs.addAll(sortedConfs.keySet());
			mutation(confs);
			sortedConfs = fitness(confs, goal);
		}
		
		
		
		return new HashSet();
	}

	@Override
	public Set<Variable> visitSoftGoal(Softgoal softgoal) {
		if(softgoal.getFlattenedImpact() != null) {
			return visitFlattened(softgoal);
		}
		
		if(! softgoal.verifyTags()) return new HashSet();
		
		Set<Variable> out = new HashSet();
		Set<GoalModelingElement> children = softgoal.getInputLinks().keySet();
		for (GoalModelingElement element : children) {
			if(element == null) continue;
			out.addAll(element.accept(this));
		}

		return out;
	}

	@Override
	public Set<Variable> visitTask(Task task) {
		if(task.getFlattenedImpact() != null) {
			return visitFlattened(task);
		}
		return new HashSet();
	}

	@Override
	public Set<Variable> visitVariable(Variable variable) {
		Set<Variable> out = new HashSet();
		if(! variable.verifyTags()) return out;
		out.add(variable);
		return out;
	}

	@Override
	public Set<Variable> visitResource(Resource resource) {
		return new HashSet();
	}
	
	private Set<Variable> visitFlattened(GoalModelingElement elem) {
		Map<GoalModelingElement, FlattenedImpact> flattenedImpacts = elem.getFlattenedImpact();
		Set<Variable> out = new HashSet();
		for (GoalModelingElement element : flattenedImpacts.keySet()) {
			FlattenedImpact fi = flattenedImpacts.get(element);
			double weight = fi.impactForValue(1);
			if(weight != 0) out.addAll(element.accept(this));
		}
		return out;
	}
	
	private TreeMap<VariableConfiguation, Double> fitness(Set<VariableConfiguation> population, Goal goal){
		Map<VariableConfiguation, Double> confEvaluation = new HashMap<>();
		for (VariableConfiguation variableConfiguation : population) {
			confEvaluation.put(variableConfiguation, evaluateConf(variableConfiguation, goal));
		}
		
		TreeMap<VariableConfiguation, Double> sortedConfs =
				confEvaluation.entrySet().stream()
			       .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
			       .collect(Collectors.toMap(
			          Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, TreeMap::new));
		
		return sortedConfs;
	}
	
	private List<VariableConfiguation> selection(TreeMap<VariableConfiguation, Double> evaluatedPopulation){
		int nbToRemove = evaluatedPopulation.size()/10;
		if(nbToRemove % 2 == 1) nbToRemove++;
//		TreeMap<VariableConfiguation, Double> sortedConfs =
//				evaluatedPopulation.entrySet().stream()
//			       .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
//			       .limit(2* nbToRemove)
//			       .collect(Collectors.toList(Map.Entry::getKey, (e1) -> e1, ArrayList::new));
		return null;
		
	}
	
	private Set<VariableConfiguation> crossover(List<VariableConfiguation> fittests){
		return null;
		
	}
	
	private void mutation(Set<VariableConfiguation> population){
		
	}

	private boolean criteria(TreeMap<VariableConfiguation, Double> evaluatedPopulation){
		return false;
		
	}
	
	private double evaluateConf(VariableConfiguation conf, Goal goal) {
		ImpactEvaluationVisitor evalWithConfiguration = new ImpactEvaluationVisitor(conf.getConfiguration());
		double confValue = goal.accept(evalWithConfiguration);
		Map<GoalModelingElement, Double> values = evalWithConfiguration.getValues();
		for (Softgoal soft : softgoalsAdaptationBounds.keySet()) {
			Bounds tolerance = softgoalsAdaptationBounds.get(soft);
			boolean valid = tolerance.contains(values.get(soft));
			if(! valid) {
				confValue += -0.5;
			}
		}
		return confValue;
	}

}
