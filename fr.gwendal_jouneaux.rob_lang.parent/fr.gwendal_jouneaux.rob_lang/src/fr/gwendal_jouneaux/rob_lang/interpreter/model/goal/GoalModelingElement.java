package fr.gwendal_jouneaux.rob_lang.interpreter.model.goal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.gwendal_jouneaux.rob_lang.interpreter.FeedbackLoop;
import fr.gwendal_jouneaux.rob_lang.interpreter.decision.model.ModelEvaluationBounds;
import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.TagConstraint.TAG;
import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.visitor.FlattenedImpact;
import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.visitor.IGoalVisitor;

import java.util.Set;

public abstract class GoalModelingElement {
	
	final public String ID;
	
	private Map<GoalModelingElement, Double> inputLinks;
	private Set<TagConstraint> tags;
	private Map<GoalModelingElement, FlattenedImpact> flattenedImpact;
	private ModelEvaluationBounds bounds;
	
	public abstract <T> T accept(IGoalVisitor<T> visitor);

	public GoalModelingElement(String ID) {
		inputLinks = new HashMap<GoalModelingElement, Double>();
		tags = new HashSet<>();
		this.ID = ID;
	}
	
	public void addContribution(GoalModelingElement elem, double impact) {
		getInputLinks().put(elem, impact);
	}
	
	public void addTag(Resource elem, TAG tag, double value) {
		tags.add(new TagConstraint(elem, tag, value));
	}
		
	public boolean verifyTags() {
		for (TagConstraint tc : tags) {
			if(! tc.isValid()) {
				return false;
			}
		}
		return true;
	}
	
	protected ModelEvaluationBounds calculateBounds(){
		if (getBounds() != null) {
			return getBounds();
		}
		
		Set<GoalModelingElement> children = getInputLinks().keySet();
		List<ModelEvaluationBounds> allBounds = new ArrayList();
		List<Double> weights = new ArrayList();
		for (GoalModelingElement element : children) {
			if(element == null) continue;
			
			ModelEvaluationBounds bounds = element.calculateBounds();
			allBounds.add(bounds);
			weights.add(getInputLinks().get(element));
		}
		this.setBounds(ModelEvaluationBounds.merge(allBounds, weights));
		return this.getBounds();
	}
	
	public Map<GoalModelingElement, Double> getInputLinks() {
		return inputLinks;
	}

	public Set<TagConstraint> getTags() {
		return tags;
	}

	public ModelEvaluationBounds getBounds() {
		return bounds;
	}

	public void setBounds(ModelEvaluationBounds bounds) {
		this.bounds = bounds;
	}
	
	public void setFlattenedImpact(Map<GoalModelingElement, FlattenedImpact> flattenedImpact) {
		this.flattenedImpact = flattenedImpact;
	}
	
	public Map<GoalModelingElement, FlattenedImpact> getFlattenedImpact() {
		return flattenedImpact;
	}

}
