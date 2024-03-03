package cycling;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * BadCyclingPortal is a minimally compiling, but non-functioning implementor
 * of the CyclingPortal interface.
 * 
 * @author Diogo Pacheco
 * @version 2.0
 *
 */
public class CyclingPortalImpl implements CyclingPortal {

	public static ArrayList<Race> races = new ArrayList<>(); // arraylist gets declared once and puts the Race
																// objects inside
	// Method below is coded by Aritra

	public void raceIDNotRecognisedException(int raceId) throws IDNotRecognisedException {

		if (raceId > races.size() || raceId <= 0) { // Checks if ID entered is greater or less than list if it is
													// exception is thrown
			throw new IDNotRecognisedException("There is no race with ID:" + raceId);

		}

	}
	// Method below is coded by Aritra

	public void invalidNameException(String name) throws InvalidNameException { // lower case i to distinguish exception
																				// method from exception
		if (name.contains(" ") || name.length() == 0) { // Race names cant have whitspace and must be of length >=1
			throw new InvalidNameException();
		}

	}
	// Method below is coded by Aritra

	public void raceIllegalNameException(String name) throws IllegalNameException {
		for (int i = races.size(); i > 0; i--) { // Looks at the names of all previous race objects and throws exception
			// if matching name is found

			Race raceObject = races.get(i - 1);
			String raceName = raceObject.getName();
			if (raceName.equals(name)) {
				System.out.println(name);
				System.out.println(raceObject);

				throw new IllegalNameException();
			}

		}

	}
	// Method below is coded by Aritra

	public void stageIllegalNameException(String name) throws IllegalNameException {
		for (int i = races.size(); i > 0; i--) { // loops through all the previous races
			Race raceObject = races.get(i - 1);
			List<Stage> stages = raceObject.getStages();

			for (int j = stages.size(); j > 0; j--) { // loops through all the stages within that race to find if any
														// matches the name
				Stage stage = stages.get(j - 1);
				String stageName = stage.getName();
				if (stageName.equals(name)) {
					throw new IllegalNameException();
				}
			}

		}

	}

	// mei
	public void stageIDNotRecognisedException(int stageId) throws IDNotRecognisedException { // made this into a method
																								// since it will get
																								// reused alot
		int raceId = stageId / 100;
		int stageIndex = (stageId % 100) - 1;

		// Check if raceId is valid
		raceIDNotRecognisedException(raceId);
		Race race = races.get(raceId - 1);
		List<Stage> stages = race.getStages();

		// Check if stageIndex is valid
		if (stageIndex < 0 || stageIndex >= stages.size()) {
			throw new IDNotRecognisedException("Stage ID not recognised for race " + raceId);
		}

	}

	@Override
	// Method below is coded by Aritra
	public int[] getRaceIds() {
		// TODO Auto-generated method stub
		System.out.println("getRaceIds is running");

		int[] raceIds = new int[races.size()];

		for (int i = 0; i < races.size(); i++) { // loops over each object in array
			Race race = races.get(i);
			String raceName = race.getName();
			raceIds[i] = i + 1;
			System.out.println(raceIds[i] + "," + raceName); // will output id and the name associated with the id
		}

		return raceIds;
	}

	@Override
	// Method below is coded by Aritra
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
		// TODO Auto-generated method stub
		System.out.println("createRace is running");

		invalidNameException(name);
		raceIllegalNameException(name);
		// Creates arraylist to put all the objects(Race) in. Racinfo consists of 2
		// items. Name and description.
		Race race = new Race(name, description);
		races.add(race);
		int raceId = races.size(); // raceId is the size of the array so first Id will be 1
		System.out.println("You created this race with id:" + raceId + " name:" + name);
		race.getName();
		return raceId; // the race id is the position the object is in the array

	}

	// Method below is coded by Aritra
	class Race { // This class can create Race objects(race constructor). Each object has 3
					// values within it.
					// The name and description and the stage list. The properties can be used to
					// get the name
					// description and stage list respectively.
		private String name;
		private String description;
		private List<Stage> stages;

		public Race(String name, String description) {
			this.name = name;
			this.description = description;
			this.stages = new ArrayList<>();

		}

		public void addStage(Stage stage) {
			stages.add(stage);
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

		public void setStages(List<Stage> stages) {
			this.stages = stages;
		}

	}
	// Method below is coded by Aritra

	class Stage { // stage class to store information about stages takes in 5 parameters(stage
					// constructor). Each
					// stage object gets put in a list which is in races called stages.
		private String name;
		private String description;
		private double length;
		private LocalDateTime startTime;
		private StageType stageType;
		private List<Checkpoint> checkpoints; // this list can store intermediate sprints and climb checkpoint objects

		public Stage(String name, String description, double length, LocalDateTime startTime, StageType type) {
			this.name = name;
			this.description = description;
			this.length = length;
			this.startTime = startTime;
			this.checkpoints = new ArrayList<>();

		}

		// Getters and setters
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public double getLength() {
			return length;
		}

		public void setLength(double length) {
			this.length = length;
		}

		public LocalDateTime getStartTime() {
			return startTime;
		}

		public void setStartTime(LocalDateTime startTime) {
			this.startTime = startTime;
		}

		public StageType getStageType() {
			return stageType;
		}

		public void setStageType(StageType stageType) {
			this.stageType = stageType;
		}

		public List<Checkpoint> getCheckpoints() {

			return checkpoints;
		}

		public void addCheckPoint(Checkpoint checkpoint) {
			checkpoints.add(checkpoint);
		}

	}
	@Override
	// Method below is coded by Aritra

	public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		System.out.println("viewRaceDetails is running");
		raceIDNotRecognisedException(raceId);
		Race race = races.get(raceId - 1); // Gets object at the ID specified -1 since ID 1 will represent the 0 item in
											// the array
		String raceName = race.getName(); // Splits the objcet up into name and description variables
		String raceDescription = race.getDescription();
		String raceDetails = raceName + "," + raceDescription; // Comma added to distinguish name from variables.
		System.out.println(raceDetails);
		return raceDetails;

	}

	@Override
	// Method below is coded by Aritra

	public void removeRaceById(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		System.out.println("removeRaceById is running");

		raceIDNotRecognisedException(raceId);
		races.remove(raceId - 1); // Removes object at the ID specified -1 , this may change the raceId of other
									// objects because the ID is based on position in the arraylist

	}

	@Override
	// Method below is coded by Aritra

	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		System.out.println("getNumberOfStages is running");

		raceIDNotRecognisedException(raceId);
		Race race = races.get(raceId - 1);
		List<Stage> stages = race.getStages();
		int numberOfStages = stages.size();
		System.out.println(numberOfStages);
		return numberOfStages;
	}

	@Override
	// Method below is coded by Aritra still have to add error handling for the
	// length exception
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime,
			StageType type)
			throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
		// TODO Auto-generated method stub
		System.out.println("addStageToRace is running");
		raceIDNotRecognisedException(raceId);
		invalidNameException(stageName);
		stageIllegalNameException(stageName);
		// Check if the length is valid (greater than 0)
		if (length < 5) {
			throw new InvalidLengthException("Stage length must be greater than 5");
		}
		Stage stage = new Stage(stageName, description, length, startTime, type);
		Race race = races.get(raceId - 1);
		race.addStage(stage);
		List<Stage> raceStages = race.getStages(); // Stages within the race
		int stageId = (raceId * 100) + raceStages.size(); // First number represents the race id the last number is the
															// stage within that. E.g. 105 will be race ID 1 and stage 5
		return stageId;
	}

	@Override
	// Mei
	public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		System.out.println("getRaceStages is running");
		raceIDNotRecognisedException(raceId);
		Race race = races.get(raceId - 1);
		List<Stage> stages = race.getStages();
		int[] stageIds = new int[stages.size()];
		for (int i = 0; i < stages.size(); i++) {
			stageIds[i] = (raceId * 100) + (i + 1);
			System.out.println("stageIDs:"+stageIds[i]);
		}
		return stageIds;
	}

	@Override
	// Aritra
	public double getStageLength(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		System.out.println("getStageLength is running");

		// Check if stageId is valid
		stageIDNotRecognisedException(stageId); // I put all code into a method since we will need to resue it alot.
		int raceId = stageId / 100;
		int stageIndex = (stageId % 100) - 1;

		Race race = races.get(raceId - 1);
		List<Stage> stages = race.getStages();

		Stage stage = stages.get(stageIndex);
		double stageLength = stage.getLength();
		System.out.println("Length at stage ID:" + stageId + " is " + stageLength);
		return stageLength;
	}


	@Override
	// Aritra
	public void removeStageById(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		stageIDNotRecognisedException(stageId);
		int raceId = stageId / 100;
		int stageIndex = (stageId % 100) - 1;
		Race race = races.get(raceId - 1);
		List<Stage> stages = race.getStages();
		stages.remove(stageIndex);
		System.out.println("removed stage id:" + stageId);
	}

	// Aritra
	class Checkpoint { // Parent of Climbcheckpoint and Intermediate sprint
		private double location;

		public Checkpoint(double location) {
			this.location = location;

		}

		public double getLocation() {
			return location;
		}
	}

	// Aritra

	class ClimbCheckpoint extends Checkpoint { // constructor for creating climbCheckpoints. This will be stored in the
												// stage as List<Checkpoint> child of checkpoint
		private double averageGradient;
		private double length;
		private CheckpointType checkpointType;

		public ClimbCheckpoint(double location, CheckpointType type, double averageGradient, double length) {
			super(location);
			this.averageGradient = averageGradient;
			this.length = length;

		}

		public CheckpointType getCheckpointType() {

			return checkpointType;
		}

		public double getaverageGradient() {
			return averageGradient;
		}

		public double getLength() {
			return length;
		}
	}

	// Aritra
	class IntermediateSprint extends Checkpoint { // constructor for creating checkpoints. This will be stored in the
		// stage as a List<Checkpoint>. Child of checkpoint

		public IntermediateSprint(double location) {
			super(location);

		}

	}

	@Override
	// Aritra
	public int addCategorizedClimbToStage(int stageId, Double location, CheckpointType type, Double averageGradient,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException { // Main part of method coded still need to add error handling

		System.out.println("Add climb checkpoint is running");
		stageIDNotRecognisedException(stageId);
		ClimbCheckpoint climbCheckpoint = new ClimbCheckpoint(location, type, averageGradient, length); // creates new
																										// checkpoint
																										// with
		// requires parameters
		int raceId = stageId / 100; // gets raceId and stage index
		int stageIndex = (stageId % 100) - 1;
		Race race = races.get(raceId - 1); // gets race specified at the id and gets the stages within that race
		List<Stage> stages = race.getStages();
		Stage stage = stages.get(stageIndex); // gets the stage at the index specified
		stage.addCheckPoint(climbCheckpoint); // adds checkpoint to that stage
		List<Checkpoint> checkpoints = stage.getCheckpoints(); // gets the checkpoint list
		int checkpointId = stageId * 100 + checkpoints.size(); // checkpointId of 10501 is race 1 stage 5 checkpoint 1
		// TODO Auto-generated method stub
		return checkpointId;
	}

	@Override
	// Aritra
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
		// TODO Auto-generated method stub
		stageIDNotRecognisedException(stageId);
		IntermediateSprint intermediateSprint = new IntermediateSprint(location); // creates new checkpoint with
																					// requires parameters
		int raceId = stageId / 100; // gets raceId and stage index
		int stageIndex = (stageId % 100) - 1;
		Race race = races.get(raceId - 1); // gets race specified at the id and gets the stages within that race
		List<Stage> stages = race.getStages();
		Stage stage = stages.get(stageIndex); // gets the stage at the index specified
		stage.addCheckPoint(intermediateSprint); // adds checkpoint to that stage
		List<Checkpoint> checkpoints = stage.getCheckpoints(); // gets the checkpoint list
		int checkpointId = stageId * 100 + checkpoints.size(); // checkpointId of 10501 is race 1 stage 5 checkpoint 1
		// TODO Auto-generated method stub
		return checkpointId;
	}


	@Override
	public void removeCheckpoint(int checkpointId) throws IDNotRecognisedException, InvalidStageStateException {
		// TODO Auto-generated method stub

	}

	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
		// TODO Auto-generated method stub

	}

	@Override
	// Aritra
	public int[] getStageCheckpoints(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		int raceId = stageId / 100; // gets raceId and stage index
		int stageIndex = (stageId % 100) - 1;
		Race race = races.get(raceId - 1); // gets race specified at the id and gets the stages within that race
		List<Stage> stages = race.getStages();
		Stage stage = stages.get(stageIndex); // gets the stage at the index specified
		List<Checkpoint> checkpoints = stage.getCheckpoints();
		int checkpointIdFrame = stageId * 100; // will have form 10100 this means i can add checkpoint ID to it
		int[] checkpointId = new int[checkpoints.size()];
		for (int i = 0; i < checkpoints.size(); i++) {
			Checkpoint checkpoint = checkpoints.get(i);
			checkpointId[i] = checkpointIdFrame + i + 1;
			System.out.println(checkpointId[i]);
			if (checkpoint instanceof ClimbCheckpoint) {
				System.out.println("Climb checkpoint");
				// Use climbCheckpoint
			} else if (checkpoint instanceof IntermediateSprint) {
				// Use sprintCheckpoint
				System.out.println("Intermediate sprint");

			}
		}

		return checkpointId;
	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
		// TODO Auto-generated method stub
		if (name == null || name.isEmpty()) {
	            throw new InvalidNameException("Team name cannot be empty");
	        }
	        if (name.contains(" ")) {
	            throw new IllegalNameException("Team name cannot contain spaces");
	        }
	
	        Team team = new Team(name, description);
	        teams.add(team);
	        return teams.size();
	}

	@Override
	// Mei
	public void removeTeam(int teamId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		// Check if team ID is valid
		if (teamId <= 0 || teamId > teams.size()) {
			throw new IDNotRecognisedException("Team Id " + teamId + "not recognised");
		}
		teams.remove(teamId - 1);

	}

	@Override
	// Mei
	public int[] getTeams() {
		// TODO Auto-generated method stub
		int[] teamIds = new int[teams.size()];
		for (int i = 0; i < teams.size(); i++) {
			teamIds[i] = i + 1;
		}
		return teamIds;
	}

	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		ArrayList<Integer> riderIds = new ArrayList<>();
	        boolean found = false
			
	        for (Race race : races) {
	            for (Stage stage : race.getStages()) {
	                for (Rider rider : stage.getRiders()) {
	                    if (rider.getTeamId() == teamId) {
	                        riderIds.add(rider.getId());
	                        found = true;
	                        }
	                    }
	                }
	            }
	        if (!found) { 
	            throw new IDNotRecognisedException("Team ID " + teamId + " not recognised.");
	        }
	
	        int[] result = new int[riderIds.size];
	        for (int i = 0; i < riderIds.size(); i++) {
	            result[i] = riderIds.get(i);
	        }
	        return result;
		}
	}

	@Override
	public int createRider(int teamID, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		boolean teamFound = false;
	        for (Race race : races) {
	            for (Team team : race.getTeams()) {
	                if (team.getId() == teamID) {
	                    teamFound = true;
	                    break;
	                }
	            }
	        }
	        if (!teamFound) {
	            throw new IDNotRecognisedException("Team ID " + teamID " not recognised");
	        }
	        if (yearOfBirth < 1900 || yearOfBirth > 2024) {
	            throw new IllegalArgumentException("Please enter your correct year of birth in YYYY format");
	        }
	        int riderId = generateRiderId();
	        Rider rider = new Rider(riderId, teamID, name, yearOfBirth);
	        riders.add(rider);
	        return riderId;
	    }
	    private int generateRiderId() {
	        return riders.size() + 1;
	}

	@Override
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointTimesException,
			InvalidStageStateException {
		// TODO Auto-generated method stub

	}

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub

	}

	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eraseCyclingPortal() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveCyclingPortal(String filename) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeRaceByName(String name) throws NameNotRecognisedException {
		// TODO Auto-generated method stub

	}

	@Override
	public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

}
