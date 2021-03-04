package fr.gwendal_jouneaux.rob_lang.webots.supervisor.model.actions;

import com.cyberbotics.webots.controller.Emitter;

public class ChangeSpeed implements IAction {
	
	String simulationRobotID;
	double speed;
	
	public ChangeSpeed(String simulationRobotID, double speed) {
		this.simulationRobotID = simulationRobotID;
		this.speed = speed;
	}

	@Override
	public int duration() {
		return 1;
	}

	@Override
	public boolean perform(Emitter emitter, int timeStep) {
		String messageToSend = simulationRobotID + " speed " + this.speed;
		byte[] formated = messageToSend.getBytes();
        emitter.send(formated);
		return true;
	}

}
