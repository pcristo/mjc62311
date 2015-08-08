package replication;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Provides a means of tracking the results of incoming responses 
 * from the Replication Managers, as well as counting the number 
 * of bad responses each RM has sent since the last correct one.
 * @author Patrick
 */
public class VotingTable {
	/**
	 * List of replica manager IDs and their failures.
	 */
	private HashMap<Long, Byte> replicaManagers = 
			new HashMap<Long, Byte>();	
	/**
	 * The structure that records votes for each transaction. The first
	 * Long represents the sequence number of the transaction. The second
	 * Long is the RM id and the Boolean is the vote it cast.
	 */
	private HashMap<Long, HashMap<Long, Boolean>> votingRecord = 
			new HashMap<Long, HashMap<Long, Boolean>>();	
	
	/**
	 * Casts a vote (true/false) from Replica Manager 
	 * @param sequenceID
	 * @param replicaID
	 * @param vote
	 */
	public void castVote(Long sequenceID, Long replicaID, Boolean vote) {
		// Initialize a new sequence if never seen before
		if (!votingRecord.containsKey(sequenceID))
			createNewSequence(sequenceID);
	
		// Do not allow a replica to vote twice (if so, only first vote counts)
		if (votingRecord.get(sequenceID).containsKey(replicaID))
			return;
		
		// Insert the new vote to the table
		votingRecord.get(sequenceID).put(replicaID, vote);		
		
		// If all votes are in, check who sent wrong messages and update
		// the replica manager table accordingly. '>=' operator is used, 
		// because it's possible that a replica manager was removed since
		// voting started (due to failure, for example), resulting in more 
		// total votes than number of active RMs
		if (votingRecord.get(sequenceID).size() >= replicaManagers.size()) 
			identifyInvalidVoters(sequenceID);
	}	
	
	/**
	 * Checks the voting results for a sequenceID. If insufficient votes were
	 * received, null is returned. If enough votes agree, true or false is returned. 
	 * @param sequenceID
	 * @return true or false if voting complete, null if not ready
	 */
	public Boolean checkResults(Long sequenceID) {
		// if sequence doesn't exist, then we haven't received a single vote yet
		if (!votingRecord.containsKey(sequenceID))
			return null;
		
		HashMap<Long, Boolean> votes = votingRecord.get(sequenceID);
		
		// at least one more than half the RMs must have voted to have quorum
		int quorumSize = (int) (Math.floor(replicaManagers.size() / 2) + 1);
		int trueVotes = 0;
		int falseVotes = 0;
		
		for(Long replica : votes.keySet()) {
			if (votes.get(replica))
				trueVotes++;
			else
				falseVotes++;
		}
		
		if (trueVotes >= quorumSize) return true;
		if (falseVotes >= quorumSize) return false;
		
		// insufficient number of votes agree... so return null
		return null;
	}
	
	/**
	 * Checks if any RMs have more failures than indicated by the provided
	 * threshold value
	 * @param threshold The number of failures at which an RM is deemed problematic
	 * @return The ids of all problematic RMs, or empty list if none
	 */
	public ArrayList<Long> identifyProblemRMs(int threshold) {
		ArrayList<Long> problemRmList = new ArrayList<Long>();
		
		for(Long RM : replicaManagers.keySet()) {
			if (replicaManagers.get(RM) >= threshold)
				problemRmList.add(RM);
		}
		
		return problemRmList;		
	}
	
	/**
	 * Registers a Replication Manager 
	 */
	public void registerRM(Long ID) {
		replicaManagers.put(ID, (byte) 0);
	}
	
	/**
	 * Unregisters a Replication Manager
	 */
	public void unregisterRM(Long ID) {
		replicaManagers.remove(ID);
	}
	
	
	private void createNewSequence(Long id) {
		HashMap<Long, Boolean> vote = new HashMap<Long, Boolean>();		
		votingRecord.put(id, vote);		
	}
	
	/**
	 * Processes the votes of a sequenceID and either adds 1 to the number 
	 * of failures for an RM or resets the count to zero if the RM voted 
	 * correctly.
	 * @param sequenceID
	 */
	private void identifyInvalidVoters(Long sequenceID) {
		HashMap<Long, Boolean> votes = votingRecord.get(sequenceID);
		
		Boolean finalResult = checkResults(sequenceID);
		if (finalResult == null) return;
		
		for(Long id : votes.keySet()) {
			if (votes.get(id) == finalResult)
				replicaManagers.replace(id, (byte) 0);
			else 
				replicaManagers.replace(id, (byte) (replicaManagers.get(id) + 1));
		}		
	}
}
