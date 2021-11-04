package tourGuide.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import tourGuide.helper.InternalTestHelper;
import tourGuide.proxies.GpsProxy;
import tourGuide.user.AttractionResponse;
import tourGuide.user.User;
import tourGuide.user.UserReward;
import tourGuide.user.VisitedLocationResponse;
import tripPricer.Provider;
import tripPricer.TripPricer;

@Service
public class TourGuideService {

	@Autowired
	private GpsProxy gpsProxy;

	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	private final RewardsService rewardsService;
	private final TripPricer tripPricer = new TripPricer();
	boolean testMode = true;
	
	public TourGuideService(RewardsService rewardsService) {
		this.rewardsService = rewardsService;
		
		if(testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
	}
	
	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}
	
	public VisitedLocationResponse getUserLocationResponse(User user) {
		VisitedLocationResponse visitedLocationResponse = (user.getVisitedLocationResponseList().size() > 0) ?
			user.getLastVisitedLocationResponse() :
			trackUserLocation(user);
		return visitedLocationResponse;
	}
	
	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}
	
	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}
	
	public void addUser(User user) {
		if(!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}
	
	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(), 
				user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}

	public VisitedLocationResponse trackUserLocation(User user) {
		VisitedLocationResponse visitedLocationResponse = gpsProxy.getUserLocation(user.getUserId());
		user.addToVisitedLocationResponseList(visitedLocationResponse);
		rewardsService.calculateRewards(user);
		return visitedLocationResponse;
	}

	public List<AttractionResponse> getNearByAttractions(VisitedLocationResponse visitedLocationResponse) {
		List<AttractionResponse> nearbyAttractions = new ArrayList<>();
		for(AttractionResponse attractionResponse : gpsProxy.getAttractions()) {
			if(rewardsService.isWithinAttractionProximity(attractionResponse, visitedLocationResponse.locationResponse)) {
				nearbyAttractions.add(attractionResponse);
			}
		}
		return nearbyAttractions;
	}
	
	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
	private final Map<String, User> internalUserMap = new HashMap<>();
	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			//generateUserLocationHistory(user);
			
			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}

	//TODO
	/*private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i-> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}*/
	
	private double generateRandomLongitude() {
		double leftLimit = -180;
	    double rightLimit = 180;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}
	
	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
	    double rightLimit = 85.05112878;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}
	
	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
	    return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}
	
}
