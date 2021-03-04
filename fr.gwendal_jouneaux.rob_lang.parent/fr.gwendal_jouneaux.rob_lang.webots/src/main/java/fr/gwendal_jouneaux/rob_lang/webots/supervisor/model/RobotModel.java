package fr.gwendal_jouneaux.rob_lang.webots.supervisor.model;

import java.util.ArrayDeque;
import java.util.Deque;

import fr.gwendal_jouneaux.rob_lang.webots.supervisor.model.actions.ChangeSpeed;
import fr.gwendal_jouneaux.rob_lang.webots.supervisor.model.actions.IAction;
import fr.gwendal_jouneaux.rob_lang.webots.supervisor.model.actions.ModelUpdate;
import fr.gwendal_jouneaux.rob_lang.webots.supervisor.model.actions.Move;
import fr.gwendal_jouneaux.rob_lang.webots.supervisor.model.actions.Turn;

public class RobotModel {
	private Deque<IAction> actions;
	
	private double battery;
	private double speed;
	private double angle;
	private double[] position;
	private double[] distance;
	
	private String robotID;
	
	private boolean updateFlag;
	
	
	public RobotModel(String robotID, double battery, double speed, double angle,
			double[] position, double[] distance) {
		this.actions = new ArrayDeque<IAction>();
		this.battery = battery;
		this.speed = speed;
		this.angle = angle;
		this.position = position;
		this.distance = distance;
		this.robotID = robotID;
		this.updateFlag = false;
	}


	public synchronized void release() {
		notifyAll();
		System.out.println("Notify");
	}

	private synchronized void update() {
		updateFlag = false;
		actions.offer(new ModelUpdate(this));
		System.out.println("Waiting");
		try {
			wait(100000000); // wait main Thread to process the update request
		} catch (InterruptedException e) {}
		
	}

	// Effectors
	public void moveRobot(int msDuration) {
		updateFlag = true;
		actions.offer(new Move(robotID, msDuration));
	}
	public void turnRobot(int msDuration) {
		updateFlag = true;
		actions.offer(new Turn(robotID, msDuration));
	}
	public void setSpeed(double speed) {
		updateFlag = true;
		this.speed = speed;
		actions.offer(new ChangeSpeed(robotID, speed));
	}
	
	// Sensors
	public double getBattery() {
		if(updateFlag) update();
		return battery;
	}
	public double[] getPosition() {
		if(updateFlag) update();
		return position;
	}
	public double getDistance(int index) {
		if(updateFlag) update();
		return distance[index];
	}
	public double getCompass() {
		if(updateFlag) update();
		return angle;
	}
	public double getSpeed() {
		return speed;
	}
	
	
	public Deque<IAction> getActions() {
		return actions;
	}
	public String getRobotID() {
		return robotID;
	}




	public void setBattery(double battery) {
		this.battery = battery;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}
	public void setPosition(double[] position) {
		this.position = position;
	}
	public void setDistance(double[] distance) {
		this.distance = distance;
	}
	
	
}
