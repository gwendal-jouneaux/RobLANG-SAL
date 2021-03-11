package fr.gwendal_jouneaux.rob_lang.interpreter.model.goal;

public class TagConstraint {
	
	public enum TAG {SUPERIOR, INFERIOR, EQUAL, NOTEQUAL}

	Resource elem;
	TAG tag;
	Double value;
	public TagConstraint(Resource elem, TAG tag, Double value) {
		this.elem = elem;
		this.tag = tag;
		this.value = value;
	}
	
	public boolean isValid() {
		switch(tag) {
		case SUPERIOR:
			return elem.monitor() > value;
		case INFERIOR:
			return elem.monitor() < value;
		case NOTEQUAL:
			return elem.monitor() != value;
		case EQUAL:
			return elem.monitor() == value;
		}
		return false;
	}

}
