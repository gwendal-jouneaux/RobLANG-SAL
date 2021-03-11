package fr.gwendal_jouneaux.rob_lang.interpreter.model.goal;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.gwendal_jouneaux.rob_lang.interpreter.decision.model.ModelEvaluationBounds;
import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.visitor.IGoalVisitor;

public class Goal extends GoalModelingElement {

	public Goal(String ID) {
		super(ID);
	}
	
	public final void updateLink(String softgoalID, double newValue) {
		Set<GoalModelingElement> softs = getInputLinks().keySet();
		for (GoalModelingElement soft : softs) {
			if(soft.ID.equals(softgoalID)) {
				getInputLinks().put(soft, newValue);
				break;
			}
		}
	}
	
	public Map<String, ModelEvaluationBounds> getSoftgoalBounds(){
		
		Set<GoalModelingElement> softs = getInputLinks().keySet();
		Map<String, ModelEvaluationBounds> out = new HashMap();
		for (GoalModelingElement soft : softs) {
			out.put(soft.ID, soft.getBounds());
		}
		return out;
	}

	@Override
	public <T> T accept(IGoalVisitor<T> visitor) {
		return visitor.visitGoal(this);
	}

}
