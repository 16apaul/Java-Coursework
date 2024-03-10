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
	public static ArrayList<Team> teams = new ArrayList<>();
	// Method below is coded by Aritra

	public void raceIDNotRecognisedException(int raceId) throws IDNotRecognisedException {

		if (raceId > races.size() || raceId <= 0) { // Checks if ID entered is greater or less than list if it is
													// exception is thrown
			throw new IDNotRecognisedException("There is no race with ID:" + raceId);

		}

	}

	// Aritra
	public void stageStateExceptionWaitingForResults(int stageId) throws InvalidStageStateException {

		int raceId = stageId / 100; // gets raceId and stage index
		int stageIndex = (stageId % 100) - 1;
		Race race = races.get(raceId - 1); // gets race specified at the id and gets the stages within that race
		List<Stage> stages = race.getStages();
		Stage stage = stages.get(stageIndex); // gets the stage at the index specified
		StageState stageState = stage.getStageState();
		if (stageState == StageState.WAITING_FOR_RESULTS) {

			throw new InvalidStageStateException(
					"Can't do this action invalid stage state, current state is:" + stageState);
		}
	}

	// Aritra
	public void stageStateExceptionAddingCheckpoints(int stageId) throws InvalidStageStateException {

		int raceId = stageId / 100; // gets raceId and stage index
		int stageIndex = (stageId % 100) - 1;
		Race race = races.get(raceId - 1); // gets race specified at the id and gets the stages within that race
		List<Stage> stages = race.getStages();
		Stage stage = stages.get(stageIndex); // gets the stage at the index specified
		StageState stageState = stage.getStageState();
		if (stageState == StageState.ADDING_CHECKPOINTS) {

			throw new InvalidStageStateException(
					"Can't do this action invalid stage state, current state is:" + stageState);
		}
	}
	// Aritra

	public void checkpointLocationException(int stageId, double location) throws InvalidLocationException {

		int raceId = stageId / 100; // gets raceId and stage index
		int stageIndex = (stageId % 100) - 1;
		Race race = races.get(raceId - 1); // gets race specified at the id and gets the stages within that race
		List<Stage> stages = race.getStages();
		Stage stage = stages.get(stageIndex); // gets the stage at the index specified
		double stageLength = stage.getLength();
		if (stageLength <= location || 0 > location) {

			throw new InvalidLocationException("Enter location in range:" + stageLength + " and " + 0);
		}

	}
	// Aritra

	public void stageTypeExeptionTT(int stageId) throws InvalidStageTypeException { // TT for time trial

		int raceId = stageId / 100; // gets raceId and stage index
		int stageIndex = (stageId % 100) - 1;
		Race race = races.get(raceId - 1); // gets race specified at the id and gets the stages within that race
		List<Stage> stages = race.getStages();
		Stage stage = stages.get(stageIndex); // gets the stage at the index specified
		StageType stageType = stage.getStageType();

		if (stageType == StageType.TT) { // cant add checkpoints to time trials

			throw new InvalidStageTypeException("cant add checkpoints to time trials: " + StageType.TT);
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

				throw new IllegalNameException("name is already being used at race ID: " + i);
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
					throw new IllegalNameException("name is already being used at stage ID: " + i * 10 + j);
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
			throw new IDNotRecognisedException("Stage ID not recognised for the stage" + stageId);
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
		private StageState stageState;
		private List<RiderResult> riderResults;

		public Stage(String name, String description, double length, LocalDateTime startTime, StageType stageType) {
			this.name = name;
			this.description = description;
			this.length = length;
			this.startTime = startTime;
			this.stageType = stageType;
			this.stageState = StageState.ADDING_CHECKPOINTS; // default parameter
			this.checkpoints = new ArrayList<>();
			this.riderResults = new ArrayList<>();

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

		public StageState getStageState() {
			return stageState;
		}

		public void nextState() {

			if (stageState == StageState.ADDING_CHECKPOINTS) {
				stageState = StageState.WAITING_FOR_RESULTS;

			} else if (stageState == StageState.WAITING_FOR_RESULTS) {

			}

		}

		public void addRiderResult(RiderResult riderResult) {
			riderResults.add(riderResult);
		}

		public List<RiderResult> getRiderResult() {
			return riderResults;
		};

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
	// Method below is coded by Aritra
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
		StageType stageType = stage.getStageType();
		System.out.println(stageType + "," + stage.getLength() + "," + stage.getName());
		Race race = races.get(raceId - 1);
		race.addStage(stage);
		List<Stage> raceStages = race.getStages(); // Stages within the race
		int stageId = (raceId * 100) + raceStages.size(); // First number represents the race id the last number is the
															// stage within that. E.g. 105 will be race ID 1 and stage 5
		assert stageType != null;
		assert stageName != null;
		assert stageId > 100;
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
			System.out.println("stageIDs:" + stageIds[i]);
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

	@Override
	// Aritra
	public int addCategorizedClimbToStage(int stageId, Double location, CheckpointType type, Double averageGradient,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException { // Main part of method coded still need to add error handling

		System.out.println("Add climb checkpoint is running");
		stageIDNotRecognisedException(stageId);
		stageStateExceptionWaitingForResults(stageId);
		checkpointLocationException(stageId, location);
		stageTypeExeptionTT(stageId);
		System.out.println(stageId);
		int raceId = stageId / 100; // gets raceId and stage index
		int stageIndex = (stageId % 100) - 1;
		Race race = races.get(raceId - 1); // gets race specified at the id and gets the stages within that race
		List<Stage> stages = race.getStages();
		Stage stage = stages.get(stageIndex); // gets the stage at the index specified

		ClimbCheckpoint climbCheckpoint = new ClimbCheckpoint(location, type, averageGradient, length); // creates new
																										// checkpoint
																										// with
		// requires parameters

		stage.addCheckPoint(climbCheckpoint); // adds checkpoint to that stage
		List<Checkpoint> checkpoints = stage.getCheckpoints(); // gets the checkpoint list
		int checkpointId = stageId * 100 + checkpoints.size(); // checkpointId of 10501 is race 1 stage 5 checkpoint 1
		// TODO Auto-generated method stub
		assert checkpointId >= 10101;

		return checkpointId;
	}

	@Override
	// Aritra

	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
		// TODO Auto-generated method stub
		System.out.println("Add sprint checkpoint is running");

		stageIDNotRecognisedException(stageId);
		stageStateExceptionWaitingForResults(stageId);
		checkpointLocationException(stageId, location);
		stageTypeExeptionTT(stageId);
		System.out.println(stageId);
		int raceId = stageId / 100; // gets raceId and stage index
		int stageIndex = (stageId % 100) - 1;
		Race race = races.get(raceId - 1); // gets race specified at the id and gets the stages within that race
		List<Stage> stages = race.getStages();
		Stage stage = stages.get(stageIndex); // gets the stage at the index specified

		IntermediateSprint intermediateSprint = new IntermediateSprint(location); // creates new checkpoint with
																					// requires parameters

		stage.addCheckPoint(intermediateSprint); // adds checkpoint to that stage
		List<Checkpoint> checkpoints = stage.getCheckpoints(); // gets the checkpoint list
		int checkpointId = stageId * 100 + checkpoints.size(); // checkpointId of 10501 is race 1 stage 5 checkpoint 1
		// TODO Auto-generated method stub
		assert checkpointId >= 10101;

		return checkpointId;
	}

	public void checkpointIDNotRecognised(int checkpointId) throws IDNotRecognisedException {
		int stageId = checkpointId / 100;
		int stageIndex = (stageId % 100) - 1;
		int checkpointIndex = (checkpointId % (stageId * 100)) - 1;
		int raceId = stageId / 100;
		raceIDNotRecognisedException(raceId);
		stageIDNotRecognisedException(stageId);

		Race race = races.get(raceId - 1); // gets race specified at the id and gets the stages within that race
		List<Stage> stages = race.getStages();
		Stage stage = stages.get(stageIndex);
		List<Checkpoint> checkpoints = stage.getCheckpoints();

		if (checkpoints.size() <= checkpointIndex || checkpointIndex < 0) {
			System.out.println(checkpoints.size());
			System.out.println(checkpointIndex);

			throw new IDNotRecognisedException("Checkpoint Id not found for:" + checkpointId);
		}
	}

	@Override
	// Aritra
	public void removeCheckpoint(int checkpointId) throws IDNotRecognisedException, InvalidStageStateException {
		// TODO Auto-generated method stub
		System.out.println("remove checkpoint is running:" + checkpointId);
		int stageId = checkpointId / 100;
		checkpointIDNotRecognised(checkpointId);
		stageStateExceptionWaitingForResults(stageId);
		int stageIndex = (stageId % 100) - 1;
		int checkpointIndex = (checkpointId % (stageId * 100)) - 1;
		int raceId = stageId / 100;
		Race race = races.get(raceId - 1); // gets race specified at the id and gets the stages within that race
		List<Stage> stages = race.getStages();
		Stage stage = stages.get(stageIndex);
		List<Checkpoint> checkpoints = stage.getCheckpoints();

		assert checkpointIndex < 100;
		checkpoints.remove(checkpointIndex);

	}

	@Override
	// Aritra
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
		// TODO Auto-generated method stub
		stageIDNotRecognisedException(stageId);
		stageStateExceptionWaitingForResults(stageId);
		System.out.println("conclude stage preparations are running");
		int raceId = stageId / 100; // gets raceId and stage index
		int stageIndex = (stageId % 100) - 1;
		Race race = races.get(raceId - 1); // gets race specified at the id and gets the stages within that race
		List<Stage> stages = race.getStages();
		Stage stage = stages.get(stageIndex); // gets the stage at the index specified

		stage.nextState(); // goes to waiting for results state

	}

	@Override
	// Aritra
	public int[] getStageCheckpoints(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		System.out.println("getStageCheckpoints is running");
		stageIDNotRecognisedException(stageId);
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
			if (checkpoint instanceof ClimbCheckpoint) {
				System.out.println("Climb checkpoint:" + checkpointId[i]);
				// Use climbCheckpoint
			} else if (checkpoint instanceof IntermediateSprint) {
				// Use sprintCheckpoint
				System.out.println("Intermediate sprint:" + checkpointId[i]);

			}
		}

		return checkpointId;
	}

	class Team {
		private String name;
		private String description;
		private List<Rider> riders; // team stores a list of rider object

		public Team(String name, String description) {
			this.name = name;
			this.description = description;
			this.riders = new ArrayList<>();
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public void addRider(Rider rider) {
			riders.add(rider);
		}

		public List<Rider> getRiders() {
			return riders;
		}
	}

	class Rider {
		private int teamID;
		private String name;
		private int yearOfBirth;

		public Rider(int teamID, String name, int yearOfBirth) {
			this.teamID = teamID;
			this.name = name;
			this.yearOfBirth = yearOfBirth;
		}

		// Getter methods for accessing rider attributes
		public int getTeamID() {
			return teamID;
		}

		public String getName() {
			return name;
		}

		public int getYearOfBirth() {
			return yearOfBirth;
		}

	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
		// TODO Auto-generated method stub
		System.out.println("createTeam is running");
		invalidNameException(name);

		// Check if name is already being used for another team
		for (int i = 0; i < teams.size(); i++) {
			Team team = teams.get(i);
			if (team.getName().equals(name)) {
				throw new IllegalNameException("Team name is already being used.");
			}

		}

		Team team = new Team(name, description);

		teams.add(team);

		System.out.println("Team created with ID:" + teams.size());

		// team ID
		return teams.size();
	}

	@Override
	// Mei
	public void removeTeam(int teamId) throws IDNotRecognisedException {
		// Check if the team ID is valid
		teamIdNotRecognised(teamId);

		// Remove the team from the list of teams
		teams.remove(teamId - 1); // Adjust for 0-based indexing
	}

	@Override
	// Mei
	public int[] getTeams() {
		// Create an array to store team IDs
		System.out.println("GetTeams is running");
		int[] teamIds = new int[teams.size()];

		// Populate the array with team IDs
		for (int i = 0; i < teams.size(); i++) {
			teamIds[i] = i + 1; // fills array with numbers since those are the team ids
			System.out.println(teamIds[i]);
		}

		return teamIds;
	}

	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		teamIdNotRecognised(teamId);

		return null;
	}

	// Method to check if a teamID exists
	public void teamIdNotRecognised(int teamId) throws IDNotRecognisedException {
		// Check if the team ID is valid
		if (teamId <= 0 || teamId > teams.size()) {
			throw new IDNotRecognisedException("Team ID " + teamId + " not recognised");
		}

	}

	@Override
	// Aritra
	public int createRider(int teamID, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {
		// Check if teamID is valid
		System.out.println("createRider is running");
		teamIdNotRecognised(teamID);

		// Validate the year of birth
		if (yearOfBirth < 1900 || yearOfBirth > 2024) {
			throw new IllegalArgumentException("Invalid year of birth");
		}
		Team team = teams.get(teamID - 1); // teams is an arraylist that stores team objects
		Rider rider = new Rider(teamID, name, yearOfBirth);

		List<Rider> riders = team.getRiders();
		team.addRider(rider); // Add rider to the list of riders
		int riderSize = riders.size();
		int riderId = (teamID * 100) + riderSize;// Stored how stage ID is stored. E.g. rider ID:101 will be team 1
													// rider 1
		System.out.println("Rider created with ID:" + riderId);
		return riderId;
	}

	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		int teamId = riderId / 100;
		int riderIndex = (riderId % 100) - 1; // Calculate index of rider with team
		// Check if team ID is valid
		teamIdNotRecognised(teamId);
		// Get the team object
		Team team = teams.get(teamId - 1);
		// Get the list of riders in the team
		List<Rider> riders = team.getRiders();
		// Check if rider index is valid
		if (riderIndex < 0 || riderIndex >= riders.size()) {
			throw new IDNotRecognisedException("Rider ID " + riderId + " not recognised");
		}
		// Remove the rider from the list of riders
		riders.remove(riderIndex);
	}

	class RiderResult {
		private int riderId;
		private LocalTime[] checkpoints; // the length of this should be how many checkpoints within the stage

		public RiderResult(int riderId, LocalTime... checkpoints) { // stores rider result in stage, this is user
																	// defined
			this.riderId = riderId;
			this.checkpoints = checkpoints;
		}

		public int getRiderId() {
			return riderId;
		}

		public LocalTime[] getCheckpoints() {
			return checkpoints;
		}

	}

	public void riderIdNotRecognised(int riderId) throws IDNotRecognisedException {
		int teamId = riderId / 100;
		int riderIndex = (riderId % 100) - 1;
		teamIdNotRecognised(teamId);
		Team team = teams.get(teamId - 1);
		List<Rider> riders = team.getRiders();
		if (riderIndex < 0 || riderIndex >= riders.size()) {
			throw new IDNotRecognisedException("Rider id not recognised for:" + riderId);
		}

	}
	public static boolean isArrayInAscendingOrder(LocalTime[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i].compareTo(array[i + 1]) > 0) {
                return false; // Array is not in ascending order
            }
        }
        return true; // Array is in ascending order
    }
	@Override
	// Aritra 
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointTimesException,
			InvalidStageStateException {
		// TODO Auto-generated method stub
		System.out.println("registerRiderResultsINStage is running");
		stageIDNotRecognisedException(stageId);
		riderIdNotRecognised(riderId);
		stageStateExceptionAddingCheckpoints(stageId);

		int raceId = stageId / 100; // gets raceId and stage index
		int stageIndex = (stageId % 100) - 1;
		Race race = races.get(raceId - 1); // gets race specified at the id and gets the stages within that race
		List<Stage> stages = race.getStages();
		Stage stage = stages.get(stageIndex); // gets the stage at the index specified
		RiderResult riderResult = new RiderResult(riderId, checkpoints);
		LocalTime[] riderCheckpointTimes = riderResult.getCheckpoints();
		List<Checkpoint> stageCheckpoints = stage.getCheckpoints();
		List<RiderResult> getRiderResults = stage.getRiderResult();

		for (int i = 0; getRiderResults.size() > i; i++) { // Loop to find if rider result is already in the stage
			RiderResult getRiderResult = getRiderResults.get(i);
			if (getRiderResult.getRiderId() == riderId) {
				throw new DuplicatedResultException("Rider:"+riderId+" result already found in system for stage:"+ stageId);
			}
		}
		if (isArrayInAscendingOrder(riderCheckpointTimes) == false) {
			throw new InvalidCheckpointTimesException("Times inputed should be in order");
		}

		if (stageCheckpoints.size() + 2 == riderCheckpointTimes.length) { // there should be 2 more checkpoint times
																			// than checkpoints to account for start and
			// finish of the race. e.g. if there is 5 checkpoints there should be 7 times.
			stage.addRiderResult(riderResult);

		} else {

			throw new InvalidCheckpointTimesException("There should be:" + (stageCheckpoints.size() + 2)
					+ " Times you have:" + riderCheckpointTimes.length);
		}

	}

	@Override
	// Aritra
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		System.out.println("gerRiderResultsInStage is running");
		stageIDNotRecognisedException(stageId);
		riderIdNotRecognised(riderId);
		int raceId = stageId / 100; // gets raceId and stage index
		int stageIndex = (stageId % 100) - 1;
		Race race = races.get(raceId - 1); // gets race specified at the id and gets the stages within that race
		List<Stage> stages = race.getStages();
		Stage stage = stages.get(stageIndex); // gets the stage at the index specified
		List<RiderResult> riderResults = stage.getRiderResult();

		for (int i = 0; riderResults.size() > i; i++) { // Loop to find rider with the specified ID
			RiderResult riderResult = riderResults.get(i);
			if (riderResult.getRiderId() == riderId) {
				LocalTime[] riderCheckpointTimes = riderResult.getCheckpoints();
				for (LocalTime localTime : riderCheckpointTimes) {
					System.out.println(localTime); // prints out all the rider's times within the quieried stage
				}
				assert riderCheckpointTimes.length != 0;
				return riderCheckpointTimes;
			}

		}

		return new LocalTime[0];// returns nothing if rider has not completed the stage
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
	// Aritra
	public void removeRaceByName(String name) throws NameNotRecognisedException {
		// TODO Auto-generated method stub
		System.out.println("Remove race by name is running");
		boolean nameFound = false;
		for (int i = 0; i < races.size(); i++) { // loops through all the races names
			Race race = races.get(i);
			String raceName = race.getName();
			if (raceName == name) { // if matching name is found it gets removed
				races.remove(i);
				nameFound = true;
			}

		}

		if (nameFound == false) {
			throw new NameNotRecognisedException("Name:" + name + " is not recognised");
		}

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
