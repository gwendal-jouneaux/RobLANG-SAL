package fr.gwendal_jouneaux.rob_lang.interpreter.model.goal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.gwendal_jouneaux.rob_lang.interpreter.decision.model.Bounds;
import fr.gwendal_jouneaux.rob_lang.interpreter.decision.model.ModelEvaluationBounds;
import fr.gwendal_jouneaux.rob_lang.interpreter.decision.model.VariableConfiguation;
import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.Variable.EvaluatedState;
import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.visitor.IGoalVisitor;

public final class Resource extends GoalModelingElement {
	
	Double value;

	public Resource(String ID) {
		super(ID);
	}
	
	public void setMonitoredValue(double val) {
		value = val;
	}
	
	public double monitor() {
		return value;
	}
	
	@Override
	public void addContribution(GoalModelingElement elem, double impact){
		System.err.println("WARNING : Add contribution to a resource is unnecessary");
	}
	
	
	protected ModelEvaluationBounds calculateBounds(){
		if (getBounds() != null) {
			return getBounds();
		}
		
		Map<VariableConfiguation, Bounds> valuesByConfiguration = new HashMap();
		Bounds bound = new Bounds(0, 1);
		VariableConfiguation variableConfiguation  = new VariableConfiguation(new HashMap());
		valuesByConfiguration.put(variableConfiguation,  bound);
		
		setBounds(new ModelEvaluationBounds(valuesByConfiguration));
		return this.getBounds();
	}

	@Override
	public <T> T accept(IGoalVisitor<T> visitor) {
		return visitor.visitResource(this);
	}

}
