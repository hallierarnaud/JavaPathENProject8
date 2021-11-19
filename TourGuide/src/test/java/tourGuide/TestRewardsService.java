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
import tourGuide.object.User;
import tourGuide.object.UserReward;
import tourGuide.object.VisitedLocationResponse;
import tourGuide.proxies.GpsProxy;
import tourGuide.proxies.RewardsProxy;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class TestRewardsService {

    @Mock
    private GpsProxy gpsProxy;

    @Mock
    private RewardsProxy rewardsProxy;

    @InjectMocks
    private RewardsService rewardsService;

	@Test
	public void userGetRewards() {
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(rewardsService);

        LocationResponse locationResponse = new LocationResponse();
        locationResponse.setLatitude(33.817595D);
        locationResponse.setLongitude(-117.922008D);
        AttractionResponse attractionResponse = new AttractionResponse(UUID.randomUUID(),"Disneyland", "Anaheim", "CA", 33.817595D, -117.922008D);
        List<AttractionResponse> attractionResponseList = new ArrayList<>();
        attractionResponseList.add(attractionResponse);
        Mockito.when(gpsProxy.getAttractions()).thenReturn(attractionResponseList);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocationResponse visitedLocationResponse = new VisitedLocationResponse(user.getUserId(), locationResponse, new Date());
        Mockito.when(gpsProxy.getUserLocation(user.getUserId())).thenReturn(visitedLocationResponse);
        user.addToVisitedLocationResponseList(visitedLocationResponse);
        Mockito.when(rewardsProxy.getRewards(attractionResponse.attractionId, user.getUserId())).thenReturn(100);
		tourGuideService.trackUserLocation(user);
		List<UserReward> userRewards = user.getUserRewards();
		assertTrue(userRewards.size() == 1);
	}
	
	/*@Test
	public void isWithinAttractionProximity() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		Attraction attraction = gpsUtil.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}*/
	
	/*@Ignore // Needs fixed - can throw ConcurrentModificationException
	@Test
	public void nearAllAttractions() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		InternalTestHelper.setInternalUserNumber(1);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		
		rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0));
		List<UserReward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));
		//tourGuideService.tracker.stopTracking();

		assertEquals(gpsUtil.getAttractions().size(), userRewards.size());
	}*/
	
}
