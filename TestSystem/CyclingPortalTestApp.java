import java.time.LocalDateTime;
import java.time.LocalTime;

import cycling.CheckpointType;
import cycling.CyclingPortalImpl;
import cycling.DuplicatedResultException;
import cycling.IDNotRecognisedException;
import cycling.IllegalNameException;
import cycling.InvalidCheckpointTimesException;
import cycling.InvalidLengthException;
import cycling.InvalidLocationException;
import cycling.InvalidNameException;
import cycling.InvalidStageStateException;
import cycling.InvalidStageTypeException;
import cycling.CyclingPortal;
import cycling.MiniCyclingPortal;

import cycling.NameNotRecognisedException;
import cycling.StageType;

/**
 * A short program to illustrate an app testing some minimal functionality of a
 * concrete implementation of the CyclingPortal interface -- note you
 * will want to increase these checks, and run it on your CyclingPortalImpl
 * class
 * (not the BadCyclingPortal class).
 *
 * 
 * @author Diogo Pacheco
 * @version 2.0
 */
public class CyclingPortalTestApp {

	/**
	 * Test method.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		System.out.println("The system compiled and started the execution...");

		// TODO replace BadMiniCyclingPortalImpl by CyclingPortalImpl
		MiniCyclingPortal portal1 = new CyclingPortalImpl();
		MiniCyclingPortal portal2 = new CyclingPortalImpl();
		try {
			portal1.createRace("Race","des1");
			portal1.createRace("Racetwo","des1");

		} catch (IllegalNameException e) {
			e.printStackTrace();
			// TODO: handle exception
		} catch (InvalidNameException e) {
			e.printStackTrace();

			// TODO: handle exception
		} 
		
	}


		

		
	
}
