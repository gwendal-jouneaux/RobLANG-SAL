package fr.gwendal_jouneaux.rob_lang.interpreter.adaptation.modules;

import org.eclipse.emf.ecore.EObject;

import fr.gwendal_jouneaux.rob_lang.interpreter.AbstractAdaptationModule;
import fr.gwendal_jouneaux.rob_lang.interpreter.AdaptationContext;
import fr.gwendal_jouneaux.rob_lang.interpreter.ContextRobLANG;
import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.Resource;
import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.Softgoal;
import fr.gwendal_jouneaux.rob_lang.robLANG.Turn;
import fr.gwendal_jouneaux.rob_lang.robLANG.TurnLeft;
import fr.gwendal_jouneaux.rob_lang.robLANG.TurnRight;

public class RotationAccuracyModule extends AbstractAdaptationModule {
	
	public RotationAccuracyModule() {
		super("RotationAccuracyModule");
	}

	private double angle;
	private double askedAngle;

	@Override
	public boolean updateBefore(EObject node, Object[] args) {
		System.out.println("-----Update before-----");
		System.out.println(node);
		System.out.println(node.eContainer());
		System.out.println("----------");
		if(node instanceof Turn) {
			ContextRobLANG ctx = (ContextRobLANG) args[1];
			angle = ctx.getCompass();
		}
		return true;
	}

	@Override
	public boolean updateAfter(EObject node, Object[] args, Object returned) {
		System.out.println("-----UpdateAfter-----");
		System.out.println(node);
		System.out.println(node.eContainer());
		System.out.println("----------");
		if(node instanceof Turn) {
			ContextRobLANG ctx = (ContextRobLANG) args[1];
			double newangle = ctx.getCompass();
			if(node instanceof TurnLeft) {
				double delta = angle - newangle;
				if(delta<0) delta = delta + 360;
				if(delta != 0) {
					double precision = (double) askedAngle / (double) delta;
					int toDo = (int) (precision * (askedAngle - delta));
					ctx.turnRobot(toDo+1);
				}
			}
			if(node instanceof TurnRight) {
				double delta = newangle - angle;
				if(delta<0) delta = delta + 360;
				if(delta != 0) {
					double precision = (double) askedAngle / (double) delta;
					int toDo = (int) (precision * (askedAngle - delta));
					ctx.turnRobot(-toDo-1);
				}
			}
		} else {
			askedAngle = (Integer) returned;
		}
		
		return false;
	}

	@Override
	public Object byPassResult() {
		return null;
	}

	@Override
	public boolean isTargetedNode(EObject node) {
		System.out.println("---- isTarget ------");
		System.out.println(node);
		System.out.println(node.eContainer());
		
		boolean out = node instanceof Turn || node.eContainer() instanceof Turn;
		System.out.println("enter = " + out);
		System.out.println("----------");
		return out;
	}

	@Override
	public Strategy callStrategy() {
		return Strategy.AROUND;
	}

	@Override
	public void init(AdaptationContext adaptationContext) {
		
	}

	@Override
	public void connectSoftGoal(Softgoal softgoal) {	
	}

	@Override
	public void connectResource(Resource resource) {
		
	}

}
