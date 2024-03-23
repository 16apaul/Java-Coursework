package cycling;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.Iterator;
import java.util.Arrays;
import cycling.StageType;
import cycling.CheckpointType;


/**
 * BadMiniCyclingPortal is a minimally compiling, but non-functioning
 * implementor
 * of the MiniCyclingPortal interface.
 *
 * @author Diogo Pacheco
 * @version 2.0
 *
 */

public class CyclingPortalImpl implements MiniCyclingPortal {
	public static ArrayList<Race> races = new ArrayList<>();
	public static ArrayList<Rider> riders = new ArrayList<>();
	public static ArrayList<Team> teams = new ArrayList<>();
	public static ArrayList<Stage> stages = new ArrayList<>();
	public static ArrayList<Checkpoint> checkpoints = new ArrayList<>();

	@Override
	// n
	public int[] getRaceIds() {
		// Race IDs correspond to the number they are in the list
		int[] raceIds = new int[races.size()];
		if (raceIds == null) {
			throw new AssertionError("Race IDs array is null");
		}
		// Check if there is at least one race
		if (races.size() >= 1) {
			// Loop through each race
			for (int i = 0; i < races.size(); i++) {
				// Get the current race
				Race race = races.get(i);
				// Get the ID of the current race and store it in the raceIds array
				raceIds[i] = race.getRaceId();
			}
		}
		return raceIds;
	}

	@Override
	// n
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
		// Check if the name is null or empty
		if (name == null || name.length() == 0) {
			throw new InvalidNameException("Your name cannot be null or empty");
		}
		// Check if the name contains spaces
		if (name.contains(" ")) {
			throw new InvalidNameException("Your name cannot contain any spaces");
		}
		// Check if the name exceeds the length limit
		if (name.length() > 30) {
			throw new InvalidNameException("Your race name must be fewer than 30 characters");
		}
		// Check if name contains only letters
		boolean illegalName = false;
		for (int i = 0; i < name.length(); i++) {
			if (!Character.isLetter(name.charAt(i))) {
				illegalName = true;
				break;
			}
		}
		if (illegalName) {
			throw new InvalidNameException("The race name cannot contain non-letter characters");
		}
		int raceId = races.size() + 1;
		Race newRace = new Race(raceId, name, description);
		races.add(newRace);
		return raceId;
	}

	@Override
	public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
		boolean raceFound = false;
		Race givenRace = null;
		// Loops through races to find one that matches given ID
		for (Race race : races) {
			if (race.getRaceId() == raceId) {
				raceFound = true;
				givenRace = race;
				break;
			}
		}
		if (!raceFound) {
			throw new IDNotRecognisedException("This race ID cannot be found");
		}
		// Calculate the number of stages
		int numStages = givenRace.getStages().size();

		// Calculate the total length of the race
		double totalLength = 0;
		for (Stage stage : givenRace.getStages()) {
			totalLength += stage.getLength();}

		// Construct string with race details
		String raceDetails = "Race ID: " + givenRace.getRaceId() + "\n"
				+ "Name: " + givenRace.getName() + "\n"
				+ "Description: " + givenRace.getDescription() + "\n"
				+ "Number of Stages: " + numStages + "\n";

		if (numStages > 0) {
			raceDetails += "Total Length: " + totalLength + " km";
		} else {
			raceDetails += "No stages available";
		}

		return raceDetails;
	}

	@Override
	// n
	public void removeRaceById(int raceId) throws IDNotRecognisedException {
		// Checks if raceId is valid
		if (raceId <= 0 || raceId > races.size()) {
			throw new IDNotRecognisedException(raceId + " is invalid.");
		}

		// Removes the race
		Race raceToRemove = races.get(raceId - 1);
		races.remove(raceToRemove);

		// Update the IDs of the remaining races
		for (int i = raceId - 1; i < races.size(); i++) {
			Race race = races.get(i);
			races.set(i, new Race(i + 1, race.getName(), race.getDescription()));
		}

		// Remove all stages associated with the race
		for (Stage stage : raceToRemove.getStages()) {
			removeStageById(stage.getStageId());
		}
	}

	@Override
	// n
	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
		if (raceId <= 0 || raceId > races.size()) {
			throw new IDNotRecognisedException(raceId + " is invalid.");
		}
		Race race = races.get(raceId - 1);
		if (race == null) {
			throw new IDNotRecognisedException("Race with ID " + raceId + " does not exist.");
		}
		List<Stage> stages = race.getStages();
		int numberOfStages = stages.size();

		return numberOfStages;
	}

	@Override
	// n
	public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
		// Check if the race ID is valid
		if (raceId <= 0 || raceId > races.size()) {
			throw new IDNotRecognisedException(raceId + " is invalid.");
		}
		Race race = races.get(raceId - 1);
		List<Stage> stages = race.getStages();
		// Return an empty array if no stages exist
		if (stages.isEmpty()) {
			return new int[0];
		}
		int[] stageIds = new int[stages.size()];
		for (int i = 0; i < stages.size(); i++) {
			stageIds[i] = (raceId * 100) + (i + 1);
		}
		return stageIds;
	}

	// Method to check if stageID is valid
	public void checkStageIdValidity(int stageId) throws IDNotRecognisedException {
		// Extracts race ID from stageId
		int raceId = stageId / 100;
		// Extracts stage index from stage ID
		int stageIndex = (stageId % 100);

		if (raceId <= 0 || raceId > races.size()) {
			throw new IDNotRecognisedException("Race ID " + raceId + " is invalid.");
		}
		Race race = races.get(raceId - 1);
		List<Stage> stages = race.getStages();

		if (stageIndex < 1 || stageIndex > stages.size()) {
			throw new IDNotRecognisedException("Stage ID " + stageId + " does not exist.");
		}
	}

	@Override
	// n
	public double getStageLength(int stageId) throws IDNotRecognisedException {
		checkStageIdValidity(stageId);
		int raceId = stageId / 100;
		int stageIndex = (stageId % 100);

		Race race = races.get(raceId - 1);
		List<Stage> stages = race.getStages();

		Stage stage = stages.get(stageIndex - 1);
		double stageLength = stage.getLength();
		return stageLength;
	}

	@Override
	public void removeStageById(int stageId) throws IDNotRecognisedException {
		checkStageIdValidity(stageId);
		int raceId = stageId / 100;
		int stageIndex = (stageId % 100) - 1;
		Race race = races.get(raceId - 1);
		List<Stage> stages = race.getStages();
		stages.remove(stageIndex);
		System.out.println("removed stage ID " + stageId);
	}

	@Override
	// n
	public int addCategorizedClimbToStage(int stageId, Double location, CheckpointType type, Double averageGradient,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException {
		// Check if the stage ID is valid
		checkStageIdValidity(stageId);
		// Retrieve the race and stage
		Race race = races.get(stageId / 100 - 1);
		Stage stage = race.getStages().get((stageId % 100) - 1);
		// CHECK/FIX
		int checkpointId = (stageId * 100) + checkpoints.size() + 1;

		// Check if the stage is in a valid state
		if (stage.getStageState() == StageState.WAITING_FOR_RESULTS) {
			throw new InvalidStageStateException("The stage is in 'waiting for results' state");
		}
		// Check if the stage type is valid
		if (stage.getStageType() == StageType.TT) {
			throw new InvalidStageTypeException("Time-trial stages cannot contain any checkpoints");
		}
		// Check if the location is within the stage length
		if (location < 0 || location > stage.getLength()) {
			throw new InvalidLocationException("The location must be within the stage length");
		}
		// Creates new checkpoint
		Checkpoint checkpoint = new Checkpoint(checkpointId, location, type, averageGradient, length);
		// Adds the checkpoint to the stage
		stage.addCheckpoint(checkpoint);
		return checkpointId;
	}

	@Override
	// us
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
		// Check if the stage ID is valid
		checkStageIdValidity(stageId);
		int raceId = stageId / 100;
		int stageIndex = (stageId % 100);
		Race race = races.get(raceId - 1);
		List<Stage> stages = race.getStages();
		Stage stage = stages.get(stageIndex - 1);

		// Check if the stage is in the right state
		if (stage.getStageState() != StageState.WAITING_FOR_RESULTS) {
			throw new InvalidStageStateException("Intermediate sprints can only be added to stages in the 'Waiting for Results' state");
		}
		// Check if the stage type allows intermediate sprints
		if (stage.getStageType() != StageType.FLAT) {
			throw new InvalidStageTypeException("Intermediate sprints can only be added to flat stages");
		}


		// Check if the location is valid
		if (location < 0 || location > stage.getLength()) {
			throw new InvalidLocationException("Invalid intermediate sprint location");
		}
		stage.addIntermediateSprint(location);

		return stageId;
	}



	// Helper method for convenience
	// CHECK/FIX
	public void checkpointIdNotRecognised(int checkpointId) throws IDNotRecognisedException {
		int stageId = checkpointId / 100;
		int stageIndex = (stageId % 100) - 1;
		int checkpointIndex = (checkpointId % (stageId * 100)) - 1;
		int raceId = stageId / 100;

		checkStageIdValidity(stageId);
		// Check if race ID is valid
		if (raceId <= 0 || raceId > races.size()) {
			throw new IDNotRecognisedException(raceId + " is invalid.");
		}
		Race race = races.get(raceId - 1);
		List<Stage> stages = race.getStages();
		Stage stage = stages.get(stageIndex);
		List<Checkpoint> checkpoints = stage.getCheckpoints();

		if (checkpoints.size() <= checkpointIndex || checkpointIndex < 0) {
			System.out.println(checkpoints.size());
			System.out.println(checkpointIndex);

			throw new IDNotRecognisedException("Checkpoint ID not found for:" + checkpointId);
		}
	}

	@Override
	// n
	public void removeCheckpoint(int checkpointId) throws IDNotRecognisedException, InvalidStageStateException {
		int stageId = checkpointId / 100;
		int checkpointIndex = checkpointId % 100;
		checkStageIdValidity(stageId);
		Race race = races.get(stageId / 100 - 1);
		Stage stage = race.getStages().get((stageId % 100) - 1);
		// Check if the stage is in a valid state
		if (stage.getStageState() == StageState.WAITING_FOR_RESULTS) {
			throw new InvalidStageStateException("The stage is in 'waiting for results' state.");
		}
		List<Checkpoint> checkpoints = stage.getCheckpoints();
		// Check if the checkpoint index is valid
		if (checkpointIndex < 0 || checkpointIndex > checkpoints.size()) {
			throw new IDNotRecognisedException("Checkpoint ID is not recognized.");
		}
		if (checkpointIndex == checkpoints.size()) {
			// Remove the last checkpoint if checkpointIndex equals checkpoints.size()
			checkpoints.remove(checkpoints.size() - 1);
		} else {
			// Otherwise, remove the checkpoint at the specified index
			checkpoints.remove(checkpointIndex);
		}
	}


	// Helper method to sort checkpoints
	// CHECK
	public void sortCheckpointsByLocation(List<Checkpoint> checkpoints) {
		int n = checkpoints.size();
		// Using bubble sort, will replace with something more efficient if I have time
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - i - 1; j++) {
				// Compare the locations of adjacent checkpoints
				if (checkpoints.get(j).getLocation() > checkpoints.get(j + 1).getLocation()) {
					// Swap if the current checkpoint's location is greater than the next one
					Checkpoint temp = checkpoints.get(j);
					checkpoints.set(j, checkpoints.get(j + 1));
					checkpoints.set(j + 1, temp);
				}
			}
		}
	}

	@Override
	// us
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
		checkStageIdValidity(stageId);
		int raceId = stageId / 100;
		int stageIndex = (stageId % 100) - 1;
		Race race = races.get(raceId - 1);
		List<Stage> stages = race.getStages();
		Stage stage = stages.get(stageIndex);

		if (stage.getStageState() != StageState.WAITING_FOR_RESULTS) {
			throw new InvalidStageStateException("The stage is already waiting for results.");
		}
		// Sort the checkpoints
		List<Checkpoint> checkpoints = stage.getCheckpoints();
		sortCheckpointsByLocation(checkpoints);
		// Print the sorted checkpoints
		for (Checkpoint checkpoint : stage.getCheckpoints()) {
			System.out.println(checkpoint.getLocation());
		}
		// Advance the stage state
		stage.nextState();
	}

	@Override
	// c
	public int[] getStageCheckpoints(int stageId) throws IDNotRecognisedException {
		// Check if the stage ID is valid
		checkStageIdValidity(stageId);
		// Retrieve the race and stage
		Race race = races.get(stageId / 100 - 1);
		Stage stage = race.getStages().get((stageId % 100) - 1);
		// Get the list of checkpoints
		List<Checkpoint> checkpoints = stage.getCheckpoints();
		// Create an array to store the checkpoint IDs
		int[] checkpointIds = new int[checkpoints.size()];
		// Populate the array with the checkpoint IDs
		for (int i = 0; i < checkpoints.size(); i++) {
			checkpointIds[i] = checkpoints.get(i).getCheckpointId();
		}
		return checkpointIds;
	}

	@Override
	// s(0)
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
		// TODO Auto-generated method stub
		int teamId = teams.size() + 1;

		for (Team team : teams) {
			if (team.getName().equals(name)) {
				throw new IllegalNameException("This name has been assigned to another team, please choose a new name");
			}
		}
		if (name == null || name.length() == 0) {
			throw new cycling.InvalidNameException("Please input a team name");
		}
		if (name.contains(" ")) {
			throw new cycling.InvalidNameException("Your team name cannot contain any spaces");
		}
		if (name.length() > 30) {
			throw new cycling.InvalidNameException("You team name must be fewer than 30 characters");
		}
		Team team = new Team(teamId, name, description);
		teams.add(team);

		return teamId;
	}

	@Override
	// n
	// mc
	public void removeTeam(int teamId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		if (teamId <= 0 || teamId > teams.size()) {
			throw new IDNotRecognisedException("Team ID " + teamId + " not recognised");
		}
		teams.remove(teamId - 1);
	}

	@Override
	// n
	// mc
	public int[] getTeams() {
		// TODO Auto-generated method stub
		int[] teamIds = new int[teams.size()];

		// Populate the array with team IDs
		for (int i = 0; i < teams.size(); i++) {
			teamIds[i] = i + 1; // fills array with numbers since those are the team ids
			System.out.println(teamIds[i]);
		}

		return teamIds;
	}

	@Override
	// us
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
		if (teamId <= 0 || teamId > teams.size()) {
			throw new IDNotRecognisedException("Team ID " + teamId + " not recognised");
		}
		Team team = teams.get(teamId - 1); // Get the team by its index (teamId - 1)
		List<Rider> ridersInTeam = team.getRiders();

		// Create an array to store rider IDs
		int[] riderIds = new int[ridersInTeam.size()];

		// Populate the array with rider IDs
		for (int i = 0; i < ridersInTeam.size(); i++) {
			riderIds[i] = (teamId * 100) + (i + 1);
			System.out.println(riderIds[i]);
		}

		return riderIds;
	}


	@Override
	// n
	// p
	public int createRider(int teamId, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {
		// TODO Auto-generated method stub
		// Validate the year of birth
		if (teamId <= 0 || teamId > teams.size()) {
			throw new IDNotRecognisedException("Team ID " + teamId + " not recognised");
		}

		if (yearOfBirth < 1900 || yearOfBirth > 2024) {
			throw new IllegalArgumentException("Invalid year of birth");
		}
		Team team = teams.get(teamId - 1);

		List<Rider> riders = team.getRiders();
		team.addRider(teamId, name, yearOfBirth); // Add rider to the list of riders
		int riderSize = riders.size();
		int riderId = (teamId * 100) + riderSize;
		System.out.println("Rider created with ID:" + riderId);
		return riderId;
	}

	@Override
	// n
	// mc
	// fix
	public void removeRider(int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		// Iterate through the list of races
		for (int i = 0; i < races.size(); i++) {
			Race race = races.get(i);
			// Iterate through the stages of each race
			List<Stage> stages = race.getStages();
			for (int j = 0; j < stages.size(); j++) {
				Stage stage = stages.get(j);
				// Get the list of rider results in the stage
				List<RiderResult> riderResults = stage.getRiderResult();
				// Iterate through the rider results
				for (int k = 0; k < riderResults.size(); k++) {
					RiderResult riderResult = riderResults.get(k);
					// If the rider ID matches, remove the rider result
					if (riderResult.getRiderId() == riderId) {
						riderResults.remove(k);
						// Decrement k to account for the removal of an element
						k--;
					}
				}
			}
		}
		// If rider not found in rider result, throw exception
		throw new IDNotRecognisedException("Rider ID not recognised: " + riderId);
	}

	@Override
	// us
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpointTimes)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointTimesException,
			InvalidStageStateException {
		// Check if the stage ID is valid
		checkStageIdValidity(stageId);

		// Retrieve the race and stage
		Race race = races.get(stageId / 100 - 1);
		Stage stage = race.getStages().get((stageId % 100) - 1);

		// Check if the stage is in a valid state
		if (stage.getStageState() != StageState.WAITING_FOR_RESULTS) {
			throw new InvalidStageStateException("The stage is not in 'waiting for results' state.");
		}

		// Check if the rider already has a result for the stage
		if (stage.hasRiderResult(riderId)) {
			throw new DuplicatedResultException("The rider already has a result for the stage.");
		}

		// Check if the length of checkpointTimes is valid
		int expectedCheckpointCount = stage.getCheckpoints().size() + 2; // checkpoints + start + finish
		if (checkpointTimes.length != expectedCheckpointCount) {
			throw new InvalidCheckpointTimesException("Invalid number of checkpoint times.");
		}

		// Check if start and finish times are included
		LocalTime startTime = checkpointTimes[0];
		LocalTime finishTime = checkpointTimes[checkpointTimes.length - 1];
		if (startTime == null || finishTime == null) {
			throw new InvalidCheckpointTimesException("Start and finish times are required.");
		}

		// Create a new RiderResult object with the provided checkpoint times
		RiderResult riderResult = new RiderResult(riderId, checkpointTimes);

		// Add the rider result to the stage
		stage.addRiderResult(riderResult, checkpointTimes);
	}

	// pa
	public void riderIdNotRecognised(int riderId) throws IDNotRecognisedException {
		int teamId = riderId / 100;
		int riderIndex = (riderId % 100) - 1;
		if (teamId <= 0 || teamId > teams.size()) {
			throw new IDNotRecognisedException("Team ID " + teamId + " not recognised");
		}

		Team team = teams.get(teamId - 1);
		List<Rider> riders = team.getRiders();
		if (riderIndex < 0 || riderIndex >= riders.size()) {
			throw new IDNotRecognisedException("Rider id not recognised for:" + riderId);
		}
	}

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// Check if the stage ID is valid
		checkStageIdValidity(stageId);
		// Check if the rider ID is recognized
		riderIdNotRecognised(riderId);
		// Extract race ID and stage index
		int raceId = stageId / 100;
		int stageIndex = (stageId % 100) - 1;
		// Get the race and its stages
		Race race = races.get(raceId - 1);
		List<Stage> stages = race.getStages();
		Stage stage = stages.get(stageIndex);
		List<RiderResult> riderResults = stage.getRiderResult();
		// Loop through rider results to find the one with the specified ID
		for (RiderResult riderResult : riderResults) {
			if (riderResult.getRiderId() == riderId) {
				// Get checkpoint times for the rider
				LocalTime[] riderCheckpointTimes = riderResult.getCheckpoints();
				// Print out checkpoint times (for debugging)
				System.out.println("Checkpoint times for rider:");
				for (LocalTime checkpointTime : riderCheckpointTimes) {
					System.out.println(checkpointTime);
				}
				LocalTime[] riderCheckpointElapsedTimes = riderResult.getRiderCheckpointElapsedTimes();
				LocalTime[] checkpointAndTotalElapsedTime = new LocalTime[riderCheckpointElapsedTimes.length + 1];
				// Populate array with elapsed checkpoint times
				for (int i = 0; i < riderCheckpointElapsedTimes.length; i++) {
					checkpointAndTotalElapsedTime[i] = riderCheckpointElapsedTimes[i];
				}
				checkpointAndTotalElapsedTime[checkpointAndTotalElapsedTime.length - 1] = riderResult.getElapsedTime();
				return checkpointAndTotalElapsedTime;
			}
		}
		// Returns an empty array if rider has not completed the stage
		return new LocalTime[0];
	}

	////
	// cn
	private void calculateAdjustedElapsedTimes(int stageId) throws IDNotRecognisedException {
		Stage stage = getStageById(stageId);
		if (stage == null) {
			// Handle the case where the stage ID is not recognized
			return;
		}
		List<RiderResult> results = stage.getRiderResult();
		if (results.isEmpty()) {
			// Handle the case where there are no results for the stage
			return;
		}
		// Sort the results by real elapsed time
		Collections.sort(results, (r1, r2) -> r1.getElapsedTime().compareTo(r2.getElapsedTime()));
		// Initialise variables for the first rider's real and adjusted elapsed time
		long previousElapsedTime = results.get(0).getElapsedTime().toNanoOfDay() / 1_000_000;
		long adjustedElapsedTime = previousElapsedTime;
		// Iterate over the results starting from the second rider
		for (int i = 1; i < results.size(); i++) {
			RiderResult currentResult = results.get(i);
			long currentElapsedTime = currentResult.getElapsedTime().toNanoOfDay() / 1_000_000;
			// Check if the current rider finished within 1 second of the previous rider
			if (currentElapsedTime - previousElapsedTime <= 1) {
				// Set the adjusted elapsed time to the minimum of the current and previous elapsed times
				adjustedElapsedTime = Math.min(adjustedElapsedTime, currentElapsedTime);
			} else {
				// If the gap is larger than 1 second, update the previous elapsed time
				previousElapsedTime = currentElapsedTime;
				adjustedElapsedTime = currentElapsedTime; // Reset the adjusted elapsed time
			}
			// Set the adjusted elapsed time for the current rider
			currentResult.setAdjustedElapsedTime(LocalTime.ofSecondOfDay(adjustedElapsedTime));

		}
	}

	@Override
	// cn
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		Stage stage = getStageById(stageId);
		if (stage == null) {
			throw new IDNotRecognisedException("Stage ID is not recognised");
		}
		// Check if the stage contains results for the rider
		if (!stage.containsResults(riderId)) {
			return null;
		}
		// Check if the stage is not a time-trial
		if (stage.getStageType() != StageType.TT) {
			// Calculate adjusted elapsed time if not a time-trial
			calculateAdjustedElapsedTimes(stageId);
			// Iterate through the rider results to find the one with the specified rider ID
			for (RiderResult result : stage.getRiderResult()) {
				if (result.getRiderId() == riderId) {
					// Return the adjusted elapsed time of the rider
					return result.getAdjustedElapsedTime();
				}
			}
		}
		// If it's a time-trial, return the real elapsed time
		return stage.getRiderElapsedTime(riderId);
	}

	/////
	public Race getRaceByStageId(int stageId) throws IDNotRecognisedException {
		for (Race race : races) {
			for (Stage stage : race.getStages()) {
				if (stage.getStageId() == stageId) {
					return race;
				}
			}
		}
		throw new IDNotRecognisedException("No race found for the given stage ID: " + stageId);
	}

	@Override
	// n
	// wr
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		// Check if the stage ID is valid
		checkStageIdValidity(stageId);
		// Check if the rider ID is recognized
		riderIdNotRecognised(riderId);
		// Find the race and stage
		Race race = getRaceByStageId(stageId);
		Stage stage = race.getStageById(stageId);
		// Find the rider result and delete it
		stage.deleteRiderResult(riderId);
	}

	@Override
	// 25
	// us
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		// Find the desired stage
		Stage stage = getStageById(stageId);
		// Check if stage is found
		if (stage == null) {
			throw new IDNotRecognisedException("Stage ID not recognised, try another stage ID");
		}
		// Get the list of rider times in the stage
		List<Map.Entry<Rider, Long>> riderTimes = new ArrayList<>(stage.getStageTimes().entrySet());
		// Sort rider times by elapsed time
		riderTimes.sort(Map.Entry.comparingByValue());
		// Extract rider IDs in sorted order
		int[] ridersRank = new int[riderTimes.size()];
		for (int i = 0; i < riderTimes.size(); i++) {
			Rider rider = riderTimes.get(i).getKey();
			int riderId = rider.getTeamId() + i + 1; // Assuming rider index starts from 0
			ridersRank[i] = riderId;
		}
		return ridersRank;
	}

	@Override
	// us
	// cn
    public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
        // Retrieve the stage
        Stage stage = getStageById(stageId);
        if (stage == null) {
            throw new IDNotRecognisedException("Stage ID is not recognised");
        }

        // Calculate adjusted elapsed times (assuming this method exists)
        calculateAdjustedElapsedTimes(stageId);

        // Retrieve the list of adjusted elapsed times
        List<LocalTime> adjustedTimes = new ArrayList<>();
        for (RiderResult result : stage.getRiderResult()) {
            adjustedTimes.add(result.getAdjustedElapsedTime());
        }

        // Sort the adjusted elapsed times
        Collections.sort(adjustedTimes);

        // Convert the list to an array
        LocalTime[] rankedAdjustedTimes = new LocalTime[adjustedTimes.size()];
        rankedAdjustedTimes = adjustedTimes.toArray(rankedAdjustedTimes);

        return rankedAdjustedTimes;
    }

	// Helper class (used in getRidersPointsInStage)
	public Stage getStageById(int stageId) throws IDNotRecognisedException {
		for (Race race : races) {
			for (Stage stage : race.getStages()) {
				if (stage.getStageId() == stageId) {
					return stage;
				}
			}
		}
		throw new IDNotRecognisedException("Stage ID not recognised: " + stageId);
	}

	@Override
	// c
	// cn
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
		// Arrays of points for different stage types
		int[] flatStagePoints = { 50, 30, 20, 18, 16, 14, 12, 10, 8, 7, 6, 5, 4, 3, 2 };
		int[] mediumMountainStagePoints = { 30, 25, 22, 19, 17, 15, 13, 11, 9, 7, 6, 5, 4, 3, 2 };
		int[] highMountainPoints = { 20, 17, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 };
		int[] timeTrialPoints = { 20, 17, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 };
		// Find the desired stage
		Stage givenStage = getStageById(stageId);
		// Check if stage is found
		if (givenStage == null) {
			throw new IDNotRecognisedException("Stage ID is not recognised");
		}
		// Determine points array based on stage type
		int[] pointsArray;
		if (givenStage.getStageType() == StageType.FLAT) {
			pointsArray = flatStagePoints;
		} else if (givenStage.getStageType() == StageType.MEDIUM_MOUNTAIN) {
			pointsArray = mediumMountainStagePoints;
		} else if (givenStage.getStageType() == StageType.HIGH_MOUNTAIN) { // StageType.HIGH_MOUNTAIN or StageType.TT
			pointsArray = highMountainPoints;
		} else if (givenStage.getStageType() == StageType.TT) { // StageType.HIGH_MOUNTAIN or StageType.TT
			pointsArray = timeTrialPoints;
		} else {
			return null;
		}
		// Get rider times in the stage
		List<Map.Entry<Rider, Long>> riderTimes = new ArrayList<>(givenStage.getStageTimes().entrySet());
		// Sort rider times by elapsed time
		riderTimes.sort(Map.Entry.comparingByValue());
		// Initialise arrays to store points and rider points
		int[] pointsArrayToReturn = new int[riderTimes.size()];
		Map<Rider, Integer> riderPoints = new HashMap<>();
		// Assign points to riders based on their positions
		for (int i = 0; i < riderTimes.size(); i++) {
			int points = (i < pointsArray.length) ? pointsArray[i] : 0;
			pointsArrayToReturn[i] = points;
			riderPoints.put(riderTimes.get(i).getKey(), points);
		}
		// Set rider points in the stage
		givenStage.setRiderPoints(riderPoints);
		// Assertion to check if output matches riders' rank in the stage
		assert (Arrays.equals(pointsArrayToReturn, getRidersRankInStage(stageId)))
				: "Output does not match riders' rank in stage";
		return pointsArrayToReturn;
	}

	@Override
	// cn
	// c
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		int[] hcPoints = { 20, 15, 12, 10, 8, 6, 4, 2 };
		int[] oneCPoints = { 10, 8, 6, 4, 2, 1 };
		int[] twoCPoints = { 5, 3, 2, 1 };
		int[] threeCPoints = { 2, 1 };
		int[] fourCPoints = { 1 };

		Stage givenStage = getStageById(stageId);

		if (givenStage == null) {
			throw new IDNotRecognisedException("Stage ID is not recognised");
		}
		// FIX
		// IMPLEMENT SPRINT
		int[] pointsArray;
		if (givenStage.getCheckpointType() == CheckpointType.HC) {
			pointsArray = hcPoints;
		} else if (givenStage.getCheckpointType() == CheckpointType.C1) {
			pointsArray = oneCPoints;
		} else if (givenStage.getCheckpointType() == CheckpointType.C2) {
			pointsArray = twoCPoints;
		} else if (givenStage.getCheckpointType() == CheckpointType.C3) {
			pointsArray = threeCPoints;
		} else if (givenStage.getCheckpointType() == CheckpointType.C4) {
			pointsArray = fourCPoints;
		} else {
			return null;
		}

		List<Map.Entry<Rider, Long>> riderTimes = new ArrayList<>(givenStage.getRiderTimes().entrySet());
		riderTimes.sort(Map.Entry.comparingByValue());

		Map<Rider, Integer> riderPointsMap = new HashMap<>();
		for (int i = 0; i < riderTimes.size(); i++) {
			int points = (i < pointsArray.length) ? pointsArray[i] : 0;
			Rider rider = riderTimes.get(i).getKey();
			riderPointsMap.put(rider, points);
		}

		// Convert riderPointsMap to the ranked list of mountain points
		int[] rankedListOfMountainPoints = new int[riderPointsMap.size()];
		int index = 0;
		for (Map.Entry<Rider, Integer> entry : riderPointsMap.entrySet()) {
			rankedListOfMountainPoints[index++] = entry.getValue();
		}

		return rankedListOfMountainPoints;
	}

	@Override
	// n
	public void eraseCyclingPortal() {
		// TODO Auto-generated method stub
		teams = new ArrayList<>();
		riders = new ArrayList<>();
		races = new ArrayList<>();
	}

	@Override
	// cn
	public void saveCyclingPortal(String filename) throws IOException {
		// TODO Auto-generated method stub
		FileOutputStream file = new FileOutputStream(filename);
		ObjectOutputStream out = new ObjectOutputStream(file);
		out.close();
	}

	@Override
	// cn
	// us
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		FileInputStream fileIn = new FileInputStream(filename);
		ObjectInputStream in = new ObjectInputStream(fileIn);
		in.close();
		fileIn.close();

	}

	public class Stage {
		private int stageId;
		private int raceId;
		private String stageName;
		private String description;
		private StageType stageType;
		private StageState stageState;
		private LocalTime startTime;
		private LocalTime finishTime;
		private double length;
		private Map<Rider, Long> stageTimes;
		private List<Checkpoint> checkpoints = new ArrayList<>();
		private List<RiderResult> riderResults;
		private Map<Rider, Integer> riderPoints;
		private List<Double> intermediateSprints;
		private CheckpointType mountainType;

		// Constructor
		public Stage(int raceId, String stageName, String description, double length, LocalTime startTime,
		StageType type) {
			this.stageName = stageName;
			this.description = description;
			this.length = length;
			this.startTime = startTime;
			this.raceId = raceId;
			this.stageType = type;
		}

		// Getter methods
		public int getStageId() {
			return stageId;
		}

		public LocalTime getStartTime() {
			return startTime;
		}

		public LocalTime getFinishTime() {
			return finishTime;
		}

		public double getLength() {
			return length;
		}

		public Map<Rider, Long> getStageTimes() {
			return stageTimes;
		}

		public StageState getStageState() {
			return stageState;
		}

		public StageType getStageType() {
			return stageType;
		}

		public CheckpointType getCheckpointType() {
			return mountainType;
		}

		public List<Checkpoint> getCheckpoints() {
			return checkpoints;
		}

		public List<RiderResult> getRiderResult() {
			return riderResults;
		};

		public LocalTime getRiderElapsedTime(int riderId) {
			for (RiderResult result : riderResults) {
				if (result.getRiderId() == riderId) {
					return result.getElapsedTime();
				}
			}
			return null; // If no result found for the rider
		}

		public boolean containsResults(int riderId) {
			for (RiderResult result : riderResults) {
				if (result.getRiderId() == riderId) {
					return true;
				}
			}
			return false;
		}

		public void addRider(Rider rider) {
			riders.add(rider);
		}

		public Map<Rider, Long> getRiderTimes() {
			return stageTimes;
		}

		public void deleteRiderResult(int riderId) throws IDNotRecognisedException {
			boolean found = false;
			Iterator<RiderResult> iterator = riderResults.iterator();
			while (iterator.hasNext()) {
				RiderResult result = iterator.next();
				if (result.getRiderId() == riderId) {
					iterator.remove();
					found = true;
					break;
				}
			}
			if (!found) {
				throw new IDNotRecognisedException("Rider result not found for the specified rider ID in this stage.");
			}
		}

		public void setState(StageState state) {
			this.stageState = state;
		}

		public void setType(StageType type) {
			this.stageType = type;
		}

		public void addCheckpoint(Checkpoint checkpoint) {
			checkpoints.add(checkpoint);
		}

		public void setRiderPoints(Map<Rider, Integer> riderPoints) {
			this.riderPoints = riderPoints;
		}

		// Method to add stage time for a rider
		public void addStageTime(Rider rider, Long time) {
			stageTimes.put(rider, time);
		}

		public boolean hasRiderResult(int riderId) {
			for (RiderResult result : riderResults) {
				if (result.getRiderId() == riderId) {
					return true;
				}
			}
			return false;
		}

		public void addIntermediateSprint(double location) throws InvalidLocationException {
			// Check if the location is valid
			if (location < 0 || location > length) {
				throw new InvalidLocationException("Invalid intermediate sprint location.");
			}

			// Add the intermediate sprint to the list
			intermediateSprints.add(location);
		}

		public void addRiderResult(RiderResult result, LocalTime[] checkpointTimes)
				throws InvalidCheckpointTimesException {
			// Check if the length of checkpointTimes is valid
			if (checkpointTimes.length != this.checkpoints.size() + 2) {
				throw new InvalidCheckpointTimesException("Invalid number of checkpoint times.");
			}

			// Set the checkpoint times for the rider result
			for (int i = 0; i < checkpointTimes.length; i++) {
				result.addCheckpointTime(checkpointTimes[i]);
			}

			// Add the rider result to the list
			riderResults.add(result);
		}

		public void nextState() {
			if (stageState == StageState.PREPARATION) {
				stageState = StageState.WAITING_FOR_RESULTS;
			} else if (stageState == StageState.WAITING_FOR_RESULTS) {
			}
		}
	}

	public class Race {
		private int raceId;
		private String name;
		private String description;
		private List<Stage> stages;

		// Constructor
		public Race(int raceId, String name, String description) {
			this.raceId = raceId;
			this.name = name;
			this.description = description;
			this.stages = new ArrayList<>();
		}

		// Getter methods
		public int getRaceId() {
			return raceId;
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public List<Stage> getStages() {
			return stages;
		}

		public Stage getStageById(int stageId) {
			for (Stage stage : stages) {
				if (stage.getStageId() == stageId) {
					return stage;
				} else {
					return null;
				}
			}
			return null;
		}

		public void addStage(Stage stage) {
			stages.add(stage);
		}
	}

	class Rider {
		private int teamId;
		private String name;
		private int yearOfBirth;
		private int riderID;
		private Team team;

		public Rider(int teamId, String name, int yearOfBirth) {
			this.teamId = teamId;
			this.name = name;
			this.yearOfBirth = yearOfBirth;
		}

		// Getter methods for accessing rider attributes
		public int getTeamId() {
			return teamId;
		}

		public String getName() {
			return name;
		}

		public int getYearOfBirth() {
			return yearOfBirth;
		}

		public void setTeam(Team team) {
			this.team = team;
		}

		public Team getTeam() {
			return team;
		}

		public int getRiderID() {
			return riderID;
		}

		public void setRiderID(int riderId) {
			this.riderID = riderId;
		}
	}

	class Team {
		private int teamId;
		private String name;
		private String description;
		private List<Rider> riders;

		// Constructor
		public Team(int teamId, String name, String description) {
			this.teamId = teamId;
			this.name = name;
			this.riders = new ArrayList<>();
			this.description = description;
		}

		// Getter methods
		public int getTeamId() {
			return teamId;
		}

		public String getName() {
			return name;
		}

		public List<Rider> getRiders() {
			return riders;
		}

		public void addRider(int riderId, String riderName, int yearOfBirth) {
			Rider rider = new Rider(teamId, riderName, yearOfBirth);
			rider.setRiderID(riderId); // Set the rider ID
			riders.add(rider);
		}

		public int createRider(String name, int yearOfBirth) {
			// Calculate the rider ID based on the size of the current rider list
			int riderId = (teamId * 100) + (riders.size() + 1);
			Rider rider = new Rider(teamId, name, yearOfBirth);
			riders.add(rider);
			return riderId;
		}

		// Method to remove a rider from the team
		public void removeRider(Rider rider) {
			riders.remove(rider);
		}

		public int getNumberOfRiders() {
			return riders.size();
		}

	}

	class Checkpoint {
		private Double location;
		private CheckpointType type;
		private Double averageGradient;
		private Double length;
		private int checkpointId;
		private List<Checkpoint> checkpoints = new ArrayList<>();

		//
		public Checkpoint(int checkpointId, Double location, CheckpointType type, Double averageGradient, Double length) {
			this.checkpointId = checkpointId;
			this.location = location;
			this.type = type;
			this.averageGradient = averageGradient;
			this.length = length;
		}

		public int getCheckpointId() {
			return checkpointId;
			}

		public void addCheckPoint(Checkpoint checkpoint) {
			checkpoints.add(checkpoint);
		}

		public Double getLocation() {
			return location;
		}
	}

	//
	class RiderResult implements Comparable<RiderResult> {
		private int riderId;
		private LocalTime[] checkpoints;
		private LocalTime elapsedTime;
		private LocalTime[] riderCheckpointElapsedTimes;
		private List<LocalTime> checkpointTimes;
		private LocalTime adjustedElapsedTime;

		public RiderResult(int riderId, LocalTime... checkpoints) {
			this.riderId = riderId;
			this.checkpoints = checkpoints;
		}

		public void setRiderCheckpointElapsedTimes(LocalTime[] riderCheckpointElapsedTimes) {
			this.riderCheckpointElapsedTimes = riderCheckpointElapsedTimes;
		}

		public LocalTime[] getRiderCheckpointElapsedTimes() {
			return riderCheckpointElapsedTimes;

		}

		public RiderResult() {
			this.checkpointTimes = new ArrayList<>();
		}

		public void addCheckpointTime(LocalTime checkpointTime) {
			checkpointTimes.add(checkpointTime);
		}

		public int getRiderId() {
			return riderId;
		}

		public LocalTime[] getCheckpoints() {
			return checkpoints;
		}

		public LocalTime getElapsedTime() {
			return elapsedTime;
		}

		public LocalTime getAdjustedElapsedTime() {
			return adjustedElapsedTime;
		}

		public void setElapsedTime(LocalTime elapsedTime) {
			this.elapsedTime = elapsedTime;
		}

		public int compareTo(RiderResult other) {
			return this.elapsedTime.compareTo(other.elapsedTime);
		}

		public void setAdjustedElapsedTime(LocalTime adjustedElapsedTime) {
			this.adjustedElapsedTime = adjustedElapsedTime;
		}
	}

	
	@Override
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime,
			cycling.StageType type) throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
		// Check if the race ID is valid
		if (raceId <= 0 || raceId > races.size()) {
			throw new IDNotRecognisedException(raceId + " is invalid.");
		}
		// Check stage name
		if (stageName == null || stageName.trim().isEmpty()) {
			throw new InvalidNameException("Please input a stage name");
		}
		// Check for spaces in stage name
		if (stageName.contains(" ")) {
			throw new InvalidNameException("Stage name cannot contain spaces");
		}
		// Check stage name length
		if (stageName.length() > 30) {
			throw new InvalidNameException("Stage name must be 30 characters or fewer");
		}
		// Check stage description length
		if (description != null && description.length() > 100) {
			throw new InvalidNameException("Stage description must be 100 characters or fewer");
		}
		// Check stage length
		if (length < 5) {
			throw new InvalidLengthException("Stage length must be 5km or greater");
		}
		// Get the race by ID
		Race race = races.get(raceId - 1);
		// Check if the race exists
		if (race == null) {
			throw new IDNotRecognisedException("Race with ID " + raceId + " does not exist.");
		}
		int numberOfStages = getNumberOfStages(raceId);
		int stageId = (raceId * 100) + (numberOfStages + 1);
		System.out.println(stageId);
		// Convert LocalDateTime to LocalTime for startTime
		LocalTime localStartTime = startTime.toLocalTime();
		// Create the Stage object using localStartTime
		Stage newStage = new Stage(raceId, stageName, description, length, localStartTime, type); //FIX THIS
		stages.add(newStage);
		race.addStage(newStage);
		return stageId;
	}
	}
