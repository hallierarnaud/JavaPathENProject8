package tourGuide.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class User {
	private final UUID userId;
	private final String userName;
	private String phoneNumber;
	private String emailAddress;
	private Date latestLocationTimestamp;
	private List<VisitedLocationResponse> visitedLocationResponseList = new ArrayList<>();
	private List<UserReward> userRewards = new ArrayList<>();
	private UserPreferences userPreferences = new UserPreferences();
	private List<ProviderResponse> tripDeals = new ArrayList<>();
	public User(UUID userId, String userName, String phoneNumber, String emailAddress) {
		this.userId = userId;
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}
	
	public UUID getUserId() {
		return userId;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setLatestLocationTimestamp(Date latestLocationTimestamp) {
		this.latestLocationTimestamp = latestLocationTimestamp;
	}
	
	public Date getLatestLocationTimestamp() {
		return latestLocationTimestamp;
	}

	public void addToVisitedLocationResponseList(VisitedLocationResponse visitedLocationResponse) {
		visitedLocationResponseList.add(visitedLocationResponse);
	}

	public List<VisitedLocationResponse> getVisitedLocationResponseList() {
		return visitedLocationResponseList;
	}
	
	public void clearVisitedLocationResponseList() {
		visitedLocationResponseList.clear();
	}
	
	public void addUserReward(UserReward userReward) {
		if(userRewards.stream().filter(r -> !r.attractionResponse.attractionName.equals(userReward.attractionResponse)).count() == 0) {
			userRewards.add(userReward);
		}
	}
	
	public List<UserReward> getUserRewards() {
		return userRewards;
	}
	
	public UserPreferences getUserPreferences() {
		return userPreferences;
	}
	
	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	public VisitedLocationResponse getLastVisitedLocationResponse() {
		//return Iterables.getLast(visitedLocationResponseList);
		return visitedLocationResponseList.get(visitedLocationResponseList.size() - 1);
	}
	
	public void setTripDeals(List<ProviderResponse> tripDeals) {
		this.tripDeals = tripDeals;
	}
	
	public List<ProviderResponse> getTripDeals() {
		return tripDeals;
	}

}
