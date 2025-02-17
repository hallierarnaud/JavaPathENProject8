package tourGuide.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import tourGuide.helper.InternalTestHelper;
import tourGuide.object.AttractionResponse;
import tourGuide.object.LocationResponse;
import tourGuide.object.NearbyAttraction;
import tourGuide.object.ProviderResponse;
import tourGuide.object.User;
import tourGuide.object.UserReward;
import tourGuide.object.VisitedLocationResponse;
import tourGuide.proxies.GpsProxy;
import tourGuide.proxies.PricerProxy;

@Service
public class TourGuideService {

	@Autowired
	private GpsProxy gpsProxy;

	@Autowired
	private PricerProxy pricerProxy;

	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	private RewardsService rewardsService;
	boolean testMode = true;

	@Autowired
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

	public List<VisitedLocationResponse> getAllCurrentLocations() {
		List<User> users = getAllUsers();
		List<VisitedLocationResponse> allCurrentVisitedLocationResponseList = new ArrayList<>();
		for(User user : users) {
			List<VisitedLocationResponse> currentVisitedLocationResponseList = user.getVisitedLocationResponseList();
			Collections.sort(currentVisitedLocationResponseList);
			allCurrentVisitedLocationResponseList.add(currentVisitedLocationResponseList.get(0));
		}
		return allCurrentVisitedLocationResponseList;
	}
	
	public List<ProviderResponse> getTripDeals(User user) {
		int cumulativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<ProviderResponse> providers = pricerProxy.getTripDeals(user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
				user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}

	public VisitedLocationResponse trackUserLocation(User user) {
		VisitedLocationResponse visitedLocationResponse = gpsProxy.getUserLocation(user.getUserId());
		user.addToVisitedLocationResponseList(visitedLocationResponse);
		rewardsService.calculateRewards(user, false);
		return visitedLocationResponse;
	}

	public List<NearbyAttraction> getNearByAttractions(User user) {
		VisitedLocationResponse visitedLocationResponse = getUserLocationResponse(user);
		List<NearbyAttraction> nearbyAttractions = new ArrayList<>();
		for(AttractionResponse attractionResponse : gpsProxy.getAttractions()) {
			NearbyAttraction nearbyAttraction = new NearbyAttraction();
			nearbyAttraction.setAttractionName(attractionResponse.attractionName);
			nearbyAttraction.setAttractionLatitude(attractionResponse.latitude);
			nearbyAttraction.setAttractionLongitude(attractionResponse.longitude);
			nearbyAttraction.setTouristLatitude(visitedLocationResponse.locationResponse.latitude);
			nearbyAttraction.setTouristLongitude(visitedLocationResponse.locationResponse.longitude);
			nearbyAttraction.setDistanceBetweenAttractionAndTourist((int) rewardsService.getDistance(attractionResponse.latitude,
							attractionResponse.longitude, visitedLocationResponse.locationResponse));
			nearbyAttraction.setRewardsPoint(rewardsService.getRewardPoints(attractionResponse, user));
			nearbyAttractions.add(nearbyAttraction);
		}
		Collections.sort(nearbyAttractions);
		return nearbyAttractions.subList(0, 5);
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
			generateUserLocationHistory(user);
			
			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}

	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i-> {
			user.addToVisitedLocationResponseList(new VisitedLocationResponse(user.getUserId(), new LocationResponse(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}
	
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
