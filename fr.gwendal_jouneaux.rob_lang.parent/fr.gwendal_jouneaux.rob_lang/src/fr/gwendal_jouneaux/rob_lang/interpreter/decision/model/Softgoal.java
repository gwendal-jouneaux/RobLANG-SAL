package fr.gwendal_jouneaux.rob_lang.interpreter.decision.model;

import java.util.HashMap;

public class Softgoal extends ModelingElement {

	public Softgoal(String ID) {
		super(ID);
	}
	
	public final void detach() {
		inputLinks = new HashMap<ModelingElement, Double>();
	}

}
