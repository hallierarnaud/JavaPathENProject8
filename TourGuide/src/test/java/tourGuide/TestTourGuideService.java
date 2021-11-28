package tourGuide;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import tourGuide.helper.InternalTestHelper;
import tourGuide.object.LocationResponse;
import tourGuide.object.ProviderResponse;
import tourGuide.object.User;
import tourGuide.object.VisitedLocationResponse;
import tourGuide.proxies.GpsProxy;
import tourGuide.proxies.PricerProxy;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class TestTourGuideService {

	@Mock
	private GpsProxy gpsProxy;

	@Mock
	private PricerProxy pricerProxy;

	@Mock
	private RewardsService rewardsService;

	@InjectMocks
	private final TourGuideService tourGuideService = new TourGuideService(rewardsService);

	@Test
	public void getUserLocation() {
		InternalTestHelper.setInternalUserNumber(0);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocationResponse visitedLocationResponse = new VisitedLocationResponse(user.getUserId(), new LocationResponse(0.0, 0.0), new Date());
		Mockito.when(gpsProxy.getUserLocation(user.getUserId())).thenReturn(visitedLocationResponse);

		tourGuideService.trackUserLocation(user);

		assertTrue(visitedLocationResponse.userId.equals(user.getUserId()));
	}
	
	@Test
	public void addUser() {
		RewardsService rewardsService = new RewardsService();
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(rewardsService);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		
		User retrievedUser = tourGuideService.getUser(user.getUserName());
		User retrievedUser2 = tourGuideService.getUser(user2.getUserName());
		
		assertEquals(user, retrievedUser);
		assertEquals(user2, retrievedUser2);
	}
	
	@Test
	public void getAllUsers() {
		RewardsService rewardsService = new RewardsService();
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(rewardsService);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		
		List<User> allUsers = tourGuideService.getAllUsers();
		
		assertTrue(allUsers.contains(user));
		assertTrue(allUsers.contains(user2));
	}

	/*@Test
	public void getNearbyAttractions() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocationResponse visitedLocationResponse = tourGuideService.trackUserLocation(user);
		
		List<Attraction> attractions = tourGuideService.getNearByAttractions(visitedLocationResponse);
		
		//tourGuideService.tracker.stopTracking();
		
		assertEquals(5, attractions.size());
	}*/

	@Test
	public void getTripDeals() {
		InternalTestHelper.setInternalUserNumber(0);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		List<ProviderResponse> providerResponseList = new ArrayList<>();
		ProviderResponse providerResponse = new ProviderResponse();
		providerResponse.setTripId(UUID.randomUUID());
		providerResponse.setName("TestProvider");
		providerResponse.setPrice(100);
		providerResponseList.add(providerResponse);
		Mockito.when(pricerProxy.getTripDeals(user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
						user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(),
						user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum())).thenReturn(providerResponseList);

		tourGuideService.getTripDeals(user);
		
		assertEquals(1, providerResponseList.size());
	}
	
	/*@Test
	public void getAllCurrentLocations() {

	}*/

}
