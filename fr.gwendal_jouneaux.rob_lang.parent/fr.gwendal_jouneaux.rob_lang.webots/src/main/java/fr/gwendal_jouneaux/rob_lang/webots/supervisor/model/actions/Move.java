package fr.gwendal_jouneaux.rob_lang.webots.supervisor.model.actions;

import com.cyberbotics.webots.controller.Emitter;

public class Move implements IAction {
	
	private String simulationRobotID;
	private int duration;
	private boolean forward;
	
	public Move(String simulationRobotID, int duration) {
		this.simulationRobotID = simulationRobotID;
		this.duration = Math.abs(duration);
		this.forward = duration > 0;
	}

	@Override
	public boolean perform(Emitter emitter, int timeStep) {
		String messageToSend = simulationRobotID + " move " + (forward ? "forward" : "backward");
		byte[] formated = messageToSend.getBytes();
        emitter.send(formated);
		duration -= 1;//timeStep;
		return duration <= 0;
	}

	@Override
	public int duration() {
		return duration;
	}

	public String toString() {
		return "Move -> remaining time : " + duration;
	}
}
