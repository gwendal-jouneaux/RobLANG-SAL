package fr.gwendal_jouneaux.rob_lang.webots.supervisor.model.actions;

import com.cyberbotics.webots.controller.Emitter;

public class Turn implements IAction {

	private String simulationRobotID;
	private int duration;
	private boolean left;
	
	public Turn(String simulationRobotID, int duration) {
		this.simulationRobotID = simulationRobotID;
		this.duration = duration;
		this.left = duration > 0;
	}

	@Override
	public boolean perform(Emitter emitter, int timeStep) {
		String messageToSend = simulationRobotID + " turn " + (left ? "left" : "right");
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
		return "Turn -> remaining time : " + duration;
	}
}
