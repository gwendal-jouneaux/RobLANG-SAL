package fr.gwendal_jouneaux.rob_lang.interpreter.decision.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.gwendal_jouneaux.rob_lang.interpreter.FeedbackLoop;

import java.util.Set;

public abstract class ModelingElement {
	
	final public String ID;
	
	protected Map<ModelingElement, Double> inputLinks;
	protected Map<TAG, Map<Resource, Double>> tags;
	protected Double value;
	
	public enum TAG {SUPERIOR, INFERIOR, EQUAL, NOTEQUAL}

	public ModelingElement(String ID) {
		inputLinks = new HashMap<ModelingElement, Double>();
		tags = new HashMap<TAG, Map<Resource, Double>>();
		for (TAG tag : TAG.values()) {
			tags.put(tag, new HashMap<Resource, Double>());
		}
		value = null;
		this.ID = ID;
	}
	
	protected void clean() {
		value = null;
		Set<ModelingElement> elems = inputLinks.keySet();
		for (ModelingElement modelingElement : elems) {
			if(modelingElement != null)	modelingElement.clean();
		}
	}
	
	protected Set<Variable> assessVariables(Double pathImpact) {
		Set<Variable> out = new HashSet<Variable>();
		if(! verifyTags()) return out;
		
		Set<ModelingElement> children = inputLinks.keySet();
		for (ModelingElement element : children) {
			if(element != null)	out.addAll(element.assessVariables(pathImpact * inputLinks.get(element)));
		}
		return out;
	}
	
	protected Double evaluate() {
		if(! verifyTags()) return 0.0;
		if (value != null) {
			return value;
		}
		
		boolean computable= true;
		value = 0.0;
		Set<ModelingElement> children = inputLinks.keySet();
		for (ModelingElement element : children) {
			if(element == null) continue;
			Double val = element.evaluate();
			if (val == null) {
				computable = false;
			} else {
				value += val * inputLinks.get(element);
			}
		}
		if(!computable) value = null;
		return Math.min(100, Math.max(value, -100));
	}
	
	protected boolean verifyTags() {
		Map<Resource, Double> inferior = tags.get(TAG.INFERIOR);
		Map<Resource, Double> superior = tags.get(TAG.SUPERIOR);
		Map<Resource, Double> equal    = tags.get(TAG.EQUAL);
		Map<Resource, Double> notequal = tags.get(TAG.NOTEQUAL);
		
		for (Entry<Resource, Double> entry : inferior.entrySet()) {
			if(entry.getKey().monitor() >= entry.getValue().doubleValue()) {
				return false;
			}
		}
		for (Entry<Resource, Double> entry : superior.entrySet()) {
			if(entry.getKey().monitor() <= entry.getValue().doubleValue()) {
				return false;
			}
		}
		for (Entry<Resource, Double> entry : equal.entrySet()) {
			if(entry.getKey().monitor() != entry.getValue().doubleValue()) {
				return false;
			}
		}
		for (Entry<Resource, Double> entry : notequal.entrySet()) {
			if(entry.getKey().monitor() == entry.getValue().doubleValue()) {
				return false;
			}
		}
		return true;
	}
	
	public void addContribution(ModelingElement elem, double impact) {
		inputLinks.put(elem, impact);
	}
	
	public void addTag(Resource elem, TAG tag, double value) {
		tags.get(tag).put(elem, value);
	}

}
