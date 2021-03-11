package fr.gwendal_jouneaux.rob_lang.webots.supervisor.model.actions;

import com.cyberbotics.webots.controller.Emitter;

import fr.gwendal_jouneaux.rob_lang.webots.supervisor.model.RobotModel;

public class ModelUpdate implements IAction {
	
	private RobotModel model;
	
	public ModelUpdate(RobotModel model) {
		this.model = model;
	}

	@Override
	public int duration() {
		return 1;
	}

	@Override
	public boolean perform(Emitter emitter, int timeStep) {
		String messageToSend = model.getRobotID() + " update";
		byte[] formated = messageToSend.getBytes();
        emitter.send(formated);
		return true;
	}
	
	public void update(double battery, double angle,
			double[] position, double[] distance) {
		model.setBattery(battery);
		model.setAngle(angle);
		model.setPosition(position);
		model.setDistance(distance);
	}
	
	public String toString() {
		return "Model update";
	}
}
