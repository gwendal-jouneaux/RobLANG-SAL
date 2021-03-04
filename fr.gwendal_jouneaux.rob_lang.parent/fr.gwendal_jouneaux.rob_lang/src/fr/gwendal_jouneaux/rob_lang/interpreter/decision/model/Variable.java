package fr.gwendal_jouneaux.rob_lang.interpreter.decision.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.gwendal_jouneaux.rob_lang.interpreter.decision.model.Variable.EvaluatedState;

public final class Variable extends ModelingElement {
	
	private double globalImpact;
	private double lowerBound;
	private double higherBound;
	private double equilibrium;
	private EvaluatedState evaluatedState;
	
	public enum EvaluatedState {LOWER, EQUILIBRIUM, HIGHER}

	public Variable(String ID) {
		super(ID);
		this.lowerBound  = -1;
		this.higherBound =  1;
		this.equilibrium =  0;
		this.globalImpact = 0;
	}
	
	public Variable(String ID, double equilibrium) {
		super(ID);
		this.lowerBound  = -1;
		this.higherBound =  1;
		this.equilibrium =  equilibrium;
		this.globalImpact = 0;
	}
	
	public Variable(String ID, double equilibrium, double lowerBound) {
		super(ID);
		this.lowerBound  = lowerBound;
		this.higherBound = 1;
		this.equilibrium = equilibrium;
		this.globalImpact = 0;
	}
	
	public Variable(String ID,double equilibrium,  double lowerBound, double higherBound) {
		super(ID);
		this.lowerBound  = lowerBound;
		this.higherBound = higherBound;
		this.equilibrium = equilibrium;
		this.globalImpact = 0;
	}
	
	@Override
	protected Set<Variable> assessVariables(Double pathImpact) {
		if(verifyTags()) {
			this.globalImpact += pathImpact;
		} else {
		    this.globalImpact = 0;
		}
		
		Set<Variable> out = new HashSet<Variable>();
		out.add(this);
		return out;
	}
	
	@Override
	protected Double evaluate() {
		if (value != null) {
			return value;
		}
		
		if (globalImpact == 0) {
			value = equilibrium;
		} else {
			value = globalImpact > 0 ? higherBound : lowerBound;
		}
		return value;
	}
	
	@Override
	public void addContribution(ModelingElement elem, double impact){
		System.err.println("WARNING : Add contribution to a variable is unnecessary");
	}
	
	public double value() {
		return value == null ? equilibrium : value;
	}

}
