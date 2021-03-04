package fr.gwendal_jouneaux.rob_lang.webots.robots;

import java.util.ArrayList;
import java.util.List;

import com.cyberbotics.webots.controller.Compass;
import com.cyberbotics.webots.controller.DistanceSensor;
import com.cyberbotics.webots.controller.Emitter;
import com.cyberbotics.webots.controller.GPS;
import com.cyberbotics.webots.controller.Motor;
import com.cyberbotics.webots.controller.Receiver;
import com.cyberbotics.webots.controller.Robot;


public abstract class AbstractWebotsRobot extends Robot {
	private int timeStep;
	private String robotID;

	// EFFECTOR
	private Motor[] motors;
	private double speed;

	// SENSOR
	private DistanceSensor[] distanceSensors;
	private Receiver receiver;
	private Emitter emitter;
	private Compass compass;
	private GPS gps;

	private enum Mode {
		INIT, STOP, MOVE, TURN, SPEED, UPDATE
	}

	public AbstractWebotsRobot(int timeStep, String robotID) {
		receiver = getReceiver("receiver");
		emitter = getEmitter("emitter");
		compass = getCompass("compass");
		gps = getGPS("gps");
		batterySensorEnable(timeStep);
		receiver.enable(timeStep);
		compass.enable(timeStep);
		gps.enable(timeStep);
		
		this.speed = getMaxSpeed();

		this.timeStep = timeStep;
		this.robotID = robotID;
	}

	public final void run() {
		while (step(timeStep) != -1) {
			Mode mode = Mode.STOP;
			boolean movingOrientation = true;
			double newSpeed = 0;

			// Read the order of the supervisor
			if (receiver.getQueueLength() > 0) {
				String message = new String(receiver.getData());
				receiver.nextPacket();

				if(message.equals("init")) {
					mode = Mode.INIT;
				} else if (message.startsWith(robotID)) {
					String[] cmd = message.split(" ");
					switch (cmd[1]) {
					case "move":
						mode = Mode.MOVE;
						movingOrientation = cmd[2].equals("forward");
						break;
					case "turn":
						mode = Mode.TURN;
						movingOrientation = cmd[2].equals("left");
						break;
					case "speed":
						mode = Mode.SPEED;
						newSpeed = Double.parseDouble(cmd[2]);
						break;
					case "update":
						mode = Mode.UPDATE;
						break;
					default:
						mode = Mode.STOP;
					}
				}
			}

			switch (mode) {
			case MOVE:
				this.move(movingOrientation);
				break;
			case TURN:
				this.turn(movingOrientation);
				break;
			case STOP:
				this.stop();
				break;
			case SPEED:
				this.setSpeed(newSpeed);
				break;
			case UPDATE:
				String messageToSend = robotID + " update\n";
				messageToSend += createStateString();
				emitter.send(messageToSend.getBytes());
				break;
			case INIT:
				String initMessage = robotID + " init\n";
				initMessage += createStateString() + "\n";
				initMessage += getMaxSpeed();
				emitter.send(initMessage.getBytes());
				break;
			}
		}
	}
	
	private String createStateString() {
		String currentState = "";
		currentState += getBattery() + "\n";
		currentState += getOrientation() + "\n";

		List<String> values = new ArrayList<>();
		for(DistanceSensor sensor : distanceSensors) {
			values.add(sensor.getValue()+"");
		}
		currentState += String.join(" ", values) + "\n";
		
		double[] pos = gps.getValues();
		currentState += pos[0] + " " + pos[2] + " " + pos[1];
		
		return currentState;
	}

	public Motor[] getMotors() {
		return motors;
	}

	public void setMotors(Motor[] motors) {
		this.motors = motors;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		if(speed > 0) {
			this.speed = Math.min(speed, getMaxSpeed());
		} else {
			this.speed = Math.max(speed, -getMaxSpeed());
		}
	}

	public DistanceSensor[] getDistanceSensors() {
		return distanceSensors;
	}

	public void setDistanceSensors(DistanceSensor[] distanceSensors) {
		this.distanceSensors = distanceSensors;
	}

	public Receiver getReceiver() {
		return receiver;
	}

	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

	public Emitter getEmitter() {
		return emitter;
	}

	public void setEmitter(Emitter emitter) {
		this.emitter = emitter;
	}

	public Compass getCompass() {
		return compass;
	}

	public void setCompass(Compass compass) {
		this.compass = compass;
	}

	public GPS getGps() {
		return gps;
	}

	public void setGps(GPS gps) {
		this.gps = gps;
	}

	public int getTimeStep() {
		return timeStep;
	}

	public void setTimeStep(int timeStep) {
		this.timeStep = timeStep;
	}

	public abstract double getMaxSpeed();

	public abstract void move(boolean forward);

	public abstract void turn(boolean left);

	public abstract void stop();

	public double getBattery() {
		return batterySensorGetValue();
	}

	public double getOrientation() {
		double[] north = compass.getValues();
		double rad = Math.atan2(north[0], north[2]);
		double bearing = (rad - 1.5708) / Math.PI * 180.0;
		if (bearing < 0.0)
			bearing = bearing + 360.0;
		return bearing;
	}
}
