package fr.gwendal_jouneaux.rob_lang.interpreter.adaptation.modules;

import org.eclipse.emf.ecore.EObject;

import fr.gwendal_jouneaux.rob_lang.interpreter.AbstractAdaptationModule;
import fr.gwendal_jouneaux.rob_lang.interpreter.AdaptationContext;
import fr.gwendal_jouneaux.rob_lang.interpreter.ContextRobLANG;
import fr.gwendal_jouneaux.rob_lang.interpreter.InterpretRobLANG;
import fr.gwendal_jouneaux.rob_lang.interpreter.decision.model.ModelingElement.TAG;
import fr.gwendal_jouneaux.rob_lang.interpreter.decision.model.Resource;
import fr.gwendal_jouneaux.rob_lang.interpreter.decision.model.Softgoal;
import fr.gwendal_jouneaux.rob_lang.interpreter.decision.model.Variable;
import fr.gwendal_jouneaux.rob_lang.robLANG.Expression;
import fr.gwendal_jouneaux.rob_lang.robLANG.Move;
import fr.gwendal_jouneaux.rob_lang.robLANG.MoveBackward;
import fr.gwendal_jouneaux.rob_lang.robLANG.MoveForward;
import fr.gwendal_jouneaux.rob_lang.robLANG.Turn;
import fr.gwendal_jouneaux.rob_lang.robLANG.TurnLeft;
import fr.gwendal_jouneaux.rob_lang.robLANG.TurnRight;

public class BatteryOptimization extends AbstractAdaptationModule {
	
	private Variable smallSpeedReduction;
	private Variable bigSpeedReduction;
	
	private InterpretRobLANG interpreter;

	public BatteryOptimization() {
		super("Battery Optimization");
		interpreter = new InterpretRobLANG();
	}

	@Override
	public boolean updateBefore(EObject node, Object[] args) {
		System.out.println("module start");
		ContextRobLANG ctx = (ContextRobLANG) args[1];
		double prevSpeed = ctx.getSpeed();
		double small = smallSpeedReduction.value();
		double big = bigSpeedReduction.value();
		System.out.println("Small : " + small + "\tBig : " + big);
		if(big > 0) {
			ctx.setSpeed(prevSpeed*0.5);
			if(node instanceof MoveForward) {
				MoveForward n = (MoveForward) node;
				Expression e = n.getDuration();
				int duration = (int) interpreter.interpret(e, ctx);
				int realDuration = (int) (duration / 0.5);
				ctx.moveRobot(realDuration);
				
			} else if(node instanceof MoveBackward) {
				MoveBackward n = (MoveBackward) node;
				Expression e = n.getDuration();
				int duration = (int) interpreter.interpret(e, ctx);
				int realDuration = (int) (duration / 0.5);
				ctx.moveRobot(-realDuration);
				
			} else if(node instanceof TurnLeft) {
				TurnLeft n = (TurnLeft) node;
				Expression e = n.getDuration();
				int duration = (int) interpreter.interpret(e, ctx);
				int realDuration = (int) (duration / 0.5);
				ctx.turnRobot(realDuration);
				
			} else if(node instanceof TurnRight) {
				TurnRight n = (TurnRight) node;
				Expression e = n.getDuration();
				int duration = (int) interpreter.interpret(e, ctx);
				int realDuration = (int) (duration / 0.5);
				ctx.turnRobot(- realDuration);
				
			}
			ctx.setSpeed(prevSpeed);
		} else if(small > 0) {
			ctx.setSpeed(prevSpeed*0.9);
			if(node instanceof MoveForward) {
				MoveForward n = (MoveForward) node;
				Expression e = n.getDuration();
				int duration = (int) interpreter.interpret(e, ctx);
				int realDuration = (int) (duration / 0.9);
				ctx.moveRobot(realDuration);
				
			} else if(node instanceof MoveBackward) {
				MoveBackward n = (MoveBackward) node;
				Expression e = n.getDuration();
				int duration = (int) interpreter.interpret(e, ctx);
				int realDuration = (int) (duration / 0.9);
				ctx.moveRobot(-realDuration);
				
			} else if(node instanceof TurnLeft) {
				TurnLeft n = (TurnLeft) node;
				Expression e = n.getDuration();
				int duration = (int) interpreter.interpret(e, ctx);
				int realDuration = (int) (duration / 0.9);
				ctx.turnRobot(realDuration);
				
			} else if(node instanceof TurnRight) {
				TurnRight n = (TurnRight) node;
				Expression e = n.getDuration();
				int duration = (int) interpreter.interpret(e, ctx);
				int realDuration = (int) (duration / 0.9);
				ctx.turnRobot(- realDuration);
				
			}
			ctx.setSpeed(prevSpeed);
		}
		System.out.println("module end");
		return true;
	}

	@Override
	public boolean updateAfter(EObject node, Object[] args, Object returned) {
		return false;
	}

	@Override
	public Object byPassResult() {
		return null;
	}

	@Override
	public boolean isTargetedNode(EObject node) {
		return node instanceof Move || node instanceof Turn;
	}

	@Override
	public Strategy callStrategy() {
		return Strategy.BEFORE;
	}

	@Override
	public void init(AdaptationContext adaptationContext) {
		smallSpeedReduction = new Variable("Small speed reduction", 0, 0);
		bigSpeedReduction = new Variable("big speed reduction", 0, 0);
	}

	@Override
	public void connectSoftGoal(Softgoal softgoal) {
		if (softgoal.ID.equals("Time")) {
			softgoal.addContribution(smallSpeedReduction, -0.5);
			softgoal.addContribution(bigSpeedReduction, -0.25);
		}
		if (softgoal.ID.equals("Energy")) {
			softgoal.addContribution(smallSpeedReduction, 0.5);
			softgoal.addContribution(bigSpeedReduction, 0.25);
		}
	}

	@Override
	public void connectResource(Resource resource) {
		if(resource.ID.equals("Battery")) {
			smallSpeedReduction.addTag(resource, TAG.INFERIOR, 0.5);
			bigSpeedReduction.addTag(resource, TAG.INFERIOR, 0.5);
		}
	}

}
