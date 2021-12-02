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
import tourGuide.object.AttractionResponse;
import tourGuide.object.LocationResponse;
import tourGuide.object.NearbyAttraction;
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
		// GIVEN
		InternalTestHelper.setInternalUserNumber(0);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocationResponse visitedLocationResponse = new VisitedLocationResponse(user.getUserId(), new LocationResponse(0.0, 0.0), new Date());
		Mockito.when(gpsProxy.getUserLocation(user.getUserId())).thenReturn(visitedLocationResponse);

		// WHEN
		VisitedLocationResponse visitedLocationResponseExpected = tourGuideService.trackUserLocation(user);

		// THEN
		assertTrue(visitedLocationResponseExpected.userId.equals(user.getUserId()));
	}
	
	@Test
	public void addUser() {
		// GIVEN
		InternalTestHelper.setInternalUserNumber(0);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");
		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);

		// WHEN
		User retrievedUser = tourGuideService.getUser(user.getUserName());
		User retrievedUser2 = tourGuideService.getUser(user2.getUserName());

		// THEN
		assertEquals(user, retrievedUser);
		assertEquals(user2, retrievedUser2);
	}
	
	@Test
	public void getAllUsers() {
		// GIVEN
		InternalTestHelper.setInternalUserNumber(0);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");
		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);

		// WHEN
		List<User> allUsers = tourGuideService.getAllUsers();

		// THEN
		assertTrue(allUsers.contains(user));
		assertTrue(allUsers.contains(user2));
	}

	@Test
	public void getNearbyAttractions() {
		// GIVEN
		InternalTestHelper.setInternalUserNumber(0);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocationResponse visitedLocationResponse = new VisitedLocationResponse(user.getUserId(), new LocationResponse(0.0, 0.0), new Date());
		Mockito.when(gpsProxy.getUserLocation(user.getUserId())).thenReturn(visitedLocationResponse);

		List<AttractionResponse> attractionResponseList = new ArrayList<>();
		AttractionResponse attractionResponse1 = new AttractionResponse(UUID.randomUUID(), "AttractionTest1", "CityTest1", "StateTest1", 1.0, 1.0);
		AttractionResponse attractionResponse2 = new AttractionResponse(UUID.randomUUID(), "AttractionTest2", "CityTest2", "StateTest2", 2.0, 2.0);
		AttractionResponse attractionResponse3 = new AttractionResponse(UUID.randomUUID(), "AttractionTest3", "CityTest3", "StateTest3", 3.0, 3.0);
		AttractionResponse attractionResponse4 = new AttractionResponse(UUID.randomUUID(), "AttractionTest4", "CityTest4", "StateTest4", 4.0, 4.0);
		AttractionResponse attractionResponse5 = new AttractionResponse(UUID.randomUUID(), "AttractionTest5", "CityTest5", "StateTest5", 5.0, 5.0);
		attractionResponseList.add(attractionResponse1);
		attractionResponseList.add(attractionResponse2);
		attractionResponseList.add(attractionResponse3);
		attractionResponseList.add(attractionResponse4);
		attractionResponseList.add(attractionResponse5);
		Mockito.when(gpsProxy.getAttractions()).thenReturn(attractionResponseList);

		// WHEN
		List<NearbyAttraction> nearbyAttractionList = tourGuideService.getNearByAttractions(user);

		//THEN
		assertEquals(5, nearbyAttractionList.size());
	}

	@Test
	public void getTripDeals() {
		// GIVEN
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

		// WHEN
		List<ProviderResponse> providerResponseListExpected = tourGuideService.getTripDeals(user);

		// THEN
		assertEquals(1, providerResponseListExpected.size());
	}
	
	@Test
	public void getAllCurrentLocations() {
		// GIVEN
		RewardsService rewardsService = new RewardsService();
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");
		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		VisitedLocationResponse visitedLocationResponse = new VisitedLocationResponse(user.getUserId(), new LocationResponse(0.0, 0.0), new Date());
		user.addToVisitedLocationResponseList(visitedLocationResponse);
		user2.addToVisitedLocationResponseList(visitedLocationResponse);

		// WHEN
		List<VisitedLocationResponse> visitedLocationResponseList = tourGuideService.getAllCurrentLocations();

		// THEN
		assertEquals(2, visitedLocationResponseList.size());
	}

}
