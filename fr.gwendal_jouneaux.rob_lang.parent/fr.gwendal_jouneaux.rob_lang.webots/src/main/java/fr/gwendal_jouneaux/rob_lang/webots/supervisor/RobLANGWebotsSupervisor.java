package fr.gwendal_jouneaux.rob_lang.webots.supervisor;

import java.util.Deque;

import com.cyberbotics.webots.controller.Emitter;
import com.cyberbotics.webots.controller.Receiver;
import com.cyberbotics.webots.controller.Supervisor;

import fr.gwendal_jouneaux.rob_lang.webots.supervisor.interpretation.InterpretationThread;
import fr.gwendal_jouneaux.rob_lang.webots.supervisor.interpretation.WebotsContext;
import fr.gwendal_jouneaux.rob_lang.webots.supervisor.model.RobotModel;
import fr.gwendal_jouneaux.rob_lang.webots.supervisor.model.actions.IAction;
import fr.gwendal_jouneaux.rob_lang.webots.supervisor.model.actions.ModelUpdate;

public class RobLANGWebotsSupervisor extends Supervisor{
	
	private Receiver receiver;
	private Emitter emitter;
	
	private WebotsContext context;
	private RobotModel robotModel;
	private ModelUpdate pendingUpdate;
	private Deque<IAction> actionStack;
	private InterpretationThread interpreterThread;
	
	private final int timeStep = 1;
	
	public static void main(String[] args) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		RobLANGWebotsSupervisor controller = new RobLANGWebotsSupervisor();
	    controller.run();
	  }

	public RobLANGWebotsSupervisor() {
		receiver = getReceiver("receiver");
	    emitter = getEmitter("emitter");
	    
	    receiver.enable(timeStep);
	    
	    String messageToSend = "init";
		byte[] formated = messageToSend.getBytes();
        emitter.send(formated);
        
        // wait for init message
        while (receiver.getQueueLength()<=0){
        	step(timeStep);
	     }
        String initMessage = new String(receiver.getData());
        receiver.nextPacket();
        this.robotModel = initModel(initMessage);
		this.actionStack = robotModel.getActions();
		
		this.context = new WebotsContext(robotModel, timeStep);
		this.interpreterThread = new InterpretationThread(context);
	}
	
	public void run() {
		interpreterThread.start();
		System.out.println("Program Started");
		
		// Loop to reflect model (AbstractWebotsRobots) changes in the simulation
		while (step(timeStep) != -1) {
			
			// Manage incoming communications
			if (receiver.getQueueLength()>0){
		        String receivedMessage = new String(receiver.getData());
		        receiver.nextPacket();

		        if(pendingUpdate != null) {
		        	update(receivedMessage);
		        	robotModel.release();
		        }
		     }
			
			// check action stack to reflect actions
			if(!actionStack.isEmpty()) {
				IAction action = actionStack.peek();
				boolean end = action.perform(emitter, timeStep);
				if(end) {
					actionStack.remove();
				}
				
				// If we asked for a model update to reflect sensor state
				// put it as pending to resolve next simulation step
				if(action instanceof ModelUpdate) {
					pendingUpdate = (ModelUpdate) action;
				}
			}
		}
	}
	
	private void update(String message) {
		String[] data = message.split("\n");
		if(! data[0].startsWith(robotModel.getRobotID())) return;
		if(! data[0].endsWith("update")) return;
		
		double battery = Double.parseDouble(data[1]);
		double angle = Double.parseDouble(data[2]);
		
		String[] positionStr = data[4].split(" ");
		String[] distanceStr = data[3].split(" ");
		double[] position = new double[positionStr.length];
		double[] distance = new double[distanceStr.length];
		for (int i = 0; i < positionStr.length; i++) {
			position[i] = Double.parseDouble(positionStr[i]);
		}
		for (int i = 0; i < distanceStr.length; i++) {
			distance[i] = Double.parseDouble(distanceStr[i]);
		}
		pendingUpdate.update(battery, angle, position, distance);
	}
	
	private RobotModel initModel(String message) {
		String[] data = message.split("\n");
		if(! data[0].endsWith("init")) return null;
		
		String ID = data[0].split(" ")[0];
		
		double battery = Double.parseDouble(data[1]);
		double speed = Double.parseDouble(data[5]);
		double angle = Double.parseDouble(data[2]);
		
		String[] positionStr = data[4].split(" ");
		String[] distanceStr = data[3].split(" ");
		double[] position = new double[positionStr.length];
		double[] distance = new double[distanceStr.length];
		for (int i = 0; i < positionStr.length; i++) {
			position[i] = Double.parseDouble(positionStr[i]);
		}
		for (int i = 0; i < distanceStr.length; i++) {
			distance[i] = Double.parseDouble(distanceStr[i]);
		}
		return new RobotModel(ID, battery, speed, angle, position, distance);
	}

}
