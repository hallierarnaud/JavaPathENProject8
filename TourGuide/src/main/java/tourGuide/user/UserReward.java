package tourGuide.user;

public class UserReward {

	public final VisitedLocationResponse visitedLocationResponse;
	public final AttractionResponse attractionResponse;
	private int rewardPoints;
	public UserReward(VisitedLocationResponse visitedLocationResponse, AttractionResponse attractionResponse, int rewardPoints) {
		this.visitedLocationResponse = visitedLocationResponse;
		this.attractionResponse = attractionResponse;
		this.rewardPoints = rewardPoints;
	}
	
	public UserReward(VisitedLocationResponse visitedLocationResponse, AttractionResponse attractionResponse) {
		this.visitedLocationResponse = visitedLocationResponse;
		this.attractionResponse = attractionResponse;
	}

	public void setRewardPoints(int rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	
	public int getRewardPoints() {
		return rewardPoints;
	}
	
}
