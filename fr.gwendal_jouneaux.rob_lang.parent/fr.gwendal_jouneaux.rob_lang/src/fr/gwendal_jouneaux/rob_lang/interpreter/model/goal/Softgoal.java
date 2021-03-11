package fr.gwendal_jouneaux.rob_lang.interpreter.model.goal;

import java.util.Map;

import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.visitor.FlattenedImpact;
import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.visitor.IGoalVisitor;

public class Softgoal extends GoalModelingElement {
	
	

	public Softgoal(String ID) {
		super(ID);
	}	

	@Override
	public <T> T accept(IGoalVisitor<T> visitor) {
		return visitor.visitSoftGoal(this);
	}

}
