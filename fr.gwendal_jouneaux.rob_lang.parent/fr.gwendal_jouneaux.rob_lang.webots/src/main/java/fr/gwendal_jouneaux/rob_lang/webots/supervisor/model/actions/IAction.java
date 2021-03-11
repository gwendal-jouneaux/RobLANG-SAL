package fr.gwendal_jouneaux.rob_lang.webots.supervisor.model.actions;

import com.cyberbotics.webots.controller.Emitter;

public interface IAction {
	public int duration();
	public boolean perform(Emitter emitter, int timeStep);
}
