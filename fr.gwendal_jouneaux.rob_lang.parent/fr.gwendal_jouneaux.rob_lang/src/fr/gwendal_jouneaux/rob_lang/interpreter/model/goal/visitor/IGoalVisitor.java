package fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.visitor;

import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.Goal;
import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.Resource;
import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.Softgoal;
import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.Task;
import fr.gwendal_jouneaux.rob_lang.interpreter.model.goal.Variable;

public interface IGoalVisitor<T> {
	
	public T visitGoal(Goal goal);
	public T visitSoftGoal(Softgoal softgoal);
	public T visitTask(Task task);
	public T visitVariable(Variable variable);
	public T visitResource(Resource resource);
}
