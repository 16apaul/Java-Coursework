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

		assert (portal1.getRaceIds().length == 0)
				: "Innitial Portal not empty as required or not returning an empty array.";
		assert (portal1.getTeams().length == 0)
				: "Innitial Portal not empty as required or not returning an empty array.";

		try {
			portal1.createRace("Race1", "Desc"); // creates 2 races
			portal1.createRace("Race2", "Desc");
			portal1.createRace("Race3", "Desc");

			portal1.getRaceIds(); // gets id of the races
		} catch (IllegalNameException e) {
			e.printStackTrace();
			System.out.println("Error");
		} catch (InvalidNameException e) {
			e.printStackTrace();
			System.out.println("Error");

		}
		try {
			portal1.viewRaceDetails(1); // view details of race with specified id
		} catch (IDNotRecognisedException e) {
			e.printStackTrace();
			System.out.println("Error");
		}
		try {
			portal1.removeRaceById(1); // remove the race at the id

		} catch (IDNotRecognisedException e) {
			e.printStackTrace();
			System.out.println("Error");

		}
		try {
			portal1.viewRaceDetails(1); // view details of race with specified id
		} catch (IDNotRecognisedException e) {
			e.printStackTrace();
			System.out.println("Error");
		}

		try { // creates 3 stages in raceID 1
			portal1.addStageToRace(1, "stage1", "desc", 5, LocalDateTime.now(), StageType.FLAT);
			portal1.addStageToRace(1, "stage2", "desc", 5, LocalDateTime.now(), StageType.TT);
			portal1.addStageToRace(1, "stage3", "desc", 5, LocalDateTime.now(), StageType.MEDIUM_MOUNTAIN);
			portal1.addStageToRace(1, "stage4", "desc", 5, LocalDateTime.now(), StageType.TT);

			System.out.println(LocalDateTime.now());
		} catch (IllegalNameException e) {
			e.printStackTrace();
			System.out.println("Error");

		} catch (IDNotRecognisedException e) {
			e.printStackTrace();
			System.out.println("Error");

		} catch (InvalidNameException e) {
			e.printStackTrace();
			System.out.println("Error");

		} catch (InvalidLengthException e) {
			e.printStackTrace();
			System.out.println("Error");

		}

		try {
			portal1.viewRaceDetails(1); // view details of race with specified id
		} catch (IDNotRecognisedException e) {
			e.printStackTrace();
			System.out.println("Error");
		}

		try {
			portal1.getStageLength(103);

		} catch (IDNotRecognisedException e) {
			e.printStackTrace();
			System.out.println("Error");
		}

		try {
			portal1.getRaceStages(1);

		} catch (IDNotRecognisedException e) {
			e.printStackTrace();
			System.out.println("Error");
		}

		assert (portal1.getTeams().length == 1)
				: "Portal1 should have one team.";

		assert (portal2.getTeams().length == 1)
				: "Portal2 should have one team.";

		try {
			portal1.removeStageById(103);

		} catch (IDNotRecognisedException e) {
			e.printStackTrace();
			System.out.println("Error");
		}
		try {
			portal1.getRaceStages(1);

		} catch (IDNotRecognisedException e) {
			e.printStackTrace();
			System.out.println("Error");
		}

		try {
			portal1.addCategorizedClimbToStage(101, 2.1, CheckpointType.C1, 2.1, 2.2);
			portal1.addCategorizedClimbToStage(101, 2.0, CheckpointType.C1, 2.1, 2.2);
			portal1.addCategorizedClimbToStage(101, 1.7, CheckpointType.C1, 2.1, 2.2);

		} catch (IDNotRecognisedException e) {
			e.printStackTrace();
			System.out.println("Error");
		} catch (InvalidStageStateException e) {
			e.printStackTrace();
			System.out.println("Error");
		} catch (InvalidStageTypeException e) {
			e.printStackTrace();
			System.out.println("Error");
		} catch (InvalidLocationException e) {
			e.printStackTrace();
			System.out.println("Error");
		}

		try {
			portal1.getStageCheckpoints(101);

		} catch (IDNotRecognisedException e) {
			e.printStackTrace();
			System.out.println("Error");
		}

		try {
			portal1.concludeStagePreparation(101);

		} catch (IDNotRecognisedException e) {
			e.printStackTrace();
			System.out.println("Error");
		} catch (InvalidStageStateException e) {
			e.printStackTrace();
			System.out.println("Error");
		}

		
		// Team tests
		try {
			portal1.createTeam("Team1", "Desc 1");
			portal1.createTeam("Team2", "Desc 2");

		} catch (InvalidNameException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Error");
		} catch (IllegalNameException e) {
			e.printStackTrace();
			System.out.println("Error");

		}

		try {
			portal1.createRider(1, "Rider 1", 1999);
			portal1.createRider(1, "Rider 2", 1999);
			portal1.createRider(1, "Rider 3", 1999);

		} catch (IDNotRecognisedException e) {
			e.printStackTrace();
			System.out.println("Error");
		}
		try {
			portal1.getTeams();
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			portal1.registerRiderResultsInStage(101, 101, LocalTime.of(0, 10, 10), LocalTime.of(0, 10, 11),
					LocalTime.of(0, 13),
					LocalTime.of(0, 19), LocalTime.of(0, 19, 9));
			portal1.registerRiderResultsInStage(101, 102, LocalTime.of(0, 10), LocalTime.of(0, 10), LocalTime.of(0, 11),
					LocalTime.of(0, 14), LocalTime.of(0, 15, 33));
			portal1.registerRiderResultsInStage(101, 103, LocalTime.of(0, 10), LocalTime.of(0, 10), LocalTime.of(0, 11),
					LocalTime.of(0, 14), LocalTime.of(0, 15, 34));
		} catch (IDNotRecognisedException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Error");
		} catch (InvalidCheckpointTimesException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Error");

		} catch (DuplicatedResultException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Error");

		} catch (InvalidStageStateException e) {
			e.printStackTrace();
			System.out.println("Error");
		}

		try {
			portal1.getRiderResultsInStage(101, 101);
		} catch (IDNotRecognisedException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Error");
		}
	}
}
