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

public final class Variable extends GoalModelingElement {
	
	private double globalImpact;
	private double lowerBound;
	private double higherBound;
	private double equilibrium;
	
	

	public Variable(String ID) {
		super(ID);
		this.setLowerBound(-1);
		this.setHigherBound(1);
		this.setEquilibrium(0);
		this.setGlobalImpact(0);
	}
	
	public Variable(String ID, double equilibrium) {
		super(ID);
		this.setLowerBound(-1);
		this.setHigherBound(1);
		this.setEquilibrium(equilibrium);
		this.setGlobalImpact(0);
	}
	
	public Variable(String ID, double equilibrium, double lowerBound) {
		super(ID);
		this.setLowerBound(lowerBound);
		this.setHigherBound(1);
		this.setEquilibrium(equilibrium);
		this.setGlobalImpact(0);
	}
	
	public Variable(String ID,double equilibrium,  double lowerBound, double higherBound) {
		super(ID);
		this.setLowerBound(lowerBound);
		this.setHigherBound(higherBound);
		this.setEquilibrium(equilibrium);
		this.setGlobalImpact(0);
	}
	
	@Override
	public <T> T accept(IGoalVisitor<T> visitor) {
		return visitor.visitVariable(this);
	}
	
	@Override
	public void addContribution(GoalModelingElement elem, double impact){
		System.err.println("WARNING : Add contribution to a variable is unnecessary");
	}
	
	public double value() {
		return getValue() == null ? getEquilibrium() : getValue();
	}
	
	protected ModelEvaluationBounds calculateBounds(){
		if (getBounds() != null) {
			return getBounds();
		}
		
		Map<VariableConfiguation, Bounds> valuesByConfiguration = new HashMap();
		
		Bounds lowBounds = new Bounds(getLowerBound(),getLowerBound());
		Bounds eqBounds = new Bounds(getEquilibrium(), getEquilibrium());
		Bounds highBounds = new Bounds(getHigherBound(), getHigherBound());
		
		VariableConfiguation variableConfiguationLow  = new VariableConfiguation(new HashMap());
		VariableConfiguation variableConfiguationEqui = new VariableConfiguation(new HashMap());
		VariableConfiguation variableConfiguationHigh = new VariableConfiguation(new HashMap());
		
		 variableConfiguationLow.addVarState(this, EvaluatedState.LOWER);
		variableConfiguationEqui.addVarState(this, EvaluatedState.EQUILIBRIUM);
		variableConfiguationHigh.addVarState(this, EvaluatedState.HIGHER);
		
		valuesByConfiguration.put(variableConfiguationLow,  lowBounds);
		valuesByConfiguration.put(variableConfiguationEqui, eqBounds);
		valuesByConfiguration.put(variableConfiguationHigh, highBounds);
		
		setBounds(new ModelEvaluationBounds(valuesByConfiguration));
		return this.getBounds();
	}

	public double getGlobalImpact() {
		return globalImpact;
	}

	public void setGlobalImpact(double globalImpact) {
		this.globalImpact = globalImpact;
	}

	public double getEquilibrium() {
		return equilibrium;
	}

	public void setEquilibrium(double equilibrium) {
		this.equilibrium = equilibrium;
	}

	public double getHigherBound() {
		return higherBound;
	}

	public void setHigherBound(double higherBound) {
		this.higherBound = higherBound;
	}

	public double getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(double lowerBound) {
		this.lowerBound = lowerBound;
	}

}
