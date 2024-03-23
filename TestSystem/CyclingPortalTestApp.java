import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

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

    public static void main(String[] args) {
        System.out.println("The system compiled and started the execution...");

        // Creating an instance of the CyclingPortalImpl
        MiniCyclingPortal portal = new CyclingPortalImpl();

        testGetRaceIds(portal); 

        testCreateRace(portal); 

        testAddStageToRace(portal); 

        testViewRaceDetails(portal); 

        //testRemoveRaceById(portal);

        testGetNumberOfStages(portal); 

	testGetRaceStages(portal);

        testGetStageLength(portal);

        //testRemoveStageById(portal); 
	    
        testAddCategorizedClimbToStage(portal); 
	    
        testAddIntermediateSprintToStage(portal); 

        //testRemoveCheckpoint(portal); 

        testConcludeStagePreparation(portal); 

        testGetStageCheckpoints(portal);

        testCreateTeam(portal); 
	    
        //testRemoveTeam(portal);

        testGetTeams(portal);

        testCreateRider(portal); 

        testGetTeamRiders(portal); 

        //testRemoveRider(portal); 

        testRegisterRiderResultsInStage(portal); 

        testGetRiderResultsInStage(portal);

        testGetRiderAdjustedElapsedTimeInStage(portal);

        //testDeleteRiderResultsInStage(portal);

    }

    
    private static void testGetRaceIds(MiniCyclingPortal portal) {
		System.out.println("RUNNING getRaceIds...");
        int[] raceIds = portal.getRaceIds();
        System.out.println("Race IDs:");
        for (int raceId : raceIds) {
            System.out.println(raceId);
        }
    }

    
    private static void testCreateRace(MiniCyclingPortal portal) {
		System.out.println("RUNNING createRace...");
        try {
            int raceId = portal.createRace("TestRace", "Test Description");
            System.out.println("Race created with ID: " + raceId);
        } catch (IllegalNameException | InvalidNameException e) {
            e.printStackTrace();
        }
    }

    private static void testViewRaceDetails(MiniCyclingPortal portal) {
		System.out.println("RUNNING viewRaceDetails...");
        try {
            String raceDetails = portal.viewRaceDetails(1); // Assuming race ID 1 exists
            System.out.println("Race Details:");
            System.out.println(raceDetails);
        } catch (IDNotRecognisedException e) {
            e.printStackTrace();
        }
    }

    private static void testRemoveRaceById(MiniCyclingPortal portal) {
		System.out.println("RUNNING removeRaceById...");
        try {
            portal.removeRaceById(1); // Assuming race ID 1 exists
            System.out.println("Race removed successfully");
        } catch (IDNotRecognisedException e) {
            e.printStackTrace();
        }
    }

    private static void testGetNumberOfStages(MiniCyclingPortal portal) {
		System.out.println("RUNNING getNumberOfStages...");
        try {
            int numberOfStages = portal.getNumberOfStages(1); // Assuming race ID 1 exists
            System.out.println("Number of stages in the race: " + numberOfStages);
        } catch (IDNotRecognisedException e) {
            e.printStackTrace();
        }
    }

    private static void testGetRaceStages(MiniCyclingPortal portal) {
		System.out.println("RUNNING getRaceStages");
        try {
            int raceId = 1; // Assuming race ID 1 exists
            int[] stageIds = portal.getRaceStages(raceId);
            System.out.println("Stages for Race ID " + raceId + ":");
            for (int stageId : stageIds) {
                System.out.println(stageId);
            }
        } catch (IDNotRecognisedException e) {
            e.printStackTrace();
        }
    }

    private static void testGetStageLength(MiniCyclingPortal portal) {
		System.out.println("RUNNING getStageLength...");
        try {
            int stageId = 101; // Assuming stage ID 101 exists
            double stageLength = portal.getStageLength(stageId);
            System.out.println("Length of Stage ID " + stageId + ": " + stageLength);
        } catch (IDNotRecognisedException e) {
            e.printStackTrace();
        }
    }

    private static void testRemoveStageById(MiniCyclingPortal portal) {
    System.out.println("RUNNING removeStageById...");
        try {
            int stageId = 101; // Assuming stage ID 101 exists
            portal.removeStageById(stageId);
            System.out.println("Stage with ID " + stageId + " removed successfully");
        } catch (IDNotRecognisedException e) {
            e.printStackTrace();
        }
    }

    private static void testAddCategorizedClimbToStage(MiniCyclingPortal portal) {
		System.out.println("RUNNING addCategorizedClimbToStage...");
        try {
            int stageId = 101; // Assuming stage ID 101 exists
            int checkpointId = portal.addCategorizedClimbToStage(stageId, 10.0, CheckpointType.C2, 5.0, 20.0);
            System.out.println("Categorized Climb added to Stage ID " + stageId + ", Checkpoint ID: " + checkpointId);
        } catch (IDNotRecognisedException | InvalidLocationException | InvalidStageStateException | InvalidStageTypeException e) {
            e.printStackTrace();
        }
    }

    public static void testAddIntermediateSprintToStage(MiniCyclingPortal portal) {
        System.out.println("RUNNING addIntermediateSprintToStage...");
        try {
            int raceId = portal.createRace("TestRace", "TestDescription");

            // Then, add a stage to the race
            int stageId = portal.addStageToRace(raceId, "Stage1", "Stage 1 Description", 100.0, LocalDateTime.now(), StageType.FLAT);
            System.out.println("Stage " + stageId + " added to Race ID " + raceId);

            // FIX THIS
            // Now, set the stage state to 'Waiting for Results'
            portal.concludeStagePreparation(stageId);

            // Now the stage is in the 'Waiting for Results' state, you can add intermediate sprints
            int sprintId = portal.addIntermediateSprintToStage(stageId, 50.0);
            System.out.println("Intermediate Sprint added to Stage ID " + stageId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

private static void testAddStageToRace(MiniCyclingPortal portal) {
	System.out.println("RUNNING addStageToRace...");
	try {
		int raceId = 1; 
		String stageName = "TestStage";
		String description = "Test Description";
		double length = 50.0; // Length of the stage in km
		LocalDateTime startTime = LocalDateTime.now(); // Current time
		StageType type = StageType.FLAT;

		int addedRaceId = portal.addStageToRace(raceId, stageName, description, length, startTime, type);
		System.out.println("Stage added to Race ID " + addedRaceId);
	} catch (IDNotRecognisedException | IllegalNameException | InvalidNameException | InvalidLengthException e) {
		e.printStackTrace();
		}
    }

    private static void testRemoveCheckpoint(MiniCyclingPortal portal) {
		System.out.println("RUNNING removeCheckpoint...");
        try {
            int checkpointId = 10101; // Assuming checkpoint ID 101 exists
            portal.removeCheckpoint(checkpointId);
            System.out.println("Checkpoint with ID " + checkpointId + " removed successfully");
        } catch (IDNotRecognisedException | InvalidStageStateException e) {
            e.printStackTrace();
        }
    }

    private static void testConcludeStagePreparation(MiniCyclingPortal portal) {
		System.out.println("RUNNING concludeStagePreparation...");
        try {
            int stageId = 101; // Assuming stage ID 101 exists
            portal.concludeStagePreparation(stageId);
            System.out.println("Stage preparation concluded for Stage ID " + stageId);
        } catch (IDNotRecognisedException | InvalidStageStateException e) {
            e.printStackTrace();
        }
    }

    private static void testGetStageCheckpoints(MiniCyclingPortal portal) {
        System.out.println("RUNNING getStageCheckpoints...");
        try {
            int stageId = 101; // Assuming stage ID 101 exists
            int[] checkpointIds = portal.getStageCheckpoints(stageId);
            System.out.println("Checkpoints for Stage ID " + stageId + ": " + Arrays.toString(checkpointIds));
        } catch (IDNotRecognisedException e) {
            e.printStackTrace();
        }
    }

     public static void testCreateTeam(MiniCyclingPortal portal) {
        System.out.println("RUNNING createTeam...");
        try {
            int teamId1 = portal.createTeam("TeamOne", "Description 1");
            System.out.println("Team created with ID: " + teamId1);

            int teamId2 = portal.createTeam("TeamTwo", "Description 2");
            System.out.println("Team created with ID: " + teamId2);
        } catch (IllegalNameException | InvalidNameException e) {
            e.printStackTrace();
        }
    }

    public static void testRemoveTeam(MiniCyclingPortal portal) {
        System.out.println("RUNNING removeTeam...");
        try {
            int teamId = 1; // Assume team with ID 1 needs to be removed
            portal.removeTeam(teamId);
            System.out.println("Team with ID " + teamId + " removed successfully.");
        } catch (IDNotRecognisedException e) {
            e.printStackTrace();
        }
    }

    public static void testGetTeams(MiniCyclingPortal portal) {
        System.out.println("RUNNING getTeams...");
        int[] teamIds = portal.getTeams();
        System.out.println("List of team IDs:");
        for (int id : teamIds) {
            System.out.println(id);
        }
    }

    public static void testGetTeamRiders(MiniCyclingPortal portal) {
        System.out.println("RUNNING getTeamRiders...");
        try {
            int teamId = 1; // Assume we want to get riders for team with ID 1
            int[] riderIds = portal.getTeamRiders(teamId);
            System.out.println("List of rider IDs in team " + teamId + ":");
            for (int id : riderIds) {
                System.out.println(id);
            }
        } catch (IDNotRecognisedException e) {
            e.printStackTrace();
        }
    }

    public static void testRegisterRiderResultsInStage(MiniCyclingPortal portal) {
        System.out.println("RUNNING registerRiderResultsInStage...");
        try {
            int stageId = 101; // Assuming stage ID for testing is 101
            int riderId = 101; // Assuming rider ID for testing is 101
            // Assuming the checkpoint times are provided as LocalTime objects
            LocalTime startTime = LocalTime.of(8, 0); // Start time
            LocalTime checkpoint1Time = LocalTime.of(9, 0); // Checkpoint 1 time
            LocalTime checkpoint2Time = LocalTime.of(10, 0); // Checkpoint 2 time
            LocalTime finishTime = LocalTime.of(11, 0); // Finish time

            // Register rider results in the stage
            portal.registerRiderResultsInStage(stageId, riderId, startTime, checkpoint1Time, checkpoint2Time, finishTime);
            System.out.println("Rider results registered successfully for rider ID " + riderId + " in stage ID " + stageId);
        } catch (IDNotRecognisedException | DuplicatedResultException | InvalidCheckpointTimesException | InvalidStageStateException e) {
            e.printStackTrace();
        }
    }

        public static void testCreateRider(MiniCyclingPortal portal) {
        try {
            System.out.println("RUNNING createRider...");
            int riderId = portal.createRider(1, "John Doe", 1990);
            System.out.println("Rider created with ID: " + riderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testRemoveRider(MiniCyclingPortal portal) {
        try {
            System.out.println("RUNNING removeRider...");
            portal.removeRider(101); // Assuming rider ID is 101
            System.out.println("Rider removed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testGetRiderResultsInStage(MiniCyclingPortal portal) {
        try {
            System.out.println("RUNNING getRiderResultsInStage...");
            LocalTime[] results = portal.getRiderResultsInStage(101, 1); // Assuming stage ID is 101 and rider ID is 1
            System.out.println("Rider results in the stage:");
            for (LocalTime result : results) {
                System.out.println(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testGetRiderAdjustedElapsedTimeInStage(MiniCyclingPortal portal) {
        try {
            System.out.println("RUNNING getRiderAdjustedElapsedTimeInStage...");
            LocalTime adjustedTime = portal.getRiderAdjustedElapsedTimeInStage(101, 1); // Assuming stage ID is 101 and rider ID is 1
            System.out.println("Rider adjusted elapsed time in the stage: " + adjustedTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testDeleteRiderResultsInStage(MiniCyclingPortal portal) {
        try {
            System.out.println("RUNNING deleteRiderResultsInStage...");
            portal.deleteRiderResultsInStage(101, 1); // Assuming stage ID is 101 and rider ID is 1
            System.out.println("Rider results deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    }






