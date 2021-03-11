package fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.visitor;

import java.util.Comparator;
import java.util.Map;

import fr.gwendal_jouneaux.rob_lang.interpreter.decision.model.VariableConfiguation;

public class VariableConfigurationComparator implements Comparator<VariableConfiguation> {
	
	private Map<VariableConfiguation, Double> confEvaluation;

	public VariableConfigurationComparator(Map<VariableConfiguation, Double> confEvaluation) {
		this.confEvaluation = confEvaluation;
	}

	@Override
	public int compare(VariableConfiguation a, VariableConfiguation b) {
        if (confEvaluation.get(a) >= confEvaluation.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }

}
